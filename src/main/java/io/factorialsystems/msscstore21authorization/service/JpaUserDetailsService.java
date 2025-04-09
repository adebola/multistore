package io.factorialsystems.msscstore21authorization.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscstore21authorization.dto.ApplicationUserDTO;
import io.factorialsystems.msscstore21authorization.dto.MailRequestDTO;
import io.factorialsystems.msscstore21authorization.dto.PagedDTO;
import io.factorialsystems.msscstore21authorization.dto.RegisterUserDTO;
import io.factorialsystems.msscstore21authorization.exception.UserExistsException;
import io.factorialsystems.msscstore21authorization.mapper.ApplicationUserMapper;
import io.factorialsystems.msscstore21authorization.model.ApplicationUser;
import io.factorialsystems.msscstore21authorization.model.UserAuthority;
import io.factorialsystems.msscstore21authorization.repository.AuthorityRepository;
import io.factorialsystems.msscstore21authorization.repository.UserRepository;
import io.factorialsystems.msscstore21authorization.security.AuthorizationProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final MQService mqService;
    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final ApplicationUserMapper applicationUserMapper;
    private final AuthorizationProperties authorizationProperties;

    public static final String CREATE_USER = "Create-User";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            log.error("Session is null");
            throw new UsernameNotFoundException("Session is null");
        }

        String tenantId = (String) session.getAttribute("TENANT_ID");
        log.info("loadUserByUserName Tenant ID: {}", tenantId);

        final Map<String, String> m = new HashMap<>();
        m.put("username", username);
        m.put("tenantId", tenantId);

        Optional<ApplicationUser> applicationUser = userRepository.findByUserNameAndTenantId(m);

        if (applicationUser.isEmpty()) {
            log.error("User {}. Not Found for Tenant {}", username, tenantId);
            throw new UsernameNotFoundException("Invalid UserName or Password");
        }

        final String userId = applicationUser.get().getId();
        session.setAttribute("USER_ID", userId);

        return applicationUser.get().toUserDetails();
    }

    public PagedDTO<ApplicationUserDTO> loadUsers(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Page<ApplicationUser> users = userRepository.findAll();
        return createDTO(users);
    }

    public void createUser(RegisterUserDTO registerUserDTO) {
        if (!registerUserDTO.getPassword().equals(registerUserDTO.getMatchingPassword())) {
            log.error("Passwords do not match for user {}", registerUserDTO.getUserName());
            throw new IllegalArgumentException("Passwords do not match");
        }

        final HttpSession session = request.getSession(false);

        if (session == null) {
            log.error("Session is null creating User {}", registerUserDTO.getUserName());
            throw new RuntimeException("Null Session creating User");
        }

        final String tenantId = (String) session.getAttribute("TENANT_ID");
        if (tenantId == null) {
            log.error("TenantId is null creating User {}", registerUserDTO.getUserName());
            throw new RuntimeException("Null TenantId creating User");
        }

        final Map<String, String> m = new HashMap<>();
        m.put("username", registerUserDTO.getUserName());
        m.put("tenantId", tenantId);
        m.put("email", registerUserDTO.getEmail());

        if (userRepository.IsExistsByUserNameInTenant(m)) {
            log.error("User {} already exists", registerUserDTO.getUserName());
            throw new UserExistsException(String.format("User %s already exists", registerUserDTO.getUserName()));
        }

        if (userRepository.IsExistsByEmailInTenant(m)) {
            log.error("Email {} already exists", registerUserDTO.getEmail());
            throw new UserExistsException(String.format("Email %s already exists", registerUserDTO.getEmail()));
        }

        List<UserAuthority> authorities = authorityRepository.findDefaultRoleForTenant(tenantId);

        if (authorities.isEmpty()) {
            log.error("Default user role not found for Tenant {}", tenantId);
            throw new IllegalArgumentException(String.format("Default user role not found for tenantId: %s", tenantId));
        }

        ApplicationUser applicationUser = ApplicationUser.create(
                registerUserDTO.getUserName(),
                registerUserDTO.getFirstName(),
                registerUserDTO.getLastName(),
                registerUserDTO.getEmail(),
                passwordEncoder.encode(registerUserDTO.getPassword()),
                tenantId,
                Set.of(authorities.getFirst())
        );

        userRepository.save(applicationUser);
        mqService.audit(CREATE_USER,
                String.format("User %s registered for Tenant %s", applicationUser.getUserName(), tenantId),
                applicationUser.getUserName(),
                tenantId
        );
        log.info("User created successfully {} for TenantId {}", applicationUser.getUserName(), tenantId);

        // Send confirmation email
        final MailRequestDTO dto = getMailRequestDTO(applicationUser, tenantId);
        log.info("Sending confirmation email to {}", dto);
        mqService.sendMail(dto);
    }

    private MailRequestDTO getMailRequestDTO(ApplicationUser applicationUser, String tenantId) {
        var confirmationLink = String.format("%s/confirm?id=%s", authorizationProperties.getLocation(), applicationUser.getId());
        var subject = "Confirm your email address";

        var subMessage = """
                Welcome to Factorial Store!<br>
                Your account has been created successfully.<br>
                Please click the link below to confirm your email address:<br><br><br>
                <a href="%s">Confirm Email</a><br><br><br>
                """;

        var message = String.format(subMessage, confirmationLink);
        return new MailRequestDTO(applicationUser.getEmail(), subject, message, tenantId);
    }

    private PagedDTO<ApplicationUserDTO> createDTO(Page<ApplicationUser> users) {
        PagedDTO<ApplicationUserDTO> page = new PagedDTO<>();
        page.setTotalSize((int) users.getTotal());

        page.setPageNumber(users.getPageNum());
        page.setPageSize(users.getPageSize());
        page.setPages(users.getPages());
        page.setList(
                users.getResult().stream()
                        .map(applicationUserMapper::toDtoSlim)
                        .toList()
        );

        return page;
    }
}

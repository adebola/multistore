package io.factorialsystems.msscstore21authorization.controller;

import io.factorialsystems.msscstore21authorization.dto.RegisterUserDTO;
import io.factorialsystems.msscstore21authorization.model.ApplicationUser;
import io.factorialsystems.msscstore21authorization.model.Tenant;
import io.factorialsystems.msscstore21authorization.repository.UserRepository;
import io.factorialsystems.msscstore21authorization.service.JpaTenantService;
import io.factorialsystems.msscstore21authorization.service.JpaUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final JpaTenantService tenantService;
    private final UserRepository userRepository;
    private final JpaUserDetailsService jpaUserDetailsService;

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            log.error("RegisterController Session is null, unable to Login");
            model.addAttribute("errorMessage", "Session is null, unable to Login");
            return "error";
        }

        final String tenantId = (String) session.getAttribute("TENANT_ID");

        if (tenantId == null) {
            log.error("RegisterController TenantId is null, unable to Login");
            model.addAttribute("errorMessage", "TenantId is null, unable to Login");
            return "error";
        }

        Tenant tenant = tenantService.findById(tenantId);

        if (tenant == null) {
            log.error("RegisterController, Invalid Tenant {}", tenantId);
            model.addAttribute("errorMessage", String.format("Invalid Tenant %s", tenantId));
            return "error";
        }

        model.addAttribute("tenant", tenant.getName());
        return "register";
    }

    @GetMapping("/confirm")
    public String confirmEmail(Model model, HttpServletRequest request) {
        final String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            log.error("Confirmation ID is missing");
            model.addAttribute("errorMessage", "Confirmation ID is missing");
            return "error";
        }

        Optional<ApplicationUser> user = userRepository.findById(id);

        if (user.isEmpty()) {
            log.error("User ID {} not found, in confirmation", id);
            model.addAttribute("errorMessage", "Invalid parameters");
            return "error";
        }

        final ApplicationUser u = user.get();

        if (u.getEnabled() && !u.getLocked()) {
            log.info("User {} has already confirmed their email", u.getUserName());
            model.addAttribute("message", "Email already confirmed!");
            return "confirmation";
        }

        userRepository.confirmUser(u.getId());
        model.addAttribute("message", "Email confirmed! successfully");
        return "confirmation"; // Return the appropriate view name
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid RegisterUserDTO user, RedirectAttributes redirectAttributes) {

        try {
            jpaUserDetailsService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Error creating user {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error occurred during registration. Please try again.");
            return "redirect:/register";
        }
    }
}
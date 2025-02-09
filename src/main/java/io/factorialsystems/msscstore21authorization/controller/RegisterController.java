package io.factorialsystems.msscstore21authorization.controller;

import io.factorialsystems.msscstore21authorization.dto.RegisterUserDTO;
import io.factorialsystems.msscstore21authorization.model.Tenant;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final JpaTenantService tenantService;
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

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid RegisterUserDTO user) {
        log.info("Registering user {}", user);

//        try {
//            jpaUserDetailsService.createUser(user);
//        } catch (Exception e) {
//            log.error("Error creating user {}", e.getMessage());
//            //redirectAttributes.addFlashAttribute("error", e.getMessage());
//            model.addAttribute("errorMessage", e.getMessage());
//            return "redirect:/register";
//        }
//
//        //redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
        return "redirect:/login";
    }
}
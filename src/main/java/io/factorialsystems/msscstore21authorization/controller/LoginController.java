package io.factorialsystems.msscstore21authorization.controller;

import io.factorialsystems.msscstore21authorization.model.Tenant;
import io.factorialsystems.msscstore21authorization.service.JpaTenantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final JpaTenantService tenantService;

	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		final HttpSession session = request.getSession(false);

		if (session == null) {
			log.error("LoginController Session is null, unable to Login");
			model.addAttribute("errorMessage", "Session is null, unable to Login");
			return "error";
		}

		final String tenantId = (String) session.getAttribute("TENANT_ID");

		if (tenantId == null) {
			log.error("LoginController TenantId is null, unable to Login");
			model.addAttribute("errorMessage", "TenantId is null, unable to Login");
			return "error";
		}

		Tenant tenant = tenantService.findById(tenantId);

		if (tenant == null) {
			log.error("LoginController, Invalid Tenant {}", tenantId);
			model.addAttribute("errorMessage", String.format("Invalid Tenant %s", tenantId));
			return "error";
		}

		model.addAttribute("tenant", tenant.getName());
		return "login";
	}
}

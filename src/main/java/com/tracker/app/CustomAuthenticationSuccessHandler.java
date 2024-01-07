package com.tracker.app;

import java.io.IOException;
import java.util.Collection;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String redirectUrl = null;

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			System.out.println(grantedAuthority.getAuthority());
			if (grantedAuthority.getAuthority().equals("ROLE_DEVELOPER")) {
				redirectUrl = "/Developer/Dashboard";
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				redirectUrl = "/Admin/Dashboard";
				break;
			} 	else if (grantedAuthority.getAuthority().equals("ROLE_MANAGER")) {
				redirectUrl = "/Manager/Dashboard";
				break;
			}	else if (grantedAuthority.getAuthority().equals("ROLE_TESTER")) {
				redirectUrl = "/Tester/Dashboard";
				break;
			} 	else if (grantedAuthority.getAuthority().equals("ROLE_SUPPORT")) {
				redirectUrl = "/Support/Dashboard";
				break;
			}
		System.out.println("redirectUrl ffffff" + redirectUrl);
		if (redirectUrl == null) {
			throw new IllegalStateException();
		}
		}
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
	
	}

	
}
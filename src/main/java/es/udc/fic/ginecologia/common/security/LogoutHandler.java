package es.udc.fic.ginecologia.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import es.udc.fic.ginecologia.model.CustomUserDetails;

@Component
public class LogoutHandler implements LogoutSuccessHandler {
	
	private static final Logger logger = LogManager.getLogger(LogoutHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		
		logger.warn("User with username=" + username + " logged out successfully");
		
	    redirectStrategy.sendRedirect(request, response,"/");
	}

	
}

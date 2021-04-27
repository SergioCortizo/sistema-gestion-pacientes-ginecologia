package es.udc.fic.ginecologia.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import es.udc.fic.ginecologia.model.CustomUserDetails;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	private static final Logger logger = LogManager.getLogger(CustomAccessDeniedHandler.class);
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String message = "ERROR 403: ";

		if (authentication == null) {
			message = message + "Anonymous user";
		} else {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			String username = userDetails.getUsername();
			message = message + username;
		}

		message = message + " tried to access endpoint=" + request.getRequestURI() + " with method="
				+ request.getMethod();
		
		logger.error(message);
		
		response.sendRedirect(request.getContextPath() + "/error/403");

	}

}

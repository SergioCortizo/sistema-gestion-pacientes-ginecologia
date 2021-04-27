package es.udc.fic.ginecologia.common.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.sun.istack.Nullable;

import es.udc.fic.ginecologia.model.CustomUserDetails;

//@Component
public class MethodHandler extends DefaultHandlerExceptionResolver {

	private static final Logger logger = LogManager.getLogger(LogoutHandler.class);

	@Override
	protected ModelAndView handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.sendRedirect(request.getContextPath() + "/error/405");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();

		logger.error("ERROR 405: User with username=" + username + " tried to execute unsupported method=" + request.getMethod()
				+ " with endpoint=" + request.getRequestURI());

		return new ModelAndView();

	}

	@Override
	protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request,
			HttpServletResponse response, @Nullable Object handler) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		response.sendRedirect(request.getContextPath() + "/error/404");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		
		logger.error("ERROR 404: User with username=" + username + " tried to execute unknown endpoint=" + request.getRequestURI()
		+ " with method=" + request.getMethod());
		
		return new ModelAndView();
	}
}

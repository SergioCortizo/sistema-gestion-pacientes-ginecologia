package es.udc.fic.ginecologia.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import es.udc.fic.ginecologia.model.CustomUserDetails;

@ControllerAdvice
public class GlobalExceptionController {

	private static final Logger logger = LogManager.getLogger(GlobalExceptionController.class);

	@ExceptionHandler(value = { NoHandlerFoundException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ModelAndView handle404(HttpServletRequest request, Exception e) {
		ModelAndView mav = new ModelAndView("/error/404");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			String username = userDetails.getUsername();

			logger.error("ERROR 404: User with username=" + username + " tried to execute unknown endpoint="
					+ request.getRequestURI() + " with method=" + request.getMethod());

			return mav;
		}
		return null;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ModelAndView handleError405(HttpServletRequest request, Exception e) {
		ModelAndView mav = new ModelAndView("/error/405");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();

		logger.error("ERROR 405: User with username=" + username + " tried to execute unsupported method="
				+ request.getMethod() + " with endpoint=" + request.getRequestURI());

		return mav;
	}
}

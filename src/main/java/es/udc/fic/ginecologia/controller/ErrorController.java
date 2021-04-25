package es.udc.fic.ginecologia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

	@GetMapping("/error/403")
	public String goTo403Page() {
		return "error/403";
	}
	
	@GetMapping("/error/405")
	public String goTo405Page() {
		return "error/405";
	}
}

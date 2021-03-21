package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.udc.fic.ginecologia.service.SettingsService;

@ControllerAdvice(annotations = Controller.class)
public class GlobalAttributes {

	@Autowired
	SettingsService settingsService;
	
	@ModelAttribute("enterpriseName")
	public String getEnterpriseName() {
		return settingsService.getEnterpriseName().getValue();
	}
	
	@ModelAttribute("logo")
	public String getLogo() {
		return settingsService.getLogo().getValue();
	}
}

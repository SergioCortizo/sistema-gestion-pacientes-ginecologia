package es.udc.fic.ginecologia.form;

import org.springframework.web.multipart.MultipartFile;

public class SettingsForm {
	
	private String enterpriseName;
	
	private MultipartFile logo;

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public MultipartFile getLogo() {
		return logo;
	}

	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}
	
}

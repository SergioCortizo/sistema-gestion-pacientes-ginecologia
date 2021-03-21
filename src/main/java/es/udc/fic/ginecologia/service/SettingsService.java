package es.udc.fic.ginecologia.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Settings;

public interface SettingsService {

	void setSettings(Integer userId, String enterpriseName, MultipartFile logo)
			throws InstanceNotFoundException, PermissionException, IOException;

	Settings getEnterpriseName();

	Settings getLogo();

}

package es.udc.fic.ginecologia.service;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Settings;
import es.udc.fic.ginecologia.repository.SettingsDao;

@Transactional
@Service
public class SettingsServiceImpl implements SettingsService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private SettingsDao settingsDao;

	Tika tika = new Tika();

	private final String enterpriseNameKey = "enterpriseName";

	private final String logoKey = "logo";

	@Override
	public void setSettings(Integer userId, String enterpriseName, MultipartFile logo)
			throws InstanceNotFoundException, PermissionException, IOException {
		if (!permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<Settings> enterpriseNameSettings = settingsDao.findById(enterpriseNameKey);

		Optional<Settings> logoSettings = settingsDao.findById(logoKey);

		if (!enterpriseNameSettings.isPresent()) {
			throw new InstanceNotFoundException("entities.settings", enterpriseNameKey);
		}

		if (!logoSettings.isPresent()) {
			throw new InstanceNotFoundException("entities.settings", logoKey);
		}

		enterpriseNameSettings.get().setValue(enterpriseName);

		if (!logo.isEmpty()) {
			String detectedType = tika.detect(logo.getBytes());
			
			StringBuilder sb = new StringBuilder();
			sb.append("data:"+ detectedType +";base64,");
			sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(logo.getBytes(), false)));
			logoSettings.get().setValue(sb.toString());
		}

	}

	@Override
	public Settings getEnterpriseName(){
		
		Optional<Settings> enterpriseNameSettings = settingsDao.findById(enterpriseNameKey);

		return enterpriseNameSettings.get();
	}

	@Override
	public Settings getLogo(){

		Optional<Settings> logoSettings = settingsDao.findById(logoKey);

		return logoSettings.get();
	}
}

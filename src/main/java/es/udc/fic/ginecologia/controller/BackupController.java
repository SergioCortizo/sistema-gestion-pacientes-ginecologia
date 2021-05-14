package es.udc.fic.ginecologia.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.BackupForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.service.BackupService;

@Controller
public class BackupController {

	@Autowired
	PermissionChecker permissionChecker;
	
	@Autowired
	BackupService backupService;

	// Go to backup view
	@GetMapping("/backup/backup-management")
	public String goToBackupManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/backup/backup-management");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/backup/backup-management");
			return "/error/403";
		}

		model.addAttribute("backupForm", new BackupForm());

		LoggingUtility.logGetResource(username, "GET", "/backup/backup-management");

		return "backup/backup-management";
	}

	// Generate backup
	@GetMapping("/backup/generate-backup")
	public ResponseEntity<Resource> generateBackup(Model model)
			throws ClassNotFoundException, IOException, SQLException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();

		File file = backupService.generateBackup();

		LoggingUtility.logGeneratedBackup(username);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/zip"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.toString() + "\"")
				.body(new ByteArrayResource(Files.readAllBytes(file.toPath())));
	}

	// Restore database
	@PostMapping("/backup/restore-database")
	public String restoreDatabase(@ModelAttribute BackupForm backupForm, Model model)
			throws ClassNotFoundException, IOException, SQLException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/backup/restore-database");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/backup/restore-database");
			return "/error/403";
		}
		
		model.addAttribute("backupForm", new BackupForm());

		MultipartFile restoreFile = backupForm.getBackupFile();
		
		String backupName = restoreFile.getOriginalFilename();
		
		if (backupName.contains("..")) {
			model.addAttribute("restorationResult", false);
			LoggingUtility.logRestoreDatabaseFailure(username, backupName);
			return "backup/backup-management";
		}

		boolean restorationResult = backupService.restoreDatabase(restoreFile);

		model.addAttribute("restorationResult", restorationResult);
		
		if (restorationResult) {
			LoggingUtility.logRestoreDatabaseSucess(username, backupName);
		} else {
			LoggingUtility.logRestoreDatabaseFailure(username, backupName);
		}

		return "backup/backup-management";
	}

}

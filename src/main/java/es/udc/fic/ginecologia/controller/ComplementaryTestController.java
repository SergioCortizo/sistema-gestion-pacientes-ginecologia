package es.udc.fic.ginecologia.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.ComplementaryTest;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.service.ComplementaryTestService;

@Controller
public class ComplementaryTestController {

	@Autowired
	ComplementaryTestService complementaryTestService;
	
	@Autowired
	PermissionChecker permissionChecker;
	
	@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity < Resource > downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		
        // Load file as Resource
        ComplementaryTest complementaryTest = complementaryTestService.getFile(fileName);
        
        LoggingUtility.logDownloadFile(username, fileName);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(complementaryTest.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + complementaryTest.getFileName() + "\"")
            .body(new ByteArrayResource(complementaryTest.getData()));
    }
	
}

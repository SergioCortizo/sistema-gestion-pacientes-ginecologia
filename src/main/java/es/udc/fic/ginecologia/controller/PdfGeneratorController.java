package es.udc.fic.ginecologia.controller;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.PdfGeneratorUtil;
import es.udc.fic.ginecologia.form.PatientDetails;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.LogLine;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.service.LogService;
import es.udc.fic.ginecologia.service.PatientService;
import es.udc.fic.ginecologia.service.RecipeService;
import es.udc.fic.ginecologia.service.SettingsService;

@Controller
public class PdfGeneratorController {

	@Autowired
	PdfGeneratorUtil pdfGenaratorUtil;

	@Autowired
	SettingsService settingsService;

	@Autowired
	RecipeService recipeService;

	@Autowired
	PatientService patientService;
	
	@Autowired
	LogService logService;

	// Recipe PDF
	@GetMapping("/recipe/get-recipe/{id}")
	public ResponseEntity<byte[]> generateRecipe(@PathVariable Integer id, Model model) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		Map<String, Object> data = setBasicData();
		data.put("recipe", recipeService.findById(userId, id));

		ResponseEntity<byte[]> response = prepareResponse("patient/recipe-pdf", "recipe-recipeId " + id, data);
		
		LoggingUtility.downloadedRecipe(username, id);

		return response;

	}

	// Monitoring report PDF
	@GetMapping("/patient/monitoring-report/{id}")
	public ResponseEntity<byte[]> generateMonitoringReport(@PathVariable Long id, Model model) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		Patient patientFound = patientService.findPatient(userId, id);

		PatientDetails patient = new PatientDetails(patientFound);

		Map<String, Object> data = setBasicData();
		data.put("patient", patient);

		ResponseEntity<byte[]> response = prepareResponse("patient/monitoring-report", "monitoring-report-histnumber:" + id, data);
		
		LoggingUtility.logDownloadMonitoringReport(username, id);

		return response;

	}
	
	// Log report PDF
	@GetMapping("/log/get-logs/")
	public ResponseEntity<byte[]> generateLogReport(Model model) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();
		
		Iterable<LogLine> logLines = logService.findLogs(userId);
		
		Map<String, Object> data = setBasicData();
		data.put("logLines", logLines);
		
		ResponseEntity<byte[]> response = prepareResponse("log/access-report", "access-report-" + LocalDateTime.now(), data);
		
		LoggingUtility.logDownloadAccessReport(username);
		
		return response;
	}

	private Map<String, Object> setBasicData() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("enterprisename", settingsService.getEnterpriseName().getValue());
		data.put("logo", settingsService.getLogo().getValue());

		return data;
	}

	private ResponseEntity<byte[]> prepareResponse(String template, String name, Map<String, Object> data)
			throws Exception {
		File pdf = pdfGenaratorUtil.createPdf(template, data);

		byte[] contents = Files.readAllBytes(pdf.toPath());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);

		String filename = name + ".pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);

		return response;
	}

}

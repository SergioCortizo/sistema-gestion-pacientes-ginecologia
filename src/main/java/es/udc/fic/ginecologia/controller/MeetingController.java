package es.udc.fic.ginecologia.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.MeetingForm;
import es.udc.fic.ginecologia.form.RecipeConversor;
import es.udc.fic.ginecologia.model.Answer;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Question;
import es.udc.fic.ginecologia.model.Recipe;
import es.udc.fic.ginecologia.service.MedicineService;
import es.udc.fic.ginecologia.service.MeetingService;

@Controller
public class MeetingController {

	@Autowired
	PermissionChecker permissionChecker;

	@Autowired
	MeetingService meetingService;

	@Autowired
	MedicineService medicineService;

	@PostMapping("/patient/add-meeting/{id}")
	public String saveMeeting(@PathVariable Long id, @ModelAttribute MeetingForm addMeetingForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		List<Answer> answers = new ArrayList<>();

		if (addMeetingForm.getQuestions() != null) {
			for (int i = 0; i < addMeetingForm.getQuestions().size(); i++) {
				Question question = new Question();
				question.setQuestion(addMeetingForm.getQuestions().get(i));

				Answer answer = new Answer();
				answer.setQuestion(question);
				answer.setAnswer(addMeetingForm.getAnswers().get(i));
				answers.add(answer);
			}
		}
		
		Set<Recipe> recipes = new HashSet<>();
		
		if (addMeetingForm.getRecipes() != null) {
			recipes = RecipeConversor.convertToRecipeList(addMeetingForm);
		}

		try {
			meetingService.addMeeting(userId, id, addMeetingForm.getActivity(), addMeetingForm.getComments(), answers,
					addMeetingForm.getDiagnosticTestIds(), addMeetingForm.getFiles(), recipes);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/patient/update-patient/" + id;
	}
}

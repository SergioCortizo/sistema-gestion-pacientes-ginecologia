package es.udc.fic.ginecologia.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class MeetingForm {

	private String activity;

	private String comments;

	private List<String> questions;

	private List<String> answers;

	private List<Integer> diagnosticTestIds;

	private List<MultipartFile> files;
	
	private List<RecipeItem> recipes;

	public MeetingForm() {

	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<String> getQuestions() {
		return questions;
	}

	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public List<Integer> getDiagnosticTestIds() {
		return diagnosticTestIds;
	}

	public void setDiagnosticTestIds(List<Integer> diagnosticTestIds) {
		this.diagnosticTestIds = diagnosticTestIds;
	}

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public List<RecipeItem> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<RecipeItem> recipes) {
		this.recipes = recipes;
	}

	@Override
	public String toString() {
		return "MeetingForm [activity=" + activity + ", comments=" + comments + ", questions=" + questions
				+ ", answers=" + answers + ", diagnosticTestIds=" + diagnosticTestIds + ", files=" + files
				+ ", recipes=" + recipes + "]";
	}

}

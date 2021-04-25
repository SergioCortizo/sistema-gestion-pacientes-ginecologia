package es.udc.fic.ginecologia.common;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.udc.fic.ginecologia.form.CalendarEntryForm;
import es.udc.fic.ginecologia.form.CommonTaskForm;
import es.udc.fic.ginecologia.form.ContraceptiveForm;
import es.udc.fic.ginecologia.form.DiagnosticTestForm;
import es.udc.fic.ginecologia.form.MedicineForm;
import es.udc.fic.ginecologia.form.MeetingForm;
import es.udc.fic.ginecologia.form.MessageForm;
import es.udc.fic.ginecologia.form.NoticeForm;
import es.udc.fic.ginecologia.form.PatientForm;
import es.udc.fic.ginecologia.form.SearchPatientsForm;
import es.udc.fic.ginecologia.form.SettingsForm;
import es.udc.fic.ginecologia.form.SignUpForm;
import es.udc.fic.ginecologia.form.SpecialitiesToAddForm;
import es.udc.fic.ginecologia.form.SpecialityForm;
import es.udc.fic.ginecologia.form.SpecialityLine;
import es.udc.fic.ginecologia.form.UpdateForm;
import es.udc.fic.ginecologia.form.UserListElem;
import es.udc.fic.ginecologia.form.UserSearchForm;
import es.udc.fic.ginecologia.model.Answer;
import es.udc.fic.ginecologia.model.Contraceptive;
import es.udc.fic.ginecologia.model.DiagnosticTest;
import es.udc.fic.ginecologia.model.Medicine;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.Schedule;

public class LoggingUtility {

	private static final Logger logger = LogManager.getLogger(LoggingUtility.class);

	public static void logGetResource(String username, String method, String request) {
		logger.info("User with username=" + username + " access to endpoint=" + request + " with method=" + method);
	}

	public static void logDeniedAccess(String username, String method, String request) {
		logger.error("Denied access to user with username=" + username + " to request=" + request + " with method="
				+ method);
	}

	public static void logDuplicateUser(String username, SignUpForm signUpForm) {
		logger.error("Denied access to user with username=" + username + " with the following data="
				+ signUpForm.toString());
	}

	public static void logInstanceNotFound(String username, String className, Object object, String method,
			String request) {
		logger.error("Instance of class " + className + " not found with attribute " + object + " in request " + request
				+ " with method " + method + " by user with username=" + username);
	}

	public static void logInstanceNotFound(String username, String method, String request) {
		logger.error("Instance not found in request " + request + " with method " + method + " by user with username="
				+ username);
	}

	public static void logRegisteredUser(String username, SignUpForm signUpForm) {
		logger.info("User registered by user with username=" + username + " with the following data="
				+ signUpForm.toString());
	}

	public static void logUpdatedUser(String username, Integer id, UpdateForm updateForm) {
		logger.info("User with ID=" + id + " updated by user with username=" + username + " with the following data="
				+ updateForm.toString());
	}

	public static void logUpdatedUserOwnData(String username, UpdateForm updateForm) {
		logger.info("User with username=" + username + " updated own data with the following data="
				+ updateForm.toString());
	}

	public static void logWrongPasword(String username, String password, String method, String request) {
		logger.warn("User with username=" + username + " wrote wrong password=" + password + " in request=" + request
				+ " with method=" + method);
	}

	public static void logChangePassword(String username) {
		logger.info("User with username=" + username + " changed password");
	}

	public static void logWrongPasword(String username, Integer id, String password, String method, String request) {
		logger.warn("User with username=" + username + " wrote wrong password=" + password + " for user with ID=" + id
				+ " in request=" + request + " with method=" + method);
	}

	public static void logChangePassword(String username, Integer id) {
		logger.info("User with username=" + username + " changed password to user with ID=" + id);
	}

	public static void logChangeEnablingState(String username, String className, Integer id) {
		logger.info(
				"User with username=" + username + " has changed enabling state to " + className + " with ID=" + id);

	}

	public static void logSearchUsers(String username, UserSearchForm userSearchForm, Iterable<UserListElem> userList) {
		String result = "User with username=" + username + " made the following search=" + userSearchForm.toString()
				+ ", obtaining the following results: ";

		for (UserListElem userListElem : userList) {
			result = result + "\n" + userListElem.toString();
		}

		logger.info(result);

	}

	public static void logChangeSchedule(String username, Integer id, Set<Schedule> schedules) {
		String result = "User with username=" + username + " changed schedule from user with ID=" + id
				+ ", saving the following schedule:";

		for (Schedule schedule : schedules) {
			result = result + "\n" + schedule.toString();
		}

		logger.info(result);

	}

	public static void updatedCalendarEntry(String username, Integer id, CalendarEntryForm updateCalendarEntryForm) {
		logger.info("User with username=" + username + " updated calendar entry with ID=" + id
				+ " with the following values: " + updateCalendarEntryForm.toString());
	}

	public static void logAddedCalendarEntry(String username, CalendarEntryForm addCalendarEntryForm) {
		logger.info("User with username=" + username + " added calendar entry with the following values: "
				+ addCalendarEntryForm.toString());
	}

	public static void logUpdatedCalendarEntry(String username, Integer id, CalendarEntryForm updateCalendarEntryForm) {
		logger.info("User with username=" + username + " updated calendar entry with ID=" + id
				+ " with the following values: " + updateCalendarEntryForm.toString());
	}

	public static void logCancelledMeeting(String username, Integer id) {
		logger.info("User with username=" + username + " cancelled calendar entry with ID=" + id);
	}

	public static void logSetEntryAsClosed(String username, Integer id) {
		logger.info("User with username=" + username + " set calendar entry as closed with ID=" + id);

	}

	public static void logDownloadFile(String username, String fileName) {
		logger.info("User with username=" + username + " downloads file with name: " + fileName);
	}

	public static void logDuplicateContraceptive(String username, String name) {
		logger.info("User with username=" + username + " tried to duplicate contraceptive with name: " + name);

	}

	public static void logAddContraceptive(String username, ContraceptiveForm addContraceptiveForm) {
		logger.info("User with username=" + username + " created contraceptive with the folowing values: "
				+ addContraceptiveForm.toString());
	}

	public static void logUpdateContraceptive(String username, Integer id, ContraceptiveForm updateContraceptiveForm) {
		logger.info("User with username=" + username + " updated contraceptive with ID=" + id
				+ "with the folowing values: " + updateContraceptiveForm.toString());

	}

	public static void logChangeContraceptiveState(String username, Integer id) {
		logger.info("User with username=" + username + " changed contraceptive state with ID=" + id);
	}

	public static void logSearchContraceptives(String username, ContraceptiveForm searchContraceptivesForm,
			Iterable<Contraceptive> contraceptives) {
		String result = "User with username=" + username + " made the following search="
				+ searchContraceptivesForm.toString() + ", obtaining the following results: ";

		for (Contraceptive contraceptive : contraceptives) {
			result = result + "\n" + contraceptive.toString();
		}

		logger.info(result);
	}

	public static void logDuplicateDiagnosticTest(String username, DiagnosticTestForm addDiagnosticTestForm) {
		logger.info("User with username=" + username + " tried to duplicate diagnostic test with name: "
				+ addDiagnosticTestForm.getName());

	}

	public static void logAddedDiagnosticTest(String username, DiagnosticTestForm addDiagnosticTestForm) {
		logger.info("User with username=" + username + " created diagnostic test with the folowing values: "
				+ addDiagnosticTestForm.toString());
	}

	public static void logUpdatedDiagnosticTest(String username, Integer id,
			DiagnosticTestForm updateDiagnosticTestForm) {
		logger.info("User with username=" + username + " updated diagnostic test with ID=" + id
				+ " with the folowing values: " + updateDiagnosticTestForm.toString());
	}

	public static void logSearchDiagnosticTests(String username, DiagnosticTestForm searchDiagnosticTestsForm,
			Iterable<DiagnosticTest> diagnosticTests) {
		String result = "User with username=" + username + " made the following search="
				+ searchDiagnosticTestsForm.toString() + ", obtaining the following results: ";

		for (DiagnosticTest diagnosticTest : diagnosticTests) {
			result = result + "\n" + diagnosticTest.toString();
		}

		logger.info(result);
	}

	public static void logDuplicateMedicine(String username, MedicineForm addMedicineForm) {
		logger.info("User with username=" + username + " tried to duplicate medicine with name: "
				+ addMedicineForm.getName());
	}

	public static void logAddMedicine(String username, MedicineForm addMedicineForm) {
		logger.info("User with username=" + username + " created medicine with the folowing values: "
				+ addMedicineForm.toString());
	}

	public static void logSearchMedicines(String username, MedicineForm searchMedicinesForm,
			Iterable<Medicine> medicines) {
		String result = "User with username=" + username + " made the following search="
				+ searchMedicinesForm.toString() + ", obtaining the following results: ";

		for (Medicine medicine : medicines) {
			result = result + "\n" + medicine.toString();
		}

		logger.info(result);
	}

	public static void logChangeMedicineState(String username, Integer id) {
		logger.info("User with username=" + username + " changed medicine state with ID=" + id);
	}

	public static void logUpdatedMedicine(String username, Integer id, MedicineForm updateMedicineForm) {
		logger.info("User with username=" + username + " updated medicine with ID=" + id + " with the folowing values: "
				+ updateMedicineForm.toString());
	}

	public static void logAddMeeting(String username, Long id, MeetingForm addMeetingForm, List<Answer> answers) {
		String result = "User with username=" + username + " added a meeting with patient with ID=" + id
				+ " with the following information: " + addMeetingForm.toString();

		if (answers != null) {
			result = result + " and the following questions: ";
			for (Answer answer : answers) {
				result = result + "\n" + answer.toString();
			}
		}
	}

	public static void downloadedRecipe(String username, Integer id) {
		logger.info("User with username=" + username + " downloaded recipe with ID=" + id);
	}

	public static void logDownloadMonitoringReport(String username, Long id) {
		logger.info("User with username=" + username + " downloaded monitoring report from patient with ID=" + id);
	}

	public static void logErrorChangeNameAndLogo(String username, SettingsForm nameAndLogoForm) {
		logger.error("Something went wrong while user with username=" + username
				+ " tried to set the following settings for name and logo: " + nameAndLogoForm.toString());

	}

	public static void logChangedNameAndLogo(String username, SettingsForm nameAndLogoForm) {
		logger.info("User with username=" + username + " set the following settings for name and logo: "
				+ nameAndLogoForm.toString());
	}

	public static void logDuplicateSpeciality(String username, SpecialityForm addSpecialityForm) {
		logger.error("User with username=" + username + " tried to duplicate speciality with name: "
				+ addSpecialityForm.getName());
	}

	public static void logAddedSpeciality(String username, SpecialityForm addSpecialityForm) {
		logger.info("User with username=" + username + " created speciality with the folowing values: "
				+ addSpecialityForm.toString());
	}

	public static void logSearchSpecialities(String username, SpecialityForm searchSpecialitiesForm,
			List<SpecialityLine> specialities) {
		String result = "User with username=" + username + " made the following search="
				+ searchSpecialitiesForm.toString() + ", obtaining the following results: ";

		for (SpecialityLine speciality : specialities) {
			result = result + "\n" + speciality.toString();
		}

		logger.info(result);
	}

	public static void logUpdateSpeciality(String username, Integer id, SpecialityForm updateSpecialityForm) {
		logger.info("User with username=" + username + " updated speciality with ID=" + id
				+ " with the folowing values: " + updateSpecialityForm.toString());
	}

	public static void logChangedSpecialities(String username, Integer id,
			SpecialitiesToAddForm specialitiesToAddForm) {
		logger.info("User with username=" + username + " changed specialities to user with ID=" + id
				+ " with the folowing values: " + specialitiesToAddForm.toString());
	}

	public static void logSearchPatients(String username, SearchPatientsForm searchPatientsForm,
			Iterable<Patient> patients) {
		String result = "User with username=" + username + " made the following search=" + searchPatientsForm.toString()
				+ ", obtaining the following results: ";

		for (Patient patient : patients) {
			result = result + "\n" + patient.toString();
		}

		logger.info(result);
	}

	public static void logDuplicatePatient(String username, PatientForm patientForm) {
		logger.error("User with username=" + username + " tried to add a duplicated patient with DNI="
				+ patientForm.getDNI_NIF() + " and number of SERGAS=" + patientForm.getHist_numsergas());
	}

	public static void logAddedPatient(String username, PatientForm addPatientForm) {
		logger.info("User with username=" + username + " added patient with the following values: "
				+ addPatientForm.toString());

	}

	public static void logUpdatePatient(String username, Long id, PatientForm updatePatientForm) {
		logger.info("User with username=" + username + " updated patient with ID=" + id + " with the following values: "
				+ updatePatientForm.toString());
	}

	public static void logChangeEnablingPatientState(String username, String className, Long id) {
		logger.info(
				"User with username=" + username + " has changed enabling state to " + className + " with ID=" + id);
	}

	public static void logChangePatientOfInterest(String username, Long id) {
		logger.info("User with username=" + username + " has changed patient of interest to patient with ID=" + id);
	}

	public static void logAddMessage(String username, MessageForm messageForm) {
		logger.info("User with username=" + username + " has added a message with the following information: "
				+ messageForm.toString());

	}

	public static void logReplyMessage(String username, Integer id, MessageForm messageForm) {
		logger.info("User with username=" + username + " has replied the message with ID=" + id
				+ " with the following information: " + messageForm.toString());
	}

	public static void logOpenInterconsultation(String username, Integer id, MessageForm messageForm) {
		logger.info("User with username=" + username + " has opened an interconsultation from the meeting with ID=" + id
				+ " with the following information: " + messageForm.toString());
	}

	public static void logAddCommonTask(String username, CommonTaskForm commonTaskForm) {
		logger.info("User with username=" + username + " has opened a common task with the following information: "
				+ commonTaskForm.toString());
	}

	public static void logAddGrupalMessage(String username, Integer id, MessageForm addMessageForm) {
		logger.info("User with username=" + username + " has sent a grupal message in common task with ID=" + id
				+ " with the following message: " + addMessageForm.getMessage_body());

	}

	public static void logAddNotice(String username, NoticeForm noticeForm) {
		logger.info("User with username=" + username + " has sent a notice with the following message: "
				+ noticeForm.getNotice());
	}

	public static void logDownloadAccessReport(String username) {
		logger.info("User with username=" + username + " has generated an access report. ");
	}

}

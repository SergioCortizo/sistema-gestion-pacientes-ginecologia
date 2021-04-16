package es.udc.fic.ginecologia.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import es.udc.fic.ginecologia.common.exception.FileStorageException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Answer;
import es.udc.fic.ginecologia.model.ComplementaryTest;
import es.udc.fic.ginecologia.model.DiagnosticTest;
import es.udc.fic.ginecologia.model.Medicine;
import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.Question;
import es.udc.fic.ginecologia.model.Recipe;
import es.udc.fic.ginecologia.model.RecipeMedicinePK;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.AnswerDao;
import es.udc.fic.ginecologia.repository.ComplementaryTestDao;
import es.udc.fic.ginecologia.repository.DiagnosticTestDao;
import es.udc.fic.ginecologia.repository.MedicineDao;
import es.udc.fic.ginecologia.repository.MeetingDao;
import es.udc.fic.ginecologia.repository.PatientDao;
import es.udc.fic.ginecologia.repository.QuestionDao;
import es.udc.fic.ginecologia.repository.RecipeDao;
import es.udc.fic.ginecologia.repository.RecipeMedicineDao;

@Transactional
@Service
public class MeetingServiceImpl implements MeetingService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private PatientDao patientDao;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private AnswerDao answerDao;

	@Autowired
	private DiagnosticTestDao diagnosticTestDao;

	@Autowired
	private ComplementaryTestDao complementaryTestDao;

	@Autowired
	private RecipeDao recipeDao;

	@Autowired
	private RecipeMedicineDao recipeMedicineDao;

	@Autowired
	private MedicineDao medicineDao;

	@Override
	public void addMeeting(Integer userId, Long patientId, String activity, String comments, List<Answer> answers,
			List<Integer> diagnosticTestIds, List<MultipartFile> files, Set<Recipe> recipes)
			throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsFacultative(userId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(userId);

		Optional<Patient> patientFound = patientDao.findById(patientId);

		if (!patientFound.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		Meeting meeting = new Meeting(activity, comments);

		meeting.setUser(user);
		meeting.setPatient(patientFound.get());

		meetingDao.save(meeting);

		Set<Answer> setAnswers = new HashSet<>();

		if (answers != null) {
			answers.forEach(a -> {
				Optional<Question> questionFound = questionDao.findByQuestion(a.getQuestion().getQuestion());

				if (!questionFound.isPresent()) {
					questionDao.save(a.getQuestion());
					questionFound = questionDao.findByQuestion(a.getQuestion().getQuestion());
				}

				Question question = questionFound.get();

				a.setQuestion(question);
				a.setMeeting(meeting);

				answerDao.save(a);

				setAnswers.add(a);

			});
		}

		meeting.setAnswers(setAnswers);

		Set<ComplementaryTest> complementaryTests = new HashSet<>();

		if (diagnosticTestIds != null) {
			for (int i = 0; i < diagnosticTestIds.size(); i++) {

				DiagnosticTest diagnosticTest = diagnosticTestDao.findById(diagnosticTestIds.get(i)).get();

				MultipartFile file = files.get(i);

				String fileName = StringUtils.cleanPath(file.getOriginalFilename());

				try {

					if (fileName.contains("..")) {
						throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
					}

					ComplementaryTest complementaryTest = new ComplementaryTest();

					complementaryTest.setFileName(fileName);
					complementaryTest.setFileType(file.getContentType());
					complementaryTest.setData(file.getBytes());
					complementaryTest.setMeeting(meeting);
					complementaryTest.setDiagnosticTest(diagnosticTest);

					complementaryTestDao.save(complementaryTest);
					complementaryTests.add(complementaryTest);

				} catch (IOException ex) {
					throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
				}

			}
		}

		meeting.setComplementaryTests(complementaryTests);

		if (recipes != null) {

			recipes.forEach(r -> {
				r.setMeeting(meeting);

				recipeDao.save(r);

				r.getMedicines().forEach(rm -> {
					Medicine medicine = medicineDao.findById(rm.getMedicine().getId()).get();

					rm.setMedicine(medicine);

					RecipeMedicinePK pk = new RecipeMedicinePK(r.getId(), medicine.getId());
					rm.setPk(pk);

					recipeMedicineDao.save(rm);

				});

			});

		}

	}

}

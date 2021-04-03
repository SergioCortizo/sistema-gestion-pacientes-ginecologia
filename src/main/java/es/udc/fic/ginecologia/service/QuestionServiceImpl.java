package es.udc.fic.ginecologia.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Question;
import es.udc.fic.ginecologia.repository.QuestionDao;

@Transactional
@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private QuestionDao questionDao;

	@Override
	public Iterable<Question> findAllQuestions(Integer userId) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId)) {
			throw new PermissionException();
		}

		return questionDao.findAll();
	}
}

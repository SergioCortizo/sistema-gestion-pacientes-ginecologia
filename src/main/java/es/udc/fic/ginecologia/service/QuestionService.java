package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Question;

public interface QuestionService {

	Iterable<Question> findAllQuestions(Integer userId) throws InstanceNotFoundException, PermissionException;

}

package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Question;

public interface QuestionDao extends CrudRepository<Question, Integer> {

	@Query("SELECT q FROM Question q WHERE LOWER(q.question) = LOWER(?1)")
	Optional<Question> findByQuestion(String question);
}

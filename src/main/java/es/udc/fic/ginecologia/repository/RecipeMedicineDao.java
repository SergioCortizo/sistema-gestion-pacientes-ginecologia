package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.RecipeMedicine;

public interface RecipeMedicineDao extends CrudRepository<RecipeMedicine, Integer> {

}

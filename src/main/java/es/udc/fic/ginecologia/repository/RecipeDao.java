package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Recipe;

public interface RecipeDao extends CrudRepository<Recipe, Integer> {

}

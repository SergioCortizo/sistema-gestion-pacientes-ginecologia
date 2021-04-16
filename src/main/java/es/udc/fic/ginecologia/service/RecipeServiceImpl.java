package es.udc.fic.ginecologia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Recipe;
import es.udc.fic.ginecologia.repository.RecipeDao;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private RecipeDao recipeDao;

	@Override
	public Recipe findById(Integer userId, Integer id) throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsFacultative(userId)) {
			throw new PermissionException();
		}

		Optional<Recipe> recipe = recipeDao.findById(id);

		if (!recipe.isPresent()) {
			throw new InstanceNotFoundException("entities.recipe", id);
		}

		return recipe.get();

	}

}

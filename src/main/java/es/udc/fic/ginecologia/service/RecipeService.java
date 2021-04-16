package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Recipe;

public interface RecipeService {

	Recipe findById(Integer userId, Integer id) throws InstanceNotFoundException, PermissionException;

	
}

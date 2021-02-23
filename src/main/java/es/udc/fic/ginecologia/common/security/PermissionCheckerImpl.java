package es.udc.fic.ginecologia.common.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.UserDao;

@Service
@Transactional(readOnly=true)
public class PermissionCheckerImpl implements PermissionChecker {

	@Autowired
	private UserDao userDao;
	
	@Override
	public void checkUserExists(Long userId) throws InstanceNotFoundException {
		if (!userDao.existsById(userId)) {
			throw new InstanceNotFoundException("project.model.user", userId);
		}

	}

	@Override
	public User checkUser(Long userId) throws InstanceNotFoundException {
		Optional<User> user = userDao.findById(userId);
		
		if (!user.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		
		return user.get();

	}

}

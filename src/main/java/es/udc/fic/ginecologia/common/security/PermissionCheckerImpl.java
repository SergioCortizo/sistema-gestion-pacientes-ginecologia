package es.udc.fic.ginecologia.common.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.UserDao;

@Service
@Transactional(readOnly=true)
public class PermissionCheckerImpl implements PermissionChecker {

	@Autowired
	private UserDao userDao;
	
	@Override
	public void checkUserExists(Integer userId) throws InstanceNotFoundException {
		if (!userDao.existsById(userId)) {
			throw new InstanceNotFoundException("entities.user", userId);
		}

	}

	@Override
	public User checkUser(Integer userId) throws InstanceNotFoundException {
		Optional<User> user = userDao.findById(userId);
		
		if (!user.isPresent()) {
			throw new InstanceNotFoundException("entities.user", userId);
		}
		
		return user.get();

	}
	
	@Override
	public boolean checkIsAdmin(Integer userId) throws InstanceNotFoundException {
		Optional<User> user = userDao.findById(userId);
		
		if (!user.isPresent()) {
			throw new InstanceNotFoundException("entities.user", userId);
		}
		
		for (Role role : user.get().getRoles()) {
			if (role.getName().equals("ROLE_ADMIN")) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public boolean checkIsFacultative(Integer userId) throws InstanceNotFoundException {
		Optional<User> user = userDao.findById(userId);
		
		if (!user.isPresent()) {
			throw new InstanceNotFoundException("entities.user", userId);
		}
		
		for (Role role : user.get().getRoles()) {
			if (role.getName().equals("ROLE_FACULTATIVE")) {
				return true;
			}
		}

		return false;
	}

}

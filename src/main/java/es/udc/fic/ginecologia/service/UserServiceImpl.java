package es.udc.fic.ginecologia.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.RoleDao;
import es.udc.fic.ginecologia.repository.UserDao;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired 
	private UserDao userRepo;
	
	@Autowired 
	private RoleDao roleRepo;
	
	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private BCryptPasswordEncoder encrypter;
	
	@Override
	public Iterable<User> findAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.getUserByUsername(username);
		
		if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
		
		CustomUserDetails result = new CustomUserDetails(user);
         
        return result;
	}

	@Override
	public void registerUser(User user, Iterable<Integer> roles) throws DuplicateInstanceException {
		
		if (userRepo.existsByUsername(user.getUsername())) {
			throw new DuplicateInstanceException("entities.user", user.getUsername());
		}
		
		Set<Role> foundRoles = new HashSet<Role>();
		roleRepo.findAllById(roles).forEach(foundRoles::add);
		
		user.setPassword(encrypter.encode(user.getPassword()));
		user.setRoles(foundRoles);
		
		userRepo.save(user);
	}

	@Override
	public Iterable<Role> findAllRoles() {
		return roleRepo.findAll();
	}

}

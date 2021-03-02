package es.udc.fic.ginecologia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.UserDao;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired 
	private UserDao userRepo;
	
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
	public void registerUser(User user) {
		
		
		
	}

}

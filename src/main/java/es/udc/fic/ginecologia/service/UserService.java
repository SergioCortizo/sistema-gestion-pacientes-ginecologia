package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.model.User;

public interface UserService {
	Iterable<User> findAllUsers();
	
	void registerUser(User user);

}

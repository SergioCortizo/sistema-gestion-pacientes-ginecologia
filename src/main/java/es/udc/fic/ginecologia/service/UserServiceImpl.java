package es.udc.fic.ginecologia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.UserDao;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired 
	UserDao userRepo;
	
	@Override
	public List<User> findAllUsers() {
		return userRepo.findAll();
	}

}

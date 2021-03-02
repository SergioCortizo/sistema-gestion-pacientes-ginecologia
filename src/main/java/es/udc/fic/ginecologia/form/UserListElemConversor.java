package es.udc.fic.ginecologia.form;

import java.util.ArrayList;
import java.util.List;

import es.udc.fic.ginecologia.model.User;

public class UserListElemConversor {
	public static List<UserListElem> generateUserList(Iterable<User> users) {
		List<UserListElem> result = new ArrayList<>(); 
		
		for (User user : users) {
			UserListElem elem = new UserListElem(user);
			result.add(elem);
		}
		
		return result;
	}
}

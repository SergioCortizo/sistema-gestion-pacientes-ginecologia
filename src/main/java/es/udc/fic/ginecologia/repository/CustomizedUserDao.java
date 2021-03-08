package es.udc.fic.ginecologia.repository;

import java.time.LocalDateTime;

import es.udc.fic.ginecologia.model.User;

public interface CustomizedUserDao {
	Iterable<User> findUsers(String login, String name, String email, LocalDateTime firstDischargeDate,
			LocalDateTime lastDischargeDate, boolean enabled, Integer roleId);
}

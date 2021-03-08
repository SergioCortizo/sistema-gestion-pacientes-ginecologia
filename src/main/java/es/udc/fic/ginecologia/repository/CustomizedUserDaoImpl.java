package es.udc.fic.ginecologia.repository;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.udc.fic.ginecologia.model.User;

public class CustomizedUserDaoImpl implements CustomizedUserDao {

	@PersistenceContext
	private EntityManager entityManager;

	private String[] getTokens(String keywords) {

		if (keywords == null || keywords.length() == 0) {
			return new String[0];
		} else {
			return keywords.split("\\s");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<User> findUsers(String login, String name, String email, LocalDateTime firstDischargeDate,
			LocalDateTime lastDischargeDate, boolean enabled, Integer roleId) {
		String[] tokensLogin = getTokens(login);
		String[] tokensName = getTokens(name);
		String[] tokensEmail = getTokens(email);
		String queryString = "SELECT u FROM User u";

		if (roleId != null) {
			queryString += " INNER JOIN u.roles r ON r.id=:roleId";
		}

		if (tokensLogin.length != 0) {
			queryString += " WHERE ";

			for (int i = 0; i < tokensLogin.length - 1; i++) {
				queryString += "LOWER(u.username) LIKE LOWER(:tokenLogin" + i + ") OR ";
			}

			queryString += "LOWER(u.username) LIKE LOWER(:tokenLogin" + (tokensLogin.length - 1) + ")";
		}

		if (tokensName.length != 0) {
			if (tokensLogin.length != 0) {
				queryString += " AND ";
			} else {
				queryString += " WHERE ";
			}

			queryString += "(";

			for (int i = 0; i < tokensName.length - 1; i++) {
				queryString += "LOWER(u.name) LIKE LOWER(:tokenName" + i + ") OR ";
			}

			queryString += "LOWER(u.name) LIKE LOWER(:tokenName" + (tokensName.length - 1) + ")";

			queryString += ")";
		}

		if (tokensEmail.length != 0) {
			if (tokensLogin.length != 0 || tokensName.length != 0) {
				queryString += " AND ";
			} else {
				queryString += " WHERE ";
			}

			queryString += "(";

			for (int i = 0; i < tokensEmail.length - 1; i++) {
				queryString += "LOWER(u.email) LIKE LOWER(:tokenEmail" + i + ") OR ";
			}

			queryString += "LOWER(u.email) LIKE LOWER(:tokenEmail" + (tokensEmail.length - 1) + ")";

			queryString += ")";
		}

		if (firstDischargeDate != null && lastDischargeDate != null) {
			if (tokensLogin.length != 0 || tokensName.length != 0 || tokensEmail.length != 0) {
				queryString += " AND ";
			} else {
				queryString += " WHERE ";
			}

			queryString += "u.discharge_date BETWEEN :firstDischargeDate AND :lastDischargeDate";
		}

		if (tokensLogin.length != 0 || tokensName.length != 0 || tokensEmail.length != 0
				|| (firstDischargeDate != null && lastDischargeDate != null)) {
			queryString += enabled ? " AND u.enabled=1" : "";
		} else {
			queryString += enabled ? " WHERE u.enabled=1" : "";
		}

		Query query = entityManager.createQuery(queryString);

		if (tokensLogin.length != 0) {
			for (int i = 0; i < tokensLogin.length; i++) {
				query.setParameter("tokenLogin" + i, '%' + tokensLogin[i] + '%');
			}
		}

		if (tokensName.length != 0) {
			for (int i = 0; i < tokensName.length; i++) {
				query.setParameter("tokenName" + i, '%' + tokensName[i] + '%');
			}
		}

		if (tokensEmail.length != 0) {
			for (int i = 0; i < tokensEmail.length; i++) {
				query.setParameter("tokenEmail" + i, '%' + tokensEmail[i] + '%');
			}
		}

		if (firstDischargeDate != null && lastDischargeDate != null) {
			query.setParameter("firstDischargeDate", firstDischargeDate);
			query.setParameter("lastDischargeDate", lastDischargeDate);

		}
		
		if (roleId != null) {
			query.setParameter("roleId", roleId);
		}

		Iterable<User> users = query.getResultList();
		return users;
	}

}

package es.udc.fic.ginecologia.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.udc.fic.ginecologia.model.Speciality;

public class CustomizedSpecialityDaoImpl implements CustomizedSpecialityDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	private String[] getTokens(String keywords) {

		if (keywords == null || keywords.length() == 0) {
			return new String[0];
		} else {
			return keywords.split("\\s");
		}

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Iterable<Speciality> findSpecialities (String name, boolean enabled) {
		String[] tokensName = getTokens(name);
		
		String queryString = "SELECT s FROM Speciality s";
		
		if (tokensName.length != 0) {
			queryString += " WHERE (";
			
			for (int i = 0; i < tokensName.length - 1; i++) {
				queryString += "LOWER(s.name) LIKE LOWER(:tokenName" + i + ") OR ";
			}
			
			queryString += "LOWER(s.name) LIKE LOWER(:tokenName" + (tokensName.length - 1) + ")";
			
			queryString += ")";
		}
		
		if (tokensName.length != 0) {
			queryString += enabled ? " AND s.enabled=1" : "";
		} else {
			queryString += enabled ? " WHERE s.enabled=1" : "";
		}
		
		Query query = entityManager.createQuery(queryString);
		
		if (tokensName.length != 0) {
			for (int i = 0; i < tokensName.length; i++) {
				query.setParameter("tokenName" + i, '%' + tokensName[i] + '%');
			}
		}
		
		Iterable<Speciality> specialities = query.getResultList();
		
		return specialities;
	}
}

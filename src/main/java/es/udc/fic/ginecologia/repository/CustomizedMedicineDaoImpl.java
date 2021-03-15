package es.udc.fic.ginecologia.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.udc.fic.ginecologia.model.Medicine;

public class CustomizedMedicineDaoImpl implements CustomizedMedicineDao {

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
	public Iterable<Medicine> findMedicines(String name, boolean enabled) {
		String[] tokensName = getTokens(name);

		String queryString = "SELECT m FROM Medicine m";

		if (tokensName.length != 0) {
			queryString += " WHERE (";

			for (int i = 0; i < tokensName.length - 1; i++) {
				queryString += "LOWER(m.name) LIKE LOWER(:tokenName" + i + ") OR ";
			}

			queryString += "LOWER(m.name) LIKE LOWER(:tokenName" + (tokensName.length - 1) + ")";

			queryString += ")";
		}

		if (tokensName.length != 0) {
			queryString += enabled ? " AND m.enabled=1" : "";
		} else {
			queryString += enabled ? " WHERE m.enabled=1" : "";
		}

		Query query = entityManager.createQuery(queryString);

		if (tokensName.length != 0) {
			for (int i = 0; i < tokensName.length; i++) {
				query.setParameter("tokenName" + i, '%' + tokensName[i] + '%');
			}
		}
		
		Iterable<Medicine> medicines = query.getResultList();

		return medicines;
	}

}

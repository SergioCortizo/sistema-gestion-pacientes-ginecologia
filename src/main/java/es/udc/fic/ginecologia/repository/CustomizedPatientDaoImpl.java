package es.udc.fic.ginecologia.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.udc.fic.ginecologia.model.Patient;

public class CustomizedPatientDaoImpl implements CustomizedPatientDao {

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
	public Iterable<Patient> findPatients(String name, String DNI_NIF, String histNumSERGAS, boolean enabled) {
		String[] tokensName = getTokens(name);
		String[] tokensDNI_NIF = getTokens(DNI_NIF);
		String[] tokensHistNumSERGAS = getTokens(histNumSERGAS);

		String queryString = "SELECT p FROM Patient p";

		if (tokensName.length != 0) {
			queryString += " WHERE (";

			for (int i = 0; i < tokensName.length - 1; i++) {
				queryString += "LOWER(p.name) LIKE LOWER(:tokenName" + i + ") OR ";
			}

			queryString += "LOWER(p.name) LIKE LOWER(:tokenName" + (tokensName.length - 1) + ")";

			queryString += ")";
		}

		if (tokensDNI_NIF.length != 0) {
			if (tokensName.length != 0) {
				queryString += " AND ";
			} else {
				queryString += " WHERE ";
			}

			queryString += "(";

			for (int i = 0; i < tokensDNI_NIF.length - 1; i++) {
				queryString += "LOWER(p.DNI_NIF) LIKE LOWER(:tokenDNI_NIF" + i + ") OR ";
			}

			queryString += "LOWER(p.DNI_NIF) LIKE LOWER(:tokenDNI_NIF" + (tokensDNI_NIF.length - 1) + ")";

			queryString += ")";
		}

		if (tokensHistNumSERGAS.length != 0) {
			if (tokensName.length != 0 || tokensDNI_NIF.length != 0) {
				queryString += " AND ";
			} else {
				queryString += " WHERE ";
			}

			queryString += "(";

			for (int i = 0; i < tokensHistNumSERGAS.length - 1; i++) {
				queryString += "LOWER(p.hist_numsergas) LIKE LOWER(:tokenHistNumSERGAS" + i + ") OR ";
			}

			queryString += "LOWER(p.hist_numsergas) LIKE LOWER(:tokenHistNumSERGAS" + (tokensHistNumSERGAS.length - 1)
					+ ")";

			queryString += ")";
		}
		
		if (tokensName.length != 0 || tokensDNI_NIF.length != 0 || tokensHistNumSERGAS.length != 0) {
			queryString += enabled ? " AND p.enabled=1" : "";
		} else {
			queryString += enabled ? " WHERE p.enabled=1" : "";
		}
		
		queryString += " ORDER BY p.id DESC";

		Query query = entityManager.createQuery(queryString);

		if (tokensName.length != 0) {
			for (int i = 0; i < tokensName.length; i++) {
				query.setParameter("tokenName" + i, '%' + tokensName[i] + '%');
			}
		}
		
		if (tokensDNI_NIF.length != 0) {
			for (int i = 0; i < tokensDNI_NIF.length; i++) {
				query.setParameter("tokenDNI_NIF" + i, '%' + tokensDNI_NIF[i] + '%');
			}
		}
		
		if (tokensHistNumSERGAS.length != 0) {
			for (int i = 0; i < tokensHistNumSERGAS.length; i++) {
				query.setParameter("tokenHistNumSERGAS" + i, '%' + tokensHistNumSERGAS[i] + '%');
			}
		}

		Iterable<Patient> patients = query.getResultList();

		return patients;
	}
}

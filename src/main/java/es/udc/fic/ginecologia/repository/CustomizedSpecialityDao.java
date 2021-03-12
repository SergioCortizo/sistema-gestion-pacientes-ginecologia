package es.udc.fic.ginecologia.repository;

import es.udc.fic.ginecologia.model.Speciality;

public interface CustomizedSpecialityDao {

	Iterable<Speciality> findSpecialities(String name, boolean enabled);

}

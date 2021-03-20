package es.udc.fic.ginecologia.repository;

import es.udc.fic.ginecologia.model.Contraceptive;

public interface CustomizedContraceptiveDao {

	Iterable<Contraceptive> findContraceptives(String name, boolean enabled);

}

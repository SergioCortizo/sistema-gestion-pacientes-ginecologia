package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.udc.fic.ginecologia.model.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Integer> {

}

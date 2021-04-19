package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.CommonTaskUser;
import es.udc.fic.ginecologia.model.CommonTaskUserPK;

public interface CommonTaskUserDao extends CrudRepository<CommonTaskUser, CommonTaskUserPK> {

}

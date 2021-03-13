package es.udc.fic.ginecologia.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Speciality;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.SpecialityDao;

@Transactional
@Service
public class SpecialityServiceImpl implements SpecialityService {

	@Autowired
	private SpecialityDao specialityDao;

	@Autowired
	private PermissionChecker permissionChecker;

	@Override
	public Iterable<Speciality> findAllSpecialities() {
		return specialityDao.findAll();
	}

	@Override
	public void addSpeciality(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		if (specialityDao.existsByName(name)) {
			throw new DuplicateInstanceException("entities.speciality", name);
		}

		Speciality speciality = new Speciality(name);

		specialityDao.save(speciality);
	}

	@Override
	public void updateSpeciality(Integer adminId, Integer specialityId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		Optional<Speciality> speciality = specialityDao.findById(specialityId);

		if (!speciality.isPresent()) {
			throw new InstanceNotFoundException("entities.speciality", specialityId);
		}

		if (specialityDao.existsByName(name)) {
			throw new DuplicateInstanceException("entities.speciality", name);
		}

		speciality.get().setName(name);
	}

	@Override
	public void changeEnablingSpeciality(Integer adminId, Integer specialityId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		Optional<Speciality> speciality = specialityDao.findById(specialityId);

		if (!speciality.isPresent()) {
			throw new InstanceNotFoundException("entities.speciality", specialityId);
		}

		speciality.get().setEnabled(!speciality.get().isEnabled());
	}

	@Override
	public Iterable<Speciality> findSpecialities(Integer adminId, String name, boolean enabled)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		return specialityDao.findSpecialities(name, enabled);

	}

	@Override
	public Iterable<Speciality> findSpecialitiesFromUser(Integer adminId, Integer userId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(userId);

		return user.getSpecialities();
	}

	@Override
	public void changeSpecialities(Integer adminId, Integer userId, Iterable<Speciality> specialities)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(userId);

		Set<Speciality> specialitiesToAdd = new HashSet<>();
		specialities.forEach(specialitiesToAdd::add);
		
		user.setSpecialities(specialitiesToAdd);
	}
}

package es.udc.fic.ginecologia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Contraceptive;
import es.udc.fic.ginecologia.repository.ContraceptiveDao;

@Transactional
@Service
public class ContraceptiveServiceImpl implements ContraceptiveService {
	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private ContraceptiveDao contraceptiveDao;
	
	@Override
	public Iterable<Contraceptive> findAllContraceptives() {
		return contraceptiveDao.findAll();
	}
	
	@Override
	public Iterable<Contraceptive> findAllActiveContraceptives() {
		return contraceptiveDao.findByEnabled(true);
	}
	
	@Override
	public void addContraceptive(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		if (contraceptiveDao.existsByName(name)) {
			throw new DuplicateInstanceException("entities.contraceptive", name);
		}
		
		Contraceptive medicine = new Contraceptive(name);
		
		contraceptiveDao.save(medicine);
		
	}
	
	@Override
	public void updateContraceptive(Integer adminId, Integer contraceptiveId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}
		
		Optional<Contraceptive> contraceptive = contraceptiveDao.findById(contraceptiveId);
		
		if (!contraceptive.isPresent()) {
			throw new InstanceNotFoundException("entities.contraceptive", contraceptive);
		}

		if (contraceptiveDao.existsByName(name)) {
			throw new DuplicateInstanceException("entities.contraceptive", name);
		}

		contraceptive.get().setName(name);
	}
	
	@Override
	public void changeEnablingContraceptive(Integer adminId, Integer contraceptiveId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		Optional<Contraceptive> contraceptive = contraceptiveDao.findById(contraceptiveId);

		if (!contraceptive.isPresent()) {
			throw new InstanceNotFoundException("entities.contraceptive", contraceptiveId);
		}

		contraceptive.get().setEnabled(!contraceptive.get().isEnabled());
	}
	
	@Override
	public Iterable<Contraceptive> findContraceptives(Integer adminId, String name, boolean enabled)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}
		
		return contraceptiveDao.findContraceptives(name, enabled);
	}
}

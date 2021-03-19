package es.udc.fic.ginecologia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.DiagnosticTest;
import es.udc.fic.ginecologia.repository.DiagnosticTestDao;

@Transactional
@Service
public class DiagnosticTestServiceImpl implements DiagnosticTestService {
	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private DiagnosticTestDao diagnosticTestDao;
	
	@Override
	public Iterable<DiagnosticTest> findAllDiagnosticTests() {
		return diagnosticTestDao.findAll();
	}
	
	@Override
	public void addDiagnosticTest(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		if (diagnosticTestDao.existsByName(name)) {
			throw new DuplicateInstanceException("entities.diagnosticTest", name);
		}
		
		DiagnosticTest medicine = new DiagnosticTest(name);
		
		diagnosticTestDao.save(medicine);
		
	}
	
	@Override
	public void updateDiagnosticTest(Integer adminId, Integer diagnosticTestId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}
		
		Optional<DiagnosticTest> diagnosticTest = diagnosticTestDao.findById(diagnosticTestId);
		
		if (!diagnosticTest.isPresent()) {
			throw new InstanceNotFoundException("entities.diagnosticTest", diagnosticTest);
		}

		if (diagnosticTestDao.existsByName(name)) {
			throw new DuplicateInstanceException("entities.diagnosticTest", name);
		}

		diagnosticTest.get().setName(name);
	}
	
	@Override
	public void changeEnablingDiagnosticTest(Integer adminId, Integer diagnosticTestId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		Optional<DiagnosticTest> diagnosticTest = diagnosticTestDao.findById(diagnosticTestId);

		if (!diagnosticTest.isPresent()) {
			throw new InstanceNotFoundException("entities.diagnosticTest", diagnosticTestId);
		}

		diagnosticTest.get().setEnabled(!diagnosticTest.get().isEnabled());
	}
	
	@Override
	public Iterable<DiagnosticTest> findDiagnosticTests(Integer adminId, String name, boolean enabled)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}
		
		return diagnosticTestDao.findDiagnosticTests(name, enabled);
	}
}

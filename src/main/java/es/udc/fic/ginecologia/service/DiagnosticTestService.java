package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.DiagnosticTest;

public interface DiagnosticTestService {

	Iterable<DiagnosticTest> findAllDiagnosticTests();

	void addDiagnosticTest(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void updateDiagnosticTest(Integer adminId, Integer diagnosticTestId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void changeEnablingDiagnosticTest(Integer adminId, Integer diagnosticTestId)
			throws InstanceNotFoundException, PermissionException;

	Iterable<DiagnosticTest> findDiagnosticTests(
			Integer adminId, String name, boolean enabled)
			throws InstanceNotFoundException, PermissionException;

}

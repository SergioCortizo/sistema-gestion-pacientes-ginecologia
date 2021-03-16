package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.DiagnosticTest;

public interface DiagnosticTestService {

	Iterable<DiagnosticTest> findAllDiagnosticTests();

	void addDiagnosticTest(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

}

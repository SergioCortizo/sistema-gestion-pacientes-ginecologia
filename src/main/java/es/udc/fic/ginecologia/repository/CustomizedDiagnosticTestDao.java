package es.udc.fic.ginecologia.repository;

import es.udc.fic.ginecologia.model.DiagnosticTest;

public interface CustomizedDiagnosticTestDao {

	Iterable<DiagnosticTest> findDiagnosticTests(String name, boolean enabled);
}

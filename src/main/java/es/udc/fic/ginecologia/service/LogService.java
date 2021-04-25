package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.LogLine;

public interface LogService {

	public Iterable<LogLine> findLogs(Integer userId) throws InstanceNotFoundException, PermissionException;
	
	
}

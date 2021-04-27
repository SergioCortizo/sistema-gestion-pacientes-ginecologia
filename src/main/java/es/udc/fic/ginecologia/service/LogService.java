package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.LogLevel;
import es.udc.fic.ginecologia.model.LogLine;

public interface LogService {

	public Iterable<LogLine> findLogs(Integer userId) throws InstanceNotFoundException, PermissionException;

	public Iterable<LogLine> searchLogs(Integer userId, LogLevel level, LocalDateTime date1, LocalDateTime date2)
			throws InstanceNotFoundException, PermissionException;
}

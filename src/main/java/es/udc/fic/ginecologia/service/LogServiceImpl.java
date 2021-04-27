package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.LogLevel;
import es.udc.fic.ginecologia.model.LogLine;
import es.udc.fic.ginecologia.repository.LogLineDao;

@Transactional
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private LogLineDao logLineDao;

	@Override
	public Iterable<LogLine> findLogs(Integer userId) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}
		
		return logLineDao.findAllByOrderByTimestampDesc();
	}

	@Override
	public Iterable<LogLine> searchLogs(Integer userId, LogLevel level, LocalDateTime date1, LocalDateTime date2)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}
		
		if (level == null) {
			return logLineDao.findByTimestampRangeOrderByTimestampDesc(date1, date2);
		}
		
		return logLineDao.findByLevelAndTimestampOrderByTimestampDesc(level, date1, date2);
	}

}

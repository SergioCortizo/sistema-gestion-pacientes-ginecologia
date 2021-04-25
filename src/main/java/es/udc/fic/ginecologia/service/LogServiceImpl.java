package es.udc.fic.ginecologia.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
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

}

package es.udc.fic.ginecologia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.FileNotFoundException;
import es.udc.fic.ginecologia.model.ComplementaryTest;
import es.udc.fic.ginecologia.repository.ComplementaryTestDao;

@Transactional
@Service
public class ComplementaryTestServiceImpl implements ComplementaryTestService {

	@Autowired
	private ComplementaryTestDao complementaryTestDao;
	
	@Override
	public ComplementaryTest getFile(String fileId) {
        return complementaryTestDao.findById(fileId)
            .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }
}

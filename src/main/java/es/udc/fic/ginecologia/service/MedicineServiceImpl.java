package es.udc.fic.ginecologia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Medicine;
import es.udc.fic.ginecologia.repository.MedicineDao;

@Transactional
@Service
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private MedicineDao medicineDao;
	
	@Override
	public Iterable<Medicine> findAllMedicines() {
		return medicineDao.findAll();
	}
	
	@Override
	public void addMedicine(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		if (medicineDao.existsByName(name)) {
			throw new DuplicateInstanceException("entities.medicine", name);
		}
		
		Medicine medicine = new Medicine(name);
		
		medicineDao.save(medicine);
		
	}
}

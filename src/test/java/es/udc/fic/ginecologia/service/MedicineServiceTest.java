package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Medicine;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.MedicineDao;
import es.udc.fic.ginecologia.repository.RoleDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MedicineServiceTest {
	@Autowired
	private UserService userService;

	@Autowired
	private MedicineService medicineService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private MedicineDao medicineDao;

	private User createUser(String name, String username, String email, String postalAddress, String location,
			String DNI, String phoneNumber, String collegiateNumber) {
		User user = new User(name, username, email, postalAddress, location, DNI, phoneNumber, collegiateNumber);
		user.setPassword(username);
		return user;
	}

	private Role createRole(String name) {
		return new Role(name);
	}

	@Test
	public void findAllMedicinesTest() {

		Medicine medicine1 = new Medicine("medicine1");
		Medicine medicine2 = new Medicine("medicine2");
		Medicine medicine3 = new Medicine("medicine3");
		Medicine medicine4 = new Medicine("medicine4");

		medicineDao.save(medicine1);
		medicineDao.save(medicine2);
		medicineDao.save(medicine3);
		medicineDao.save(medicine4);

		Iterable<Medicine> result = medicineService.findAllMedicines();
		Iterable<Medicine> expectedResult = Arrays.asList(medicine1, medicine2, medicine3, medicine4);

		assertEquals(expectedResult, result);
	}

	@Test
	public void addMedicineTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		medicineService.addMedicine(user.getId(), "newmedicine");

		Medicine medicine = medicineDao.findByName("newmedicine").get();

		assertEquals("newmedicine", medicine.getName());
		assertTrue(medicine.isEnabled());
	}

	@Test
	public void addMedicineTestExpectAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> medicineService.addMedicine(-1, "newmedicine"));
	}

	@Test
	public void addMedicineTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> medicineService.addMedicine(user.getId(), "newmedicine"));
	}

	@Test
	public void addMedicineTestExpectDuplicateInstanceException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		medicineService.addMedicine(user.getId(), "newmedicine");

		assertThrows(DuplicateInstanceException.class, () -> medicineService.addMedicine(user.getId(), "newmedicine"));
	}
}

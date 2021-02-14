package es.udc.fic.ginecologia.common.security;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EncrypterTest {
	
	private Encrypter encrypter = new Encrypter();

	@Test
	public void testEncrypter() {
		final String string = "PasswordToEncrypt";
		
		final String encryptedString = encrypter.encode(string);
		
		assertTrue(encrypter.matches(string, encryptedString));
	}
}

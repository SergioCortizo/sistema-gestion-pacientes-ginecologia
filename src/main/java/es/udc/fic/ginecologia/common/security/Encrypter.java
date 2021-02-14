package es.udc.fic.ginecologia.common.security;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encrypter {
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom());
	
	public String encode(String content) {
		String encoded = encoder.encode(content);
		
		return encoded;
	}
	
	public boolean matches(String content, String encoded) {
		return encoder.matches(content, encoded);
	}
}

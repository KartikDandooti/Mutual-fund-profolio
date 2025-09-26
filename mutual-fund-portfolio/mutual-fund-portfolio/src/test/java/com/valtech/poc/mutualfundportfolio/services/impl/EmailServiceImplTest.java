package com.valtech.poc.mutualfundportfolio.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.services.UserService;

@SpringBootTest
class EmailServiceImplTest {

	@Autowired
	private UserService userService;

	@Test
	void testEmail() throws Exception {
		User user = new User("Anisha", "sharma", 25, 9876543201L, "anisha1111@example.com", "Pas@2345");
		User createdUser = userService.createUser(user);
		String validEmail = createdUser.getEmail();
		createdUser.setEmail("");
		String nullEmail = createdUser.getEmail();
		createdUser.setEmail("qdjknhkqach");
		String notValidEmail = createdUser.getEmail();

		boolean valid = isValidEmail(validEmail);
		boolean notValid = isValidEmail(notValidEmail);
		boolean nullMail = isValidEmail(nullEmail);

		assertEquals(true, valid);
		assertEquals(false, notValid);
		assertEquals(false, nullMail);
	}

	public static boolean isValidEmail(String email) {
		final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

		final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

		return email != null && EMAIL_PATTERN.matcher(email).matches();
	}
}

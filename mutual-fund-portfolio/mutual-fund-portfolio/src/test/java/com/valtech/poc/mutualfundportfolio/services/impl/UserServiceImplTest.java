package com.valtech.poc.mutualfundportfolio.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.valtech.poc.mutualfundportfolio.custom.exceptions.DuplicateEmailException;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.repositories.UserRepository;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock 
	private EmailServiceImpl emailService;

	@InjectMocks
	UserServiceImpl userService;

	@Test
	void testCreateUser() throws MessagingException, IOException, TemplateException, DuplicateEmailException {

		User newUser = new User("kartik", "dandooti", 21, 8951236665L, "test@gamil.com", "KAR88112");
		when(userRepository.findByemail(newUser.getEmail())).thenReturn(null);
		when(userRepository.existsByportfolioNumber(anyString())).thenReturn(false);
		when(userRepository.save(newUser)).thenReturn(newUser);

		User result = userService.createUser(newUser);

		verify(userRepository).findByemail(newUser.getEmail());
		verify(userRepository).existsByportfolioNumber(anyString());
		verify(userRepository).save(newUser);
		assertNotNull(result);
		assertEquals(newUser, result);
	}

	@Test
	void testUpdateUser() {

		User user = new User("kartik", "dandooti", 21, 8951236665L, "test@gamil.com", "KAR88112");
		user.setFirstName("pradeep");
		user.setLastName("halimani");
		user.setAge(23);
		user.setPhoneNumber(9448001357L);
		when(userRepository.save(user)).thenReturn(user);

		User result = userService.updateUser(user);

		verify(userRepository).save(user);
		assertNotNull(result);
		assertEquals(user.getFirstName(), result.getFirstName());
		assertEquals(user.getLastName(), result.getLastName());
		assertEquals(user.getAge(), result.getAge());
		assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(user, result);
	}

	@Test
	void testFindByUserId() {

		User user = new User();
		user.setId(1);
		user.setFirstName("John");
		user.setLastName("Doe");
		when(userRepository.getReferenceById(1)).thenReturn(user);

		User result = userService.findUserById(1);

		verify(userRepository).getReferenceById(1);
		assertNotNull(result);
		assertEquals(user, result);
	}

	@Test
	void testChangePassword() {
 
		User newUser = new User("kartik", "dandooti", 21, 8951236665L, "test2@gamil.com", "KAR88112");
		String oldPassword = newUser.getPassword();
		String newPassowrd = "Ksdan@143";
		newUser.setPassword(newPassowrd);
		when(userRepository.save(newUser)).thenReturn(newUser);

		User updatedUser = userService.changePassword(newUser, newPassowrd);
		when(passwordEncoder.matches(newPassowrd, updatedUser.getPassword())).thenReturn(true);
		when(passwordEncoder.matches(oldPassword, updatedUser.getPassword())).thenReturn(false);

		verify(userRepository).save(newUser);
		assertEquals(false, passwordEncoder.matches(oldPassword, updatedUser.getPassword()));
		assertEquals(true, passwordEncoder.matches(newPassowrd, updatedUser.getPassword()));
		assertEquals(newUser, updatedUser);
	}
}
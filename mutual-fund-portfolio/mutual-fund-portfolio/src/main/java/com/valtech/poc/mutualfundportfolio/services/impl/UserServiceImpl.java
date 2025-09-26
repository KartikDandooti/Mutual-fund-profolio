package com.valtech.poc.mutualfundportfolio.services.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.poc.mutualfundportfolio.custom.exceptions.DuplicateEmailException;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.repositories.UserRepository;
import com.valtech.poc.mutualfundportfolio.services.EmailService;
import com.valtech.poc.mutualfundportfolio.services.UserService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;

	Random random = new Random();
	LocalDateTime date = LocalDateTime.now();

	@Override
	public User createUser(User newUser)
			throws MessagingException, IOException, TemplateException, DuplicateEmailException {
		User user = userRepository.findByemail(newUser.getEmail());
		if (user == null) {
			String portfolioNumber = generatePortfolioNumber(newUser.getFirstName());
			if (userRepository.existsByportfolioNumber(portfolioNumber)) {
				String newPortfolioNumber = generatePortfolioNumber(newUser.getFirstName());
				newUser.setPortfolioNumber(newPortfolioNumber);
			} else {
				newUser.setPortfolioNumber(portfolioNumber);
			}
			newUser.setEnabled(true);
			newUser.setRole("USER");
			newUser.setRegisteredDate(date);
			LOGGER.info("Creating A new User with UserName: {},Role: {} and Portfolio number: {}",
					newUser.getFirstName() + newUser.getLastName(), newUser.getRole(), newUser.getPortfolioNumber());

			User createdUser = userRepository.save(newUser);
			if (Objects.nonNull(createdUser)) {
				emailService.sendUserRegistrationMail(createdUser);
				LOGGER.info("Sending Registration mail to the user");
			}
			return createdUser;
		} else {
			LOGGER.warn("User with this eamil already exist");
			throw new DuplicateEmailException("User with email already exists");
		}
	}

	@Override
	public String generatePortfolioNumber(String name) {
		String initials = name.substring(0, 3);
		StringBuilder randomNumbers = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			randomNumbers.append(this.random.nextInt(10));
		}
		LOGGER.info("Generating a new Random PortFolio Number");

		return initials + randomNumbers.toString();
	}

	@Override
	public User updateUser(User user) {
		user.setModifiedDate(date);
		LOGGER.info("Updating Modified date: {} of User with UserId: {}, UserName: {}", date, user.getId(),
				user.getFirstName() + user.getLastName());

		return userRepository.save(user);
	}

	@Override
	public User findUserById(int id) {
		LOGGER.info("Finding user by Id: {}", id);

		return userRepository.getReferenceById(id);
	}

	@Override
	public User findUserByUsername(String username) {
		LOGGER.info("Finding user with name: {}", username);

		return userRepository.findByportfolioNumber(username);
	}

	@Override
	public User changePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));

		return userRepository.save(user);
	}

}
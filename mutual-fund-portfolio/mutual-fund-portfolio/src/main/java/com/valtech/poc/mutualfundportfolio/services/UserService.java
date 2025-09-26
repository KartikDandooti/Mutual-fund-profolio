package com.valtech.poc.mutualfundportfolio.services;

import java.io.IOException;

import com.valtech.poc.mutualfundportfolio.custom.exceptions.DuplicateEmailException;
import com.valtech.poc.mutualfundportfolio.entities.User;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

/*
 * Service interface for managing user-related operations, including user creation,
 * portfolio number generation, user details updates, and password validation.
 */
public interface UserService {

	/*
	 * Creating a new user and checking if user already exists by using email.
	 * Checking if the portfolio number is unique. Setting basic fields in the
	 * database
	 * 
	 * @param The user object that is being created
	 */
	User createUser(User user) throws MessagingException, IOException, TemplateException, DuplicateEmailException;

	/*
	 * Generating a portfolio number for a user based on the first the characters of
	 * their name and 5 randomly generated numbers
	 * 
	 * @param A string that contains the first name of the user
	 * 
	 * @return A string that is the portfolio number of the user
	 */
	String generatePortfolioNumber(String name);

	/*
	 * Updating user details and setting the modified date to reflect the last time
	 * when user details were changed
	 * 
	 * @param The user_id of the current user
	 * 
	 * @return User object with updated details
	 */
	User updateUser(User user);

	/*
	 * Finding the user by user_id
	 * 
	 * @param user_id of the user object to be found
	 * 
	 * @return The user object with the specific user_id
	 */
	User findUserById(int id);

	/*
	 * Finding a user by the portfolio number
	 * 
	 * @param A string that has the portfolio number of the user
	 * 
	 * @return The user associated with that porfolio number
	 */
	User findUserByUsername(String username);

	/*
	 * changing the user password.
	 * 
	 * @param An object of user and new password.
	 * 
	 * @return the user
	 */
	User changePassword(User user, String newPassword);
}
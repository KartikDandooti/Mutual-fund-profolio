package com.valtech.poc.mutualfundportfolio.services;

import java.io.IOException;

import com.valtech.poc.mutualfundportfolio.entities.Transaction;
import com.valtech.poc.mutualfundportfolio.entities.User;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

/*
 * A service interface for handling email-related operations, such as generating
 * email templates and sending emails to users.
 */

public interface EmailService {

	/*
	 * Getting user details & portfolio number and inserting into user registration
	 * email template.
	 * 
	 * @param The user object to be used
	 * 
	 * @return Registration email template with user details inserted
	 * 
	 * @throws IOException If an I/O error occurs while processing the email
	 * template.
	 * 
	 * @throws TemplateException If an error occurs during template processing.
	 */
	String getUserRegistrationDetails(User user) throws IOException, TemplateException;

	/*
	 * Getting user email and adding subject to email. Using
	 * getUserRegistrationDetails to get email content from freemarker template and
	 * adding to email and sending mail to user.
	 * 
	 * @param The user whose details are required * @throws MessagingException If an
	 * error occurs during the email sending process.
	 * 
	 * @throws IOException If an I/O error occurs while processing the email
	 * template.
	 * 
	 * @throws TemplateException If an error occurs during template processing.
	 */
	void sendUserRegistrationMail(User user) throws MessagingException, IOException, TemplateException;

	/*
	 * Getting user details and transaction details and inserting into transaction
	 * successful email template
	 * 
	 * @param The transaction object to be used
	 * 
	 * @return Transaction successful email template with user details inserted
	 * 
	 * @throws IOException If an I/O error occurs while processing the email
	 * template.
	 * 
	 * @throws TemplateException If an error occurs during template processing.
	 */
	String getUserTransactionDetails(Transaction transaction) throws IOException, TemplateException;

	/*
	 * Getting user email and adding subject to email. Using
	 * getUserRegistrationDetails to get email content from freemarker template and
	 * adding to email and sending mail to user.
	 * 
	 * @param The transaction details are required
	 * 
	 * @throws IOException If an I/O error occurs while processing the email
	 * template.
	 * 
	 * @throws TemplateException If an error occurs during template processing.
	 * 
	 * @throws MessagingException If an error occurs during the email sending
	 * process.
	 */
	void sendUserTransactionMail(Transaction transaction) throws IOException, TemplateException, MessagingException;
}
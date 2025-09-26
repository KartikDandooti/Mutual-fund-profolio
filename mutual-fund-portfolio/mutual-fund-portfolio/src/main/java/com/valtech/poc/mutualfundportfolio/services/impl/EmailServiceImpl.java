package com.valtech.poc.mutualfundportfolio.services.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.poc.mutualfundportfolio.entities.Transaction;
import com.valtech.poc.mutualfundportfolio.entities.TransactionType;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.services.EmailService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EmailServiceImpl implements EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Configuration configuration;

	@Async 
	@Override 
	public void sendUserRegistrationMail(User user) throws IOException, TemplateException, MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setSubject("Mutualfund-portfolio UserId");
		helper.setTo(user.getEmail());
		String emailContent = getUserRegistrationDetails(user);
		helper.setText(emailContent, true);
		javaMailSender.send(mimeMessage);
		LOGGER.info("Genarated Portfolio number is added to user: {} with email id: {}", user.getFirstName(), user.getEmail());
	}

	@Override
	public String getUserRegistrationDetails(User user) throws IOException, TemplateException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		configuration.getTemplate("registrationEmailTemplate.ftlh").process(model, stringWriter);
		LOGGER.info("Fetching email template to mail to user: {}", user.getFirstName());

		return stringWriter.getBuffer().toString();
	}

	@Async
	@Override
	public void sendUserTransactionMail(Transaction transaction) throws IOException, TemplateException, MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		if (transaction.getTransactionType() == TransactionType.INVEST) {
			helper.setSubject("Your Investment Transaction is successful");
		} else {
			helper.setSubject("Your Redeem Transaction is successful");
		}
		helper.setTo(transaction.getUser().getEmail());
		String emailContent = getUserTransactionDetails(transaction);
		helper.setText(emailContent, true);
		javaMailSender.send(mimeMessage);
		LOGGER.info("Sent investment email message to user: {} to eamil id: {}", transaction.getUser().getFirstName(), transaction.getUser().getEmail());
	}

	@Override
	public String getUserTransactionDetails(Transaction transaction) throws IOException, TemplateException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("transaction", transaction);
		configuration.getTemplate("transactionEmailTemplate.ftlh").process(model, stringWriter);
		LOGGER.info("Fetching Investment email template to mail to user: {}", transaction.getUser().getFirstName());

		return stringWriter.getBuffer().toString();
	}
}

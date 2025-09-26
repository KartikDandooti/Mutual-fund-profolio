package com.valtech.poc.mutualfundportfolio.services.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.poc.mutualfundportfolio.entities.Transaction;
import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.entities.UserSchemeId;
import com.valtech.poc.mutualfundportfolio.repositories.TransactionRepository;
import com.valtech.poc.mutualfundportfolio.repositories.UserSchemeRepository;
import com.valtech.poc.mutualfundportfolio.services.EmailService;
import com.valtech.poc.mutualfundportfolio.services.TransactionService;
import com.valtech.poc.mutualfundportfolio.services.UserSchemeService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TransactionServiceImpl implements TransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

	LocalDateTime dateTime = LocalDateTime.now();

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserSchemeService userSchemeService;
	@Autowired
	private UserSchemeRepository userSchemeRepository;
	@Autowired
	private EmailService emailService;

	@Override
	public Transaction createInvestTransaction(Transaction transaction) throws IOException, TemplateException, MessagingException {

		LOGGER.info("Creating Transaction with Net Asset Value: {}, Nav Units: {}", transaction.getNetAssetValue(), transaction.getNavUnits());
		Transaction createdTransaction = transactionRepository.save(transaction);
		if (Objects.nonNull(createdTransaction)) {
			LOGGER.info("Sending transaction mail to the user");
			emailService.sendUserTransactionMail(createdTransaction);
			UserSchemeId userSchemeId = new UserSchemeId(createdTransaction.getUser(), createdTransaction.getMutualFundScheme());
			if (userSchemeRepository.existsById(userSchemeId)) {
				UserScheme userScheme = new UserScheme(userSchemeId, createdTransaction.getNavUnits(), createdTransaction.getAmount());
				userSchemeService.updateUserScheme(userScheme);
			} else {
				UserScheme userScheme = new UserScheme(userSchemeId, createdTransaction.getNavUnits(), createdTransaction.getAmount());
				LOGGER.info("User: {} successfully made an investment in scheme '{}'", userSchemeId.getUser().getFirstName(), userSchemeId.getScheme().getSchemeName());
				userSchemeService.createUserScheme(userScheme);
			}
		}

		return createdTransaction; 
	}

	@Override
	public Transaction createRedeemTransaction(Transaction transaction) throws IOException, TemplateException, MessagingException {

		LOGGER.info("Creating Transaction with Net Asset Value: {}, Nav Units: {}", transaction.getNetAssetValue(), transaction.getNavUnits());
		Transaction createdTransaction = transactionRepository.save(transaction);
		if (Objects.nonNull(createdTransaction)) {
			LOGGER.info("Sending transaction mail to the user");
			emailService.sendUserTransactionMail(createdTransaction);
			UserSchemeId userSchemeId = new UserSchemeId(createdTransaction.getUser(), createdTransaction.getMutualFundScheme());
			userSchemeService.deleteUserScheme(userSchemeId);
		}

		return createdTransaction;
	}

	@Override
	public List<Transaction> showUserTransactions(int userId) {
		LOGGER.info("Fetching transactions for user with userId: {}", userId);

		return transactionRepository.findTrasactionsByUserId(userId);
	}

	@Override
	public List<Object[]> getTotalAmountByMutualFund(int userId) {
		LOGGER.info("Fetching piechart Data");

		return transactionRepository.getTotalAmountByMutualFund(userId);
	}

	@Override
	public Page<Transaction> listAllInvestments(int pageNumber, int userId) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3);

		return transactionRepository.findInvestTransactionsByUserId(userId, pageable);
	}

	@Override
	public Page<Transaction> listAllRedeems(int pageNumber, int userId) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3);

		return transactionRepository.findRedeemTransactionsByUserId(userId, pageable);
	}
}
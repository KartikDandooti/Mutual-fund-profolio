package com.valtech.poc.mutualfundportfolio.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.valtech.poc.mutualfundportfolio.entities.Transaction;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

/*
 * Service interface for handling transactions, such as creating new
 * transactions, retrieving user-specific transactions, and fetching investment
 * and redeem details.
 */
public interface TransactionService {

	/*
	 * Creating a new transaction after an investment or a redemption
	 * 
	 * @param The transaction object to be created
	 * 
	 * @return The transaction that was created
	 */
	Transaction createInvestTransaction(Transaction transaction) throws IOException, TemplateException, MessagingException;

	/*
	 * Creating a new transaction after an investment or a redemption
	 * 
	 * @param The transaction object to be created
	 * 
	 * @return The transaction that was created
	 */
	Transaction createRedeemTransaction(Transaction transaction) throws IOException, TemplateException, MessagingException;

	/*
	 * Getting all transactions that are made by a user
	 * 
	 * @param The user whose transactions are to be displayed
	 * 
	 * @return List of all transactions made by a user
	 */
	List<Transaction> showUserTransactions(int userId);

	/*
	 * Getting all investment transactions made by a user
	 * 
	 * @param The user whose investments are to be viewed, The number of elements to
	 * be displayed in 1 page for pagination
	 * 
	 * @return All investments made by a user with pagination
	 */
	Page<Transaction> listAllInvestments(int pageNumber, int userId);

	/*
	 * Getting all redeem transactions made by a user
	 * 
	 * @param The user whose redeem transactions are to be viewed, The number of
	 * elements to be displayed in 1 page for pagination
	 * 
	 * @return All redeem transactions made by a user with pagination
	 */
	Page<Transaction> listAllRedeems(int pageNumber, int userId);

	/*
	 * Fetching total amount invested in all the mutual funds by mutual fund type
	 * 
	 * @param The user whose total invested amount is to be viewed
	 * 
	 * @return The total amount that is currently invested according to mutual funs
	 * type
	 */
	public List<Object[]> getTotalAmountByMutualFund(int userId);
}
package com.valtech.poc.mutualfundportfolio.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.entities.Transaction;
import com.valtech.poc.mutualfundportfolio.entities.TransactionType;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.entities.UserSchemeId;
import com.valtech.poc.mutualfundportfolio.repositories.TransactionRepository;
import com.valtech.poc.mutualfundportfolio.repositories.UserSchemeRepository;
import com.valtech.poc.mutualfundportfolio.services.EmailService;
import com.valtech.poc.mutualfundportfolio.services.UserSchemeService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	@Mock
	private User user;

	@Mock
	private EmailService emailService;

	@Mock
	private UserSchemeService userSchemeService;

	@Mock
	private UserSchemeRepository userSchemeRepository;

	@Mock
	private User testUser;

	@Mock
	private Transaction testInvestTransaction;

	@Mock
	private Transaction testRedeemTransaction;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("John", "Doe", 30, 1234567890, "john.doe@example.com", "password");
		testUser.setId(1);

		testInvestTransaction = new Transaction(testUser, null, BigDecimal.valueOf(10.0), BigDecimal.valueOf(100.0),
				BigDecimal.valueOf(5.0), LocalDateTime.now(), TransactionType.INVEST);
		testInvestTransaction.setId(1);

		testRedeemTransaction = new Transaction(testUser, null, BigDecimal.valueOf(10.0), BigDecimal.valueOf(50.0),
				BigDecimal.valueOf(2.0), LocalDateTime.now(), TransactionType.REDEEM);
		testRedeemTransaction.setId(2);
	}

	@Test
    void testCreateInvestTransaction() throws IOException, TemplateException, MessagingException {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testInvestTransaction);
        when(userSchemeRepository.existsById(any(UserSchemeId.class))).thenReturn(true);

        Transaction createdTransaction = transactionService.createInvestTransaction(testInvestTransaction);

        assertNotNull(createdTransaction);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(userSchemeService, times(1)).updateUserScheme(any(UserScheme.class));
        verify(emailService, times(1)).sendUserTransactionMail(any(Transaction.class));
    }

	@Test
	void testCreateRedeemTransaction() throws IOException, TemplateException, MessagingException {
		lenient().when(transactionRepository.save(any(Transaction.class))).thenReturn(testRedeemTransaction);
		lenient().when(userSchemeRepository.existsById(any(UserSchemeId.class))).thenReturn(true);
		Transaction createdTransaction = transactionService.createRedeemTransaction(testRedeemTransaction);

		assertNotNull(createdTransaction);
		verify(transactionRepository, times(1)).save(any(Transaction.class));
		verify(userSchemeService, times(1)).deleteUserScheme(any(UserSchemeId.class));
		verify(emailService, times(1)).sendUserTransactionMail(any(Transaction.class));
	}

	@Test
	void testShowUserTransactions() {
		int userId = 1;
		Transaction transaction = new Transaction();
		transaction.setId(1);
		transaction.setNetAssetValue(new BigDecimal(100));
		transaction.setNavUnits(new BigDecimal(10));
		user.setId(userId);
		transaction.setUser(user);
		Transaction transaction2 = new Transaction();
		transaction.setId(2);
		transaction.setNetAssetValue(new BigDecimal(10000));
		transaction.setNavUnits(new BigDecimal(101));
		user.setId(userId);
		transaction2.setUser(user);
		List<Transaction> mockTransactions = new ArrayList<Transaction>();
		mockTransactions.add(transaction);
		mockTransactions.add(transaction2);
		when(transactionRepository.findTrasactionsByUserId(userId)).thenReturn(mockTransactions);

		List<Transaction> userTransactions = transactionService.showUserTransactions(userId);

		assertNotNull(userTransactions);
		assertEquals(2, userTransactions.size());
		assertEquals(mockTransactions.size(), userTransactions.size());
	}

	@Test
	void testGetTotalAmountByMutualFundType() {
		int userId = 1;
		List<Object[]> mockResult = new ArrayList<>();
		mockResult.add(new Object[] { "Equity", 100.0 });
		mockResult.add(new Object[] { "Debt", 1000.0 });
		mockResult.add(new Object[] { "Hybrid", 2000.0 });
		when(transactionRepository.getTotalAmountByMutualFund(userId)).thenReturn(mockResult);

		List<Object[]> result = transactionService.getTotalAmountByMutualFund(userId);

		assertNotNull(result);
		assertEquals(mockResult, result);
	}

	@Test
	void testlistAllInvestments() {
		MutualFund mutualfund = new MutualFund(1, "Equity", "Equity Mutual Fund Type");

		MutualFundScheme mutualFundScheme = new MutualFundScheme(1, "SBI Equity", "SBI Equity Mutual Fund Scheme",
				new BigDecimal(200), mutualfund);

		List<Transaction> investTransactions = new ArrayList<>();
		investTransactions.add(new Transaction(user, mutualFundScheme, new BigDecimal(200), new BigDecimal(2000),
				new BigDecimal(10), LocalDateTime.now(), TransactionType.INVEST));
		investTransactions.add(new Transaction(user, mutualFundScheme, new BigDecimal(200), new BigDecimal(4000),
				new BigDecimal(20), LocalDateTime.now(), TransactionType.INVEST));
		Page<Transaction> expectedPage = new PageImpl<>(investTransactions);
		Pageable pageable = PageRequest.of(0, 3);
		when(transactionRepository.findInvestTransactionsByUserId(1, pageable)).thenReturn(expectedPage);

		Page<Transaction> result = transactionService.listAllInvestments(1, 1);

		verify(transactionRepository).findInvestTransactionsByUserId(1, pageable);
		assertEquals(expectedPage, result);
		assertEquals(expectedPage.getContent().size(), result.getContent().size());
	}

	@Test
	void testlistAllRedeems() {
		MutualFund mutualfund = new MutualFund(1, "Equity", "Equity Mutual Fund Type");

		MutualFundScheme mutualFundScheme = new MutualFundScheme(1, "SBI Equity", "SBI Equity Mutual Fund Scheme",
				new BigDecimal(200), mutualfund);

		List<Transaction> redeemTransactions = new ArrayList<>();
		redeemTransactions.add(new Transaction(user, mutualFundScheme, new BigDecimal(200), new BigDecimal(2000),
				new BigDecimal(10), LocalDateTime.now(), TransactionType.REDEEM));
		redeemTransactions.add(new Transaction(user, mutualFundScheme, new BigDecimal(200), new BigDecimal(4000),
				new BigDecimal(20), LocalDateTime.now(), TransactionType.REDEEM));
		redeemTransactions.add(new Transaction(user, mutualFundScheme, new BigDecimal(100), new BigDecimal(3000),
				new BigDecimal(15), LocalDateTime.now(), TransactionType.REDEEM));

		Page<Transaction> expectedPage = new PageImpl<>(redeemTransactions);
		Pageable pageable = PageRequest.of(0, 3);
		when(transactionRepository.findRedeemTransactionsByUserId(1, pageable)).thenReturn(expectedPage);

		Page<Transaction> result = transactionService.listAllRedeems(1, 1);

		verify(transactionRepository).findRedeemTransactionsByUserId(1, pageable);
		assertEquals(expectedPage, result);
		assertEquals(expectedPage.getContent().size(), result.getContent().size());
	}
}
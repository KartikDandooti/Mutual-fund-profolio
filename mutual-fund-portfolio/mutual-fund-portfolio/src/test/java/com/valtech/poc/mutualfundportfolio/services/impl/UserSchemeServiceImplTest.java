package com.valtech.poc.mutualfundportfolio.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.entities.UserSchemeId;
import com.valtech.poc.mutualfundportfolio.repositories.UserSchemeRepository;

@SpringBootTest
@Transactional
class UserSchemeServiceImplTest {

	@Mock
	private UserSchemeRepository userSchemeRepository;

	@InjectMocks
	private UserSchemeServiceImpl userSchemeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateUserScheme() {
		User user = new User("Priyanka", "Patil", 23, 8970564320L, "pri123@gmail.com", "Pri@123");
		MutualFund fund = new MutualFund("Equity", "Equity Mutual Fund");
		MutualFundScheme scheme = new MutualFundScheme("KFC Equity", "Equity Mutual Funf", new BigDecimal(100), fund);
		UserSchemeId id = new UserSchemeId(user, scheme);
		UserScheme userScheme = new UserScheme(id, new BigDecimal(20), new BigDecimal(2000));

		when(userSchemeRepository.existsById(any())).thenReturn(false);
		when(userSchemeRepository.save(any())).thenReturn(userScheme);

		UserScheme result = userSchemeService.createUserScheme(userScheme);

		assertNotNull(result);
		assertEquals(userScheme, result);
	}

	@Test
	void testFindSchemesByUser() {
		int userId = 1;
		User user = new User("Priyanka", "Patil", 23, 8970564320L, "pri123@gmail.com", "Pri@123");
		MutualFund fund = new MutualFund("Equity", "Equity Mutual Fund");
		MutualFundScheme scheme = new MutualFundScheme("KFC Equity", "Equity Mutual Funf", new BigDecimal(100), fund);
		MutualFundScheme scheme1 = new MutualFundScheme("SBI Equity", "Equity Mutual Funf", new BigDecimal(200), fund);
		UserSchemeId id = new UserSchemeId(user, scheme);
		UserSchemeId id1 = new UserSchemeId(user, scheme);
		UserSchemeId id2 = new UserSchemeId(user, scheme1);
		List<UserScheme> userSchemes = new ArrayList<>();
		userSchemes.add(new UserScheme(id, new BigDecimal(20), new BigDecimal(2000)));
		userSchemes.add(new UserScheme(id1, new BigDecimal(25), new BigDecimal(2500)));
		userSchemes.add(new UserScheme(id2, new BigDecimal(25), new BigDecimal(5000)));

		when(userSchemeRepository.findByIdUserId(userId)).thenReturn(userSchemes);

		List<UserScheme> result = userSchemeService.findSchemesByUser(userId);

		assertNotNull(result);
		assertEquals(userSchemes, result);
	}

	@Test
	void testGetSumInvestedAmount() {
		int userId = 1;
		BigDecimal expectedSumInvestedAmount = new BigDecimal(1000);

		when(userSchemeRepository.sumInvestedAmountByUserId(userId)).thenReturn(expectedSumInvestedAmount);

		BigDecimal result = userSchemeService.getSumInvestedAmount(userId);

		assertEquals(expectedSumInvestedAmount, result);
	}

	@Test
	void testGetSumCurrentAmount() {
		int userId = 1;
		BigDecimal expectedSumCurrentAmount = new BigDecimal(1200);

		when(userSchemeRepository.sumCurrentAmountByUserId(userId)).thenReturn(expectedSumCurrentAmount);

		BigDecimal result = userSchemeService.getSumCurrentAmount(userId);

		assertEquals(expectedSumCurrentAmount, result);
	}

	@Test
	void testCalculateReturnsPercentage() {
		int userId = 1;
		BigDecimal sumInvestedAmount = new BigDecimal(1000);
		BigDecimal sumCurrentAmount = new BigDecimal(1200);
		BigDecimal expectedReturnsPercentage = new BigDecimal(20).setScale(2);

		when(userSchemeRepository.sumInvestedAmountByUserId(userId)).thenReturn(sumInvestedAmount);
		when(userSchemeRepository.sumCurrentAmountByUserId(userId)).thenReturn(sumCurrentAmount);
		BigDecimal result = userSchemeService.calculateReturnsPercentage(userId);

		assertEquals(expectedReturnsPercentage, result);
	}
}
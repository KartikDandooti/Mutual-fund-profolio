package com.valtech.poc.mutualfundportfolio.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.repositories.MutualFundRepository;
import com.valtech.poc.mutualfundportfolio.repositories.MutualFundSchemeRepository;
import com.valtech.poc.mutualfundportfolio.services.UserSchemeService;

@ExtendWith(MockitoExtension.class)
class MutualFundServiceImplTest {

	@Mock
	private Random random;

	@Mock
	private MutualFundRepository mockMutualFundRepository;

	@Mock
	private MutualFundSchemeRepository mockSchemeRepository;

	@Mock
	UserSchemeService mockUserSchemeService;

	@InjectMocks
	private MutualFundServiceImpl mutualFundService;

	// Creating a new MutualFundType should add it to the database and return the
	// created object.
	@Test
	void testCreateMutualFundType() {
		// Given
		MutualFund mutualFund = new MutualFund("Equity", "This is Equity type Mutual Fund");
		when(mockMutualFundRepository.save(mutualFund)).thenReturn(mutualFund);

		// When
		MutualFund result = mutualFundService.createMutualFund(mutualFund);

		// Then
		verify(mockMutualFundRepository).save(mutualFund);
		assertEquals(mutualFund, result);
	}
 
	// Updating an existing MutualFundType should update it in the database and
	// return the updated object.
	@Test
	void testUpdateMutualFundType() {
		// Given
		MutualFund mutualFund = new MutualFund(1, "Equity", "This is Equity type Mutual Fund");
		when(mockMutualFundRepository.save(mutualFund)).thenReturn(mutualFund);

		// When
		MutualFund result = mutualFundService.updateMutualFund(mutualFund);

		// Then
		verify(mockMutualFundRepository).save(mutualFund);
		assertEquals(mutualFund, result);
	}

	// Retrieving all MutualFundTypes should return a list of all MutualFundTypes in
	// the database.
	@Test
	void testGetAllMutualFundTypes() {
		// Given
		List<MutualFund> mutualFunds = new ArrayList<>();
		mutualFunds.add(new MutualFund("Equity", "This is Equity type Mutual Fund"));
		mutualFunds.add(new MutualFund("Debt", "This is Debt type Mutual Fund"));
		when(mockMutualFundRepository.findAll()).thenReturn(mutualFunds);

		// When
		List<MutualFund> result = mutualFundService.getAllMutualFundTypes();

		// Then
		verify(mockMutualFundRepository).findAll();
		assertEquals(mutualFunds, result);
	}

	// Retrieving a MutualFundType by id should return the MutualFundType with the
	// given id.
	@Test
	void testRetrieveMutualFundTypeById() {
		// Given
		int id = 1;
		MutualFund expectedMutualFund = new MutualFund("Equity", "This is Equity type Mutual Fund");
		when(mockMutualFundRepository.getReferenceById(id)).thenReturn(expectedMutualFund);

		// When
		MutualFund result = mutualFundService.getMutualFundById(id);

		// Then
		verify(mockMutualFundRepository).getReferenceById(id);
		assertEquals(expectedMutualFund, result);
	}

	// Creating a new MutualFundScheme should add it to the database and return the
	// created object.
	@Test
	void testCreateMutualFundScheme() {
		// Given
		MutualFundScheme scheme = new MutualFundScheme("Test Scheme", "This is a test scheme", new BigDecimal(200), new MutualFund());
		when(mockSchemeRepository.save(scheme)).thenReturn(scheme);

		// When
		MutualFundScheme result = mutualFundService.createMutualFundScheme(scheme);

		// then
		verify(mockSchemeRepository).save(scheme);
		assertEquals(scheme, result);
	}

	// Updating an existing MutualFundScheme should update it in the database and
	// return the updated object.
	@Test
	void testUpdateMutualFundScheme() {
		// Given
		MutualFundScheme scheme = new MutualFundScheme(1, "SBI Equity", "This is SBI Equity  Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund"));
		when(mockSchemeRepository.save(scheme)).thenReturn(scheme);

		// When
		MutualFundScheme result = mutualFundService.updateMutualFundScheme(scheme);

		// Then
		verify(mockSchemeRepository).save(scheme);
		assertEquals(scheme, result);
	}

	// Retrieving all MutualFundSchemes should return a list of all
	// MutualFundSchemes in the database.
	@Test
	void testRetrieveAllMutualFundSchemes() {
		// Given
		List<MutualFundScheme> expectedSchemes = new ArrayList<>();
		expectedSchemes.add(new MutualFundScheme("SBI Equity", "This is SBI Equity  Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund")));
		expectedSchemes.add(new MutualFundScheme("Axis Equity", "This is Axis Equity Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund")));
		when(mockSchemeRepository.findAll()).thenReturn(expectedSchemes);

		// When
		List<MutualFundScheme> result = mutualFundService.getAllMutualFundSchemes();

		// Then
		verify(mockSchemeRepository).findAll();
		assertEquals(expectedSchemes, result);
	}

	// Retrieving a MutualFundScheme by id should return the MutualFundScheme with
	// the given id.
	@Test
	void testRetrieveMutualFundSchemeById() {
		// Given
		int schemeId = 1;
		MutualFundScheme expectedScheme = new MutualFundScheme("SBI Equity", "This is SBI Equity  Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund"));
		when(mockSchemeRepository.getReferenceById(schemeId)).thenReturn(expectedScheme);

		// When
		MutualFundScheme result = mutualFundService.getMutualFundSchemeById(schemeId);

		// Then
		verify(mockSchemeRepository).getReferenceById(schemeId);
		assertEquals(expectedScheme, result);
	}

	// Returns a list of MutualFundSchemes based on a valid mutualFundId.
	@Test
	void testgetAllSchemesByMutualFundType() {
		// Given
		int mutualFundId = 1;
		List<MutualFundScheme> expectedSchemes = new ArrayList<>();
		expectedSchemes.add(new MutualFundScheme("SBI Equity", "This is SBI Equity  Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund")));
		expectedSchemes.add(new MutualFundScheme("Axis Equity", "This is Axis Equity Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund")));
		when(mockSchemeRepository.findAllBymutualFund(mutualFundId)).thenReturn(expectedSchemes);

		// When
		List<MutualFundScheme> actualSchemes = mutualFundService.getAllSchemesByMutualFundType(mutualFundId);

		// Then
		assertEquals(expectedSchemes, actualSchemes);
	}

	// Returns an empty list if no MutualFundSchemes are found for the given
	// mutualFundId.
	@Test
	void testReturnsEmptyListForNoSchemesFound() {
		// Given
		int mutualFundId = 1;
		List<MutualFundScheme> expectedSchemes = new ArrayList<>();
		when(mockSchemeRepository.findAllBymutualFund(mutualFundId)).thenReturn(expectedSchemes);

		// when
		List<MutualFundScheme> actualSchemes = mutualFundService.getAllSchemesByMutualFundType(mutualFundId);

		// Then
		assertTrue(actualSchemes.isEmpty());
	}

	// Listing all MutualFundSchemes with pagination should return a page of
	// MutualFundSchemes with the given page number.
	@Test
	void testListingAllMutualFundSchemesWithPagination() {
		// Given
		List<MutualFundScheme> schemes = new ArrayList<>();
		schemes.add(new MutualFundScheme("SBI Equity", "This is SBI Equity  Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund")));
		schemes.add(new MutualFundScheme("Axis Equity", "This is Axis Equity Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund")));
		schemes.add(new MutualFundScheme("SBI Debt", "This is SBI Debt type Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Debt", "This is Debt type Mutual Fund")));
		schemes.add(new MutualFundScheme("Axis Debt", "This is Axis Debt type Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Debt", "This is Debt type Mutual Fund")));
		Page<MutualFundScheme> expectedPage = new PageImpl<>(schemes);
		Pageable pageable = PageRequest.of(0, 4);
		when(mockSchemeRepository.findAll(pageable)).thenReturn(expectedPage);

		// When
		Page<MutualFundScheme> result = mutualFundService.listAllSchemes(1);

		// Then
		verify(mockSchemeRepository).findAll(pageable);
		assertEquals(expectedPage, result);
	}

	// Returns a BigDecimal value based on the net asset value of the given
	// MutualFundScheme object and a random variance value between -0.1 and 1.0.
	@Test
	void testReturnsNavValueBasedOnNavAndRandomVariance() {
		// given
		MutualFundScheme scheme = new MutualFundScheme();
		BigDecimal netAssetValue = new BigDecimal(100);
		scheme.setNetAssetValue(netAssetValue);

		// when
		BigDecimal result = mutualFundService.fetchNewNavValueForScheme(scheme);

		assertNotEquals(netAssetValue, result);
	}

	// Updating the NAV values for all MutualFundSchemes should update the current
	// amount for all UserSchemes associated with the MutualFundSchemes.
	@Test
	void testUpdateNavForSchemes() {
		// Given
		MutualFundScheme scheme1 = new MutualFundScheme("SBI Equity", "This is SBI Equity  Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund"));
		scheme1.setId(1);
		MutualFundScheme scheme2 = new MutualFundScheme("Axis Equity", "This is Axis Equity Mutual Fund Scheme", new BigDecimal(200), new MutualFund("Equity", "This is Equity type Mutual Fund"));
		scheme2.setId(2);
		List<MutualFundScheme> schemes = new ArrayList<>();
		schemes.add(scheme1);
		schemes.add(scheme2);
		when(mockSchemeRepository.findAll()).thenReturn(schemes);

		// When
		mutualFundService.updateNavForSchemes();

		// Then
		verify(mockSchemeRepository).findAll();
		verify(mockUserSchemeService, Mockito.times(2)).updateCurrentAmountForAllUsers(Mockito.anyInt(), Mockito.any(BigDecimal.class));
//	    verify(mockSchemeRepository).saveAll(Mockito.anyList());
	}

}
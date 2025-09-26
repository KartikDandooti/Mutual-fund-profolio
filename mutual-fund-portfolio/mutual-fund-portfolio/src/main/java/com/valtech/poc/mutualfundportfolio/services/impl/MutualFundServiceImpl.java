package com.valtech.poc.mutualfundportfolio.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.repositories.MutualFundRepository;
import com.valtech.poc.mutualfundportfolio.repositories.MutualFundSchemeRepository;
import com.valtech.poc.mutualfundportfolio.services.MutualFundService;
import com.valtech.poc.mutualfundportfolio.services.UserSchemeService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MutualFundServiceImpl implements MutualFundService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MutualFundServiceImpl.class);

	@Autowired
	private MutualFundRepository mutualFundRepository;
	@Autowired
	private MutualFundSchemeRepository schemeRepository;
	@Autowired
	private UserSchemeService userSchemeService;

	private Random random = new Random();

	@Override
	public MutualFund createMutualFund(MutualFund mutualFund) {
		LOGGER.info("Creating MutualFundType: {},Description: {}", mutualFund.getType(), mutualFund.getDescription());

		return mutualFundRepository.save(mutualFund);
	}

	@Override
	public MutualFund updateMutualFund(MutualFund mutualFund) {
		LOGGER.info("Updating MutualFundType:{} with MutualFundId: {}", mutualFund.getType(), mutualFund.getId());

		return mutualFundRepository.save(mutualFund);
	}

	@Override
	public List<MutualFund> getAllMutualFundTypes() {
		LOGGER.info("Listing all Mutual Fund types");

		return mutualFundRepository.findAll();
	}

	@Override
	public MutualFund getMutualFundById(int id) {
		LOGGER.info("Fetching MutualFundType with MutualFundId: {}", id);

		return mutualFundRepository.getReferenceById(id);
	}

	@Override
	public MutualFundScheme createMutualFundScheme(MutualFundScheme scheme) {
		LOGGER.info("Creating MutualFundScheme with MutualFundId: {}, SchemeName: {},Description: {}", scheme.getId(),
				scheme.getSchemeName(), scheme.getDescription());

		return schemeRepository.save(scheme);
	}

	@Override
	public MutualFundScheme updateMutualFundScheme(MutualFundScheme scheme) {
		LOGGER.info("Updating MutualFundScheme with MutualFundId: {}", scheme.getId());
		userSchemeService.updateCurrentAmountForAllUsers(scheme.getId(), scheme.getNetAssetValue());

		return schemeRepository.save(scheme);
	}

	@Override
	public List<MutualFundScheme> getAllMutualFundSchemes() {
		LOGGER.info("Listing all Mutual Fund Schemes");

		return schemeRepository.findAll();
	}

	@Override
	public MutualFundScheme getMutualFundSchemeById(int id) {
		LOGGER.info("Fetching MutualFundScheme with schemeId: {}", id);

		return schemeRepository.getReferenceById(id);
	}

	@Override
	public List<MutualFundScheme> getAllSchemesByMutualFundType(int mutualFundId) {
		LOGGER.info("Getting all MutualFundSchemes based on MutualFundType with MutualFundId: {}", mutualFundId);

		return schemeRepository.findAllBymutualFund(mutualFundId);
	}

	@Scheduled(cron = "0 */10 * * * *")
	public void updateNavForSchemes() {
		LOGGER.info("Updating Nav values of each Schemes");
		List<MutualFundScheme> schemes = schemeRepository.findAll();
		for (MutualFundScheme scheme : schemes) {
			BigDecimal newNavValue = fetchNewNavValueForScheme(scheme);
			scheme.setNetAssetValue(newNavValue);
			userSchemeService.updateCurrentAmountForAllUsers(scheme.getId(), scheme.getNetAssetValue());
			LOGGER.info("Updating Nav: {} values of Scheme: {}", newNavValue, scheme);
			schemeRepository.save(scheme);
		}
	}

	public BigDecimal fetchNewNavValueForScheme(MutualFundScheme scheme) {
		double randomVariance = this.random.nextDouble() * 1.1 - 0.1;
		randomVariance = Math.max(randomVariance, -0.1);
		randomVariance = Math.min(randomVariance, 1.0);
		BigDecimal randomVarianceBigDecimal = BigDecimal.valueOf(randomVariance);
		LOGGER.info("Fetching new Nav value based on variance: {}", randomVarianceBigDecimal);

		return scheme.getNetAssetValue().multiply(BigDecimal.ONE.add(randomVarianceBigDecimal));
	}

	@Override
	public Page<MutualFundScheme> listAllSchemes(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 4);
		LOGGER.info("Adding Pagable functionality");

		return schemeRepository.findAll(pageable);
	}
}
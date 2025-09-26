package com.valtech.poc.mutualfundportfolio.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.entities.UserSchemeId;
import com.valtech.poc.mutualfundportfolio.repositories.UserSchemeRepository;
import com.valtech.poc.mutualfundportfolio.services.UserSchemeService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserSchemeServiceImpl implements UserSchemeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserSchemeServiceImpl.class);

	@Autowired
	private UserSchemeRepository userSchemeRepository;

	@Override 
	public UserScheme createUserScheme(UserScheme userScheme) {
		LOGGER.info("Created new user scheme: {}", userScheme);
		userScheme.setCurrentAmount(userScheme.getSchemeUnits().multiply(userScheme.getId().getScheme().getNetAssetValue()));

		return userSchemeRepository.save(userScheme);
	}

	@Override
	public UserScheme updateUserScheme(UserScheme userScheme) {
		UserScheme existingUserScheme = userSchemeRepository.getReferenceById(userScheme.getId());
		existingUserScheme.setSchemeUnits(existingUserScheme.getSchemeUnits().add(userScheme.getSchemeUnits()));
		existingUserScheme.setInvestedAmount(existingUserScheme.getInvestedAmount().add(userScheme.getInvestedAmount()));
		existingUserScheme.setCurrentAmount(existingUserScheme.getSchemeUnits().multiply(existingUserScheme.getId().getScheme().getNetAssetValue()));
		LOGGER.info("Updated existing user with scheme: {}", existingUserScheme);

		return userSchemeRepository.save(existingUserScheme);
	}

	@Override
	public List<UserScheme> findSchemesByUser(int id) {
		LOGGER.debug("Finding schemes for user with ID: {}", id);

		return userSchemeRepository.findByIdUserId(id);
	}

	@Override
	public UserScheme findByuserAndScheme(int userId, int schemeId) {
		LOGGER.debug("Finding user scheme for user ID: {} and scheme ID: {}", userId, schemeId);

		return userSchemeRepository.findByIdUserIdAndSchemeId(userId, schemeId);
	}

	@Override
	public BigDecimal getSumInvestedAmount(int userId) {
		LOGGER.info("Calculating sum of invested amount for user with ID: {}", userId);
		BigDecimal sumInvestedAmount = userSchemeRepository.sumInvestedAmountByUserId(userId);
		LOGGER.debug("User total Invested Money:{} ", sumInvestedAmount);

		return sumInvestedAmount != null ? sumInvestedAmount : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getSumCurrentAmount(int userId) {
		LOGGER.info("Calculating sum of return amount for user with ID: {}", userId);
		BigDecimal sumCurrentAmount = userSchemeRepository.sumCurrentAmountByUserId(userId);
		LOGGER.debug("User total Return Money:{} ", sumCurrentAmount);

		return sumCurrentAmount != null ? sumCurrentAmount : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal calculateReturnsPercentage(int userId) {
		LOGGER.info("Calculating returns percentage for user with ID: {}", userId);
		BigDecimal sumInvestedAmount = userSchemeRepository.sumInvestedAmountByUserId(userId);
		BigDecimal sumCurrentAmount = userSchemeRepository.sumCurrentAmountByUserId(userId);
		if (sumInvestedAmount == null || sumCurrentAmount == null || sumInvestedAmount == BigDecimal.ZERO) {
			LOGGER.warn("Unable to calculate returns percentage. Invested amount is zero or null.");

			return BigDecimal.ZERO;
		}
		if (sumInvestedAmount.compareTo(BigDecimal.ZERO) == 0) {
			LOGGER.warn("Unable to calculate returns percentage. Invested amount is zero.");

			return BigDecimal.ZERO;
		}
		BigDecimal returnsPercentage = ((sumCurrentAmount.subtract(sumInvestedAmount)).divide(sumInvestedAmount, 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
		returnsPercentage = returnsPercentage.setScale(2, RoundingMode.HALF_UP);
		LOGGER.info("Calculated returns percentage: {}% for user with ID: {}", returnsPercentage, userId);

		return returnsPercentage;
	}

	@Override
	public void deleteUserScheme(UserSchemeId userSchemeId) {
		LOGGER.info("Updated user scheme with id after withdrawal: {}", userSchemeId);
		userSchemeRepository.deleteById(userSchemeId);
	}

	@Override
	public void updateCurrentAmountForAllUsers(int schemId, BigDecimal newNavValue) {
		LOGGER.info("Updating current amount for all after change in NAV value");
		List<UserScheme> userSchemes = userSchemeRepository.findByIdSchemeId(schemId);
		for (UserScheme userScheme : userSchemes) {
			userScheme.setCurrentAmount(userScheme.getSchemeUnits().multiply(newNavValue));
			LOGGER.info("Updating current amount of scheme: {}", userScheme);
		}

		userSchemeRepository.saveAll(userSchemes);
	}

}
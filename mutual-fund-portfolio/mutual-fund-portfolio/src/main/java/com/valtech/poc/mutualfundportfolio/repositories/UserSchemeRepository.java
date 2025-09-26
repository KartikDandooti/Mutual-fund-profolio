package com.valtech.poc.mutualfundportfolio.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.entities.UserSchemeId;

public interface UserSchemeRepository extends JpaRepository<UserScheme, UserSchemeId> {
	
	List<UserScheme> findByIdUserId(int id);

	List<UserScheme> findByIdSchemeId(int id);

	UserScheme findByIdUserIdAndSchemeId(int userId, int schemeId);

	@Query("SELECT SUM(us.investedAmount) FROM UserScheme us WHERE us.id.user.id = ?1")
	BigDecimal sumInvestedAmountByUserId(int userId);

	@Query("SELECT SUM(us.currentAmount) FROM UserScheme us WHERE us.id.user.id = ?1")
	BigDecimal sumCurrentAmountByUserId(int userId);

	@Modifying
	void deleteById(UserSchemeId id);

}
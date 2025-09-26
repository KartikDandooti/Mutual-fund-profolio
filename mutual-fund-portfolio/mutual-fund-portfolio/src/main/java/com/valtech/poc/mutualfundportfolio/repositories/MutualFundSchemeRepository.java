package com.valtech.poc.mutualfundportfolio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;

@Repository
public interface MutualFundSchemeRepository extends PagingAndSortingRepository<MutualFundScheme, Integer>,JpaRepository<MutualFundScheme, Integer> {

	@Query(value = "SELECT s FROM MutualFundScheme s WHERE s.mutualFund.id=?1")
	List<MutualFundScheme> findAllBymutualFund(int id);
}

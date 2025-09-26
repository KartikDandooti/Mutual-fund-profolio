package com.valtech.poc.mutualfundportfolio.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valtech.poc.mutualfundportfolio.entities.Transaction;

@Repository
public interface TransactionRepository
		extends PagingAndSortingRepository<Transaction, Integer>, JpaRepository<Transaction, Integer> {

	List<Transaction> findTrasactionsByUserId(int userId);

	@Query("Select t from Transaction t  where t.transactionType='INVEST' and t.user.id=?1")
	Page<Transaction> findInvestTransactionsByUserId(int id, Pageable pageable);

	@Query("Select t from Transaction t where  t.transactionType='REDEEM' and t.user.id=?1")
	Page<Transaction> findRedeemTransactionsByUserId(int id, Pageable pageable);

	@Query("SELECT mf.type AS mutualFundType, " + "SUM(s.schemeUnits * mfs.netAssetValue) AS CurrentAmount "
			+ "FROM UserScheme s " + "JOIN MutualFundScheme mfs ON s.scheme.id = mfs.id "
			+ "JOIN MutualFund mf ON mfs.mutualFund.id = mf.id " + "WHERE s.user.id = ?1 " + "GROUP BY mf.type")
	List<Object[]> getTotalAmountByMutualFund(int userId);
	
	@Modifying
	void deleteById(int id);
}

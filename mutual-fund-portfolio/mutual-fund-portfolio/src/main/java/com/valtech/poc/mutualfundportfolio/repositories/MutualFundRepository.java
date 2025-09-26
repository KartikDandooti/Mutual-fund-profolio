package com.valtech.poc.mutualfundportfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;

@Repository
public interface MutualFundRepository extends PagingAndSortingRepository<MutualFund, Integer>, JpaRepository<MutualFund, Integer> {

}
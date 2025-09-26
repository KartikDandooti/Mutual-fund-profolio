package com.valtech.poc.mutualfundportfolio.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;

/*
 * Service interface for managing Mutual Funds and their Schemes.
 */
public interface MutualFundService {

	/*
	 * Creates a new Mutual Fund Type.
	 * 
	 * @param The Mutual Fund object to be created.
	 * 
	 * @return The created Mutual Fund.
	 */
	MutualFund createMutualFund(MutualFund mutualFund);

	/*
	 * Updates an existing Mutual Fund Type.
	 * 
	 * @param The Mutual Fund object with updated information.
	 * 
	 * @return The updated Mutual Fund.
	 */
	MutualFund updateMutualFund(MutualFund mutualFund);

	/*
	 * Retrieves a list of all available Mutual Fund Types.
	 * 
	 * @return A list of Mutual Funds.
	 */
	List<MutualFund> getAllMutualFundTypes();

	/*
	 * Retrieves a specific Mutual Fund Type by its unique id.
	 * 
	 * @param The id of the Mutual Fund.
	 * 
	 * @return The Mutual Fund object.
	 */
	MutualFund getMutualFundById(int id);

	/*
	 * Creates a new Mutual Fund Scheme.
	 * 
	 * @param The Mutual Fund Scheme object to be created.
	 * 
	 * @return The created Mutual Fund Scheme.
	 */
	MutualFundScheme createMutualFundScheme(MutualFundScheme scheme);

	/*
	 * Updates an existing Mutual Fund Scheme.
	 * 
	 * @param The Mutual Fund Scheme object with updated information.
	 * 
	 * @return The updated Mutual Fund Scheme.
	 */
	MutualFundScheme updateMutualFundScheme(MutualFundScheme scheme);

	/*
	 * Retrieves a list of all available Mutual Fund Schemes.
	 * 
	 * @return A list of Mutual Fund Schemes.
	 */
	List<MutualFundScheme> getAllMutualFundSchemes();

	/*
	 * Retrieves a specific Mutual Fund Scheme by its unique id.
	 * 
	 * @param The id of the Mutual Fund Scheme.
	 * 
	 * @return The Mutual Fund Scheme object.
	 */
	MutualFundScheme getMutualFundSchemeById(int id);

	/*
	 * Retrieves a list of all Mutual Fund Schemes associated with a specific Mutual
	 * Fund.
	 * 
	 * @param The id of the Mutual Fund.
	 * 
	 * @return A list of Mutual Fund Schemes.
	 */
	List<MutualFundScheme> getAllSchemesByMutualFundType(int mutualFundId);

	/*
	 * Retrieves a paginated list of all Mutual Fund Schemes.
	 * 
	 * @param The page number to retrieve.
	 * 
	 * @return A paginated list of Mutual Fund Schemes.
	 */
	Page<MutualFundScheme> listAllSchemes(int pageNumber);
}
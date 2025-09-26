package com.valtech.poc.mutualfundportfolio.services;

import java.math.BigDecimal;
import java.util.List;

import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.entities.UserSchemeId;

/*
 *  Service interface for managing user investment schemes, including creation,
 * retrieval, and updates of user schemes.
 */
public interface UserSchemeService {

	/*
	 * Creating a user scheme. If a user scheme with the id already exists then a
	 * new user scheme won't be created and the same user scheme will be updated
	 * 
	 * @param The user scheme object to be created or updated
	 * 
	 * @return The newly added or updated user scheme object
	 */
	UserScheme createUserScheme(UserScheme userScheme);
	/*
	 * Updating a user scheme. 
	 * 
	 * @param The user scheme object to be created or updated
	 * 
	 * @return The newly added or updated user scheme object
	 */
	UserScheme updateUserScheme(UserScheme userScheme);

	/*
	 * Retrieving a list of all the user schemes of a user by user_id
	 * 
	 * @param The user_id of a user whose schemes are to be viewed
	 * 
	 * @return The list of all the user scheme objects of a user
	 */
	List<UserScheme> findSchemesByUser(int id);

	/*
	 * Retrieving a particular scheme that a user has invested in
	 * 
	 * @param The user_id of the user and the scheme_id of the scheme
	 * 
	 * @return The user scheme object with the matching user_id and scheme_id
	 */
	UserScheme findByuserAndScheme(int userId, int schemeId);

	/*
	 * Getting the sum of all the amount that has been invested by a user
	 * 
	 * @param The user_id of the user whose total current invested amount we want
	 * 
	 * @return Total amount invested by user with given user_id
	 */
	BigDecimal getSumInvestedAmount(int userId);

	/*
	 * Getting the sum of the amount currently invested by the user;
	 * 
	 * @param The user_id of the user whose current invested amount we want
	 * 
	 * @return Current invested amount of user with the given user_id;
	 */
	BigDecimal getSumCurrentAmount(int userId);

	/*
	 * Calculating the returns percentage of the current user based on their current
	 * invested and total invested amounts. Also checking if either of the two are 0
	 * or null and handling those cases
	 * 
	 * @param The user_id of the current user
	 * 
	 * @return The return percentage of the current user
	 */
	BigDecimal calculateReturnsPercentage(int userId);

	/*
	 * After successfully withdrawing an investment from a user scheme, we are
	 * deleting the user scheme as the invested amount now is 0
	 * 
	 * @param User_scheme_id of the user scheme we are deleting
	 * 
	 */
	void deleteUserScheme(UserSchemeId userSchemeId);

	/*
	 * Getting all users current amount and changing and setting it according to the
	 * new net asset value
	 * 
	 * @param scheme_id of the scheme that is being updated and the new net asset
	 * value which was updated
	 */
	void updateCurrentAmountForAllUsers(int schemId, BigDecimal newNavValue);
}
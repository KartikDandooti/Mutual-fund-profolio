package com.valtech.poc.mutualfundportfolio.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.valtech.poc.mutualfundportfolio.entities.Transaction;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.models.UserRegisterModel;
import com.valtech.poc.mutualfundportfolio.services.TransactionService;
import com.valtech.poc.mutualfundportfolio.services.UserService;

@Controller
public class UserProfileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

	private static final String CHANGE_PASSWORD_VIEW = "changePassword";
	private static final String USER_PROFILE_VIEW = "userProfileView";
	private static final String USER_PROFILE_EDIT_VIEW = "userProfileEdit";
	private static final String INVESTMENT_TRANSACTIONS_VIEW = "userInvestmentTransaction";
	private static final String REDEEM_TRANSACTION_VIEW = "userRedeemTransaction";


	@Autowired
	private UserService userService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private void userIconAttributes(Model model, UserDetails userDetails) {
		User user = userService.findUserByUsername(userDetails.getUsername());
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("lastName", user.getLastName());
		model.addAttribute("userId", user.getId());
		LOGGER.info("Passing attributes to user icon to user: {} with ID: {}", user.getFirstName() + user.getLastName(), user.getId());
	}

	@GetMapping("/mutualfund/userprofile-view")
	public String userProfileView(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Handling request to view user profile for user");

		userIconAttributes(model, userDetails);
		User user = userService.findUserByUsername(userDetails.getUsername());
		model.addAttribute("user", user);

		LOGGER.info("User profile details retrieved successfully for user: {}", user.getFirstName() + user.getLastName());

		return USER_PROFILE_VIEW;
	}

	@GetMapping("/mutualfund/userprofile-edit")
	public String userProfileEditView(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("successMessage") String successMessage) {
		LOGGER.info("Handling request to edit user profile of user");

		User user = userService.findUserByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		model.addAttribute("successMessage", successMessage);
		userIconAttributes(model, userDetails);

		LOGGER.info("User profile edit view loaded successfully for user: {}", user.getFirstName() + user.getLastName());

		return USER_PROFILE_EDIT_VIEW;
	}

	@PostMapping("/mutualfund/userprofile-edit")
	public String processUserProfileEditViewDetails(
	      @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("userRegisterModel") UserRegisterModel userModel, RedirectAttributes redirectAttributes
	) {
		LOGGER.info("Processing user profile update for user");

		User user = userService.findUserByUsername(userDetails.getUsername());
		user.setFirstName(StringUtils.capitalize(userModel.getFirstName()));
		user.setLastName(StringUtils.capitalize(userModel.getLastName()));
		user.setAge(userModel.getAge());
		user.setPhoneNumber(userModel.getPhoneNumber());
		userService.updateUser(user);
		redirectAttributes.addFlashAttribute("successMessage", "Profile Updated Successfully");

		LOGGER.info("User profile updated successfully for user: {}", user.getFirstName() + user.getLastName());

		return "redirect:/mutualfund/userprofile-view";
	}

	@GetMapping("/mutualfund/userprofile-change-password")
	public String userChangePassword(@AuthenticationPrincipal UserDetails userDetails, Model model, @ModelAttribute("errorMessage") String errorMessage) {
		LOGGER.info("Handling request to change password for user");
		userIconAttributes(model, userDetails);

		return CHANGE_PASSWORD_VIEW;
	}

	@PostMapping("/mutualfund/userprofile-change-password")
	public String processUserChangePassword(@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam String currentPassword, @RequestParam String newPassword, Model model,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Processing change password request for user: {}", userDetails.getUsername());
		User user = userService.findUserByUsername(userDetails.getUsername());
		if (passwordEncoder.matches(currentPassword, user.getPassword())) {
			if (currentPassword.equals(newPassword)) {
				LOGGER.warn("Change password failed: Old and new password must not be the same!");
				redirectAttributes.addFlashAttribute("errorMessage", "Old and new password must not be the same!");

				return "redirect:/mutualfund/userprofile-change-password";
			} else {
				LOGGER.info("Password changed successfully!");
				userService.changePassword(user, newPassword);
				redirectAttributes.addFlashAttribute("passwordChangeMessage", "Password changed successfully!");


				return "redirect:/mutualfund/login";
			}
		} else {
			LOGGER.warn("Change password failed: Enter a valid old password!");
			redirectAttributes.addFlashAttribute("errorMessage", "Enter a valid old password!");

			return "redirect:/mutualfund/userprofile-change-password";
		}
	}

	@GetMapping("/mutualfund/invest-transactions")
	public String showUserInvestTransactions(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Handling request to display User: {} Invest Transactions.", userDetails.getUsername());

		return listByInvestPage(model, userDetails, 1);
	}

	@RequestMapping("/mutualfund/pageI/{pageNumberI}")
	public String listByInvestPage(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable("pageNumberI") int currentPage) {
		LOGGER.info("Handling request to list User Invest Transaction on page: {}", currentPage);

		User user = userService.findUserByUsername(userDetails.getUsername());
		Page<Transaction> page = transactionService.listAllInvestments(currentPage, user.getId());
		userIconAttributes(model, userDetails);
		model.addAttribute("investCurrentPage", currentPage);
		model.addAttribute("investTotalitems", page.getTotalElements());
		model.addAttribute("investTotalpages", page.getTotalPages());
		model.addAttribute("investTransactions", page.getContent());

		LOGGER.info("User Invest Transactions listed successfully for page: {}", currentPage);

		return INVESTMENT_TRANSACTIONS_VIEW;
	}

	@GetMapping("/mutualfund/redeem-transactions")
	public String showUserRedeemTransactions(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Handling request to display User: {} Redeem Transactions.", userDetails.getUsername());

		return listByRedeemPage(model, userDetails, 1);
	}

	@RequestMapping("/mutualfund/pageR/{pageNumberR}")
	public String listByRedeemPage(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable("pageNumberR") int currentPage) {
		LOGGER.info("Handling request to list User Redeem Transaction on page: {}", currentPage);

		User user = userService.findUserByUsername(userDetails.getUsername());
		Page<Transaction> page = transactionService.listAllRedeems(currentPage, user.getId());
		userIconAttributes(model, userDetails);
		model.addAttribute("redeemCurrentPage", currentPage);
		model.addAttribute("redeemTotalitems", page.getTotalElements());
		model.addAttribute("redeemTotalpages", page.getTotalPages());
		model.addAttribute("redeemTransactions", page.getContent());
		LOGGER.info("User Redeem Transactions listed successfully for page: {}", currentPage);

		return REDEEM_TRANSACTION_VIEW;
	}

}

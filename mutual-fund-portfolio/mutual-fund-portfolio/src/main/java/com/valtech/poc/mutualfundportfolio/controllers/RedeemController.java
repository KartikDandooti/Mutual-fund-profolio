package com.valtech.poc.mutualfundportfolio.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.entities.Transaction;
import com.valtech.poc.mutualfundportfolio.entities.TransactionType;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.models.RedeemModel;
import com.valtech.poc.mutualfundportfolio.services.MutualFundService;
import com.valtech.poc.mutualfundportfolio.services.TransactionService;
import com.valtech.poc.mutualfundportfolio.services.UserSchemeService;
import com.valtech.poc.mutualfundportfolio.services.UserService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/mutualfund")
public class RedeemController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedeemController.class);

	private static final String REDEEM_VIEW = "redeem";
	private static final String MESSAGE = "redeemMessage";

	LocalDateTime date = LocalDateTime.now();

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserSchemeService userSchemeService;
	@Autowired
	private MutualFundService mutualFundService;

	@PostMapping("/redeem")
	public String submitForm(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("redeemModel") RedeemModel redeemModel, RedirectAttributes redirectAttributes, Model model) {
		User user = userService.findUserByUsername(userDetails.getUsername());

		LOGGER.info("Handling redeem request from User: {}", user.getFirstName() + user.getLastName());

		UserScheme userScheme = userSchemeService.findByuserAndScheme(user.getId(), redeemModel.getScheme().getId());
		Transaction transaction = new Transaction(user, redeemModel.getScheme(), redeemModel.getNav(), redeemModel.getAmount(), userScheme.getSchemeUnits(), date, TransactionType.REDEEM);
		try {
			transactionService.createRedeemTransaction(transaction);
			redirectAttributes.addFlashAttribute(MESSAGE, "Congratulations, You have successfully REDEEMED Rs." + redeemModel.getAmount() + " (" + userScheme.getSchemeUnits() + " units) in "
			      + redeemModel.getScheme().getSchemeName() + " scheme.");

			LOGGER.info("User: {} successfully redeemed Rs.{} ({} units) in scheme '{}'", user.getFirstName() + user.getLastName(), redeemModel.getAmount(), userScheme.getSchemeUnits(),
			      redeemModel.getScheme().getSchemeName());
		} catch (IOException | TemplateException | MessagingException | MailException e) {
			redirectAttributes.addFlashAttribute(MESSAGE, "Some error occured, Please try again later.");
		}

		return "redirect:/mutualfund/redeem";
	}

	@GetMapping("/schemeDetails")
	@ResponseBody
	public Map<String, BigDecimal> getSchemeDetails(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int schemeId) {
		MutualFundScheme scheme = mutualFundService.getMutualFundSchemeById(schemeId);
		User user = userService.findUserByUsername(userDetails.getUsername());

		LOGGER.info("Handling request from User: {} to get details for scheme with ID: {}", user.getFirstName() + user.getLastName(), schemeId);

		UserScheme userScheme = userSchemeService.findByuserAndScheme(user.getId(), schemeId);
		Map<String, BigDecimal> schemeDetails = new HashMap<>();
		schemeDetails.put("nav", scheme.getNetAssetValue());
		schemeDetails.put("schemeUnits", userScheme.getSchemeUnits());
		schemeDetails.put("amount", userScheme.getCurrentAmount());

		LOGGER.info("User: {} retrieved details for scheme with ID: {}", user.getFirstName() + user.getLastName(), schemeId);

		return schemeDetails;
	}

	@GetMapping("/redeem")
	public String redeem(@AuthenticationPrincipal UserDetails userDetails, Model model, @ModelAttribute(MESSAGE) String redeemMessage) {
		User user = userService.findUserByUsername(userDetails.getUsername());
		List<UserScheme> userSchemes = userSchemeService.findSchemesByUser(user.getId());
		model.addAttribute("userSchemes", userSchemes);
		model.addAttribute("message", redeemMessage);
		userIconAttributes(model, userDetails);
		LOGGER.info("User: {} accessed the redeem page", userDetails.getUsername());

		return REDEEM_VIEW;
	}

	@GetMapping("/redeem/scheme")
	public String redeemScheme(@RequestParam("schemeId") int schemeId, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		User user = userService.findUserByUsername(userDetails.getUsername());
		userIconAttributes(model, userDetails);
		UserScheme userScheme = userSchemeService.findByuserAndScheme(user.getId(), schemeId);
		model.addAttribute("userScheme", userScheme);

		LOGGER.info("User: {} viewed details for redeeming scheme with ID: {}", user.getFirstName() + user.getLastName(), schemeId);

		return REDEEM_VIEW;
	}

	private void userIconAttributes(Model model, UserDetails userDetails) {
		User user = userService.findUserByUsername(userDetails.getUsername());
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("lastName", user.getLastName());
		model.addAttribute("userId", user.getId());
		LOGGER.info("Passing attributes to user icon to user: {} with ID: {}", user.getFirstName() + user.getLastName(), user.getId());
	}
}
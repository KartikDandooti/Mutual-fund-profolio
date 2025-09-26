package com.valtech.poc.mutualfundportfolio.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.entities.Transaction;
import com.valtech.poc.mutualfundportfolio.entities.TransactionType;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.models.InvestModel;
import com.valtech.poc.mutualfundportfolio.services.MutualFundService;
import com.valtech.poc.mutualfundportfolio.services.TransactionService;
import com.valtech.poc.mutualfundportfolio.services.UserService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/mutualfund")
public class InvestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestController.class);

	private static final String INVEST_VIEW = "invest";
	private static final String INVEST_REDIRECT = "redirect:/mutualfund/invest";
	private static final String MESSAGE = "investMessage";

	LocalDateTime date = LocalDateTime.now();

	@Autowired
	private MutualFundService mutualFundService;
	@Autowired
	private UserService userService;
	@Autowired
	private TransactionService transactionService;

	private void userIconAttributes(Model model, UserDetails userDetails) {
		User user = userService.findUserByUsername(userDetails.getUsername());
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("lastName", user.getLastName());
		model.addAttribute("userId", user.getId());
		LOGGER.info("Passing attributes to user icon to user: {} with ID: {}", user.getFirstName() + user.getLastName(), user.getId());
	}

	@PostMapping("/invest")
	public String submitForm(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("investModel") InvestModel investModel, Model model, RedirectAttributes redirectAttributes) {
		User user = userService.findUserByUsername(userDetails.getUsername());
		Transaction transaction = new Transaction(user, investModel.getScheme(), investModel.getNav(), investModel.getAmount(), investModel.getUnits(), date, TransactionType.INVEST);
		try {
			transactionService.createInvestTransaction(transaction);
			redirectAttributes.addFlashAttribute(MESSAGE,
			      "Congratulations, Your INVESTMENT in " + investModel.getScheme().getSchemeName() + " scheme is successful. You have purchased " + investModel.getUnits() + " units.");

			LOGGER.info("User: {} successfully made an investment in scheme '{}'", userDetails.getUsername(), investModel.getScheme().getSchemeName());
		} catch (IOException | TemplateException | MessagingException | MailException e) {
			redirectAttributes.addFlashAttribute(MESSAGE, "Some error occured, Please try again later.");
			LOGGER.error("Error sending Transaction email for user: {},{}", user.getEmail(), e.getMessage());
		}

		return INVEST_REDIRECT;

	}

	@GetMapping("/schemeNav")
	@ResponseBody
	public BigDecimal getSchemeNav(@RequestParam int schemeId) {
		MutualFundScheme schemeForNav = mutualFundService.getMutualFundSchemeById(schemeId);

		LOGGER.info("NAV retrieved for scheme with ID: {} with Nav value: {}", schemeId, schemeForNav.getNetAssetValue());

		return schemeForNav.getNetAssetValue();
	}

	@GetMapping("/getschemes")
	@ResponseBody
	public List<MutualFundScheme> getSchemes(@RequestParam int mutualFundType) {
		LOGGER.info("Schemes retrieved for mutual fund type: {}", mutualFundType);

		return mutualFundService.getAllSchemesByMutualFundType(mutualFundType);
	}

	@GetMapping("/invest")
	public String invest(@AuthenticationPrincipal UserDetails userDetails, Model model, @ModelAttribute(MESSAGE) String investMessage) {
		LOGGER.info("Handling request from User: {} to invest", userDetails.getUsername());
		userIconAttributes(model, userDetails);
		List<MutualFund> types = mutualFundService.getAllMutualFundTypes();
		model.addAttribute("mutualFundTypes", types);
		model.addAttribute("message", investMessage);

		LOGGER.info("User: {} accessed the invest page", userDetails.getUsername());

		return INVEST_VIEW;
	}

	@GetMapping("/invest/scheme")
	public String investScheme(@RequestParam("schemeId") int schemeId, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		userIconAttributes(model, userDetails);
		LOGGER.info("Handling request from User: {} to view details for scheme with ID: {}", userDetails.getUsername(), schemeId);
		MutualFundScheme scheme = mutualFundService.getMutualFundSchemeById(schemeId);
		model.addAttribute("scheme", scheme);

		LOGGER.info("User: {} viewed details for scheme with ID: {}", userDetails.getUsername(), schemeId);

		return INVEST_VIEW;
	}
}
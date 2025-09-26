package com.valtech.poc.mutualfundportfolio.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.MailException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.valtech.poc.mutualfundportfolio.custom.exceptions.DuplicateEmailException;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.entities.UserScheme;
import com.valtech.poc.mutualfundportfolio.models.UserRegisterModel;
import com.valtech.poc.mutualfundportfolio.services.MutualFundService;
import com.valtech.poc.mutualfundportfolio.services.TransactionService;
import com.valtech.poc.mutualfundportfolio.services.UserSchemeService;
import com.valtech.poc.mutualfundportfolio.services.UserService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@Controller
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private static final String HOME_PAGE_VIEW = "homePage";
	private static final String REGISTER_VIEW = "register";
	private static final String LOGIN_VIEW = "login";
	private static final String LANDING_PAGE_VIEW = "landingPage";
	private static final String EXPLORE_MUTUALFUND_SCHEMES_VIEW = "exploreMutualFunds";
	private static final String MESSAGE = "registrationMessage";
	private static final String REGISTER_REDIRECT = "redirect:/mutualfund/register";
	private static final String ERROR = "error";

	@Autowired
	private UserService userService;
	@Autowired
	private UserSchemeService userSchemeService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MutualFundService mutualFundService;

	private void userIconAttributes(Model model, UserDetails userDetails) {
		User user = userService.findUserByUsername(userDetails.getUsername());
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("lastName", user.getLastName());
		model.addAttribute("userId", user.getId());
		LOGGER.info("Passing attributes to user icon to user: {} with ID: {}", user.getFirstName() + user.getLastName(), user.getId());
	}

	@GetMapping("/mutualfund/explore-mutualfunds")
	public String exploreMutualFunds(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		LOGGER.info("Handling request to explore mutual funds");

		userIconAttributes(model, userDetails);

		LOGGER.info("Mutual funds exploration completed successfully");

		return listByPage(model, userDetails, 1);
	}

	@RequestMapping("/mutualfund/page/{pageNumber}")
	public String listByPage(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable("pageNumber") int currentPage) {
		LOGGER.info("Handling request to list mutual funds on page: {}", currentPage);

		Page<MutualFundScheme> page = mutualFundService.listAllSchemes(currentPage);
		long totalitems = page.getTotalElements();
		int totalpages = page.getTotalPages();
		userIconAttributes(model, userDetails);
		List<MutualFundScheme> listMutualFundSchemes = page.getContent();

		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalitems", totalitems);
		model.addAttribute("totalpages", totalpages);
		model.addAttribute("mutualFundSchemes", listMutualFundSchemes);

		LOGGER.info("Mutual funds listed successfully for page: {}", currentPage);

		return EXPLORE_MUTUALFUND_SCHEMES_VIEW;
	}

	@GetMapping("/mutualfund/home")
	public String userPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Handling request to display user home page");

		User user = userService.findUserByUsername(userDetails.getUsername());
		BigDecimal totalInvestedAmount = userSchemeService.getSumInvestedAmount(user.getId());
		BigDecimal totalReturnAmount = userSchemeService.getSumCurrentAmount(user.getId());
		BigDecimal totalReturnsPercentage = userSchemeService.calculateReturnsPercentage(user.getId());

		userIconAttributes(model, userDetails);
		List<Object[]> pieChartData = transactionService.getTotalAmountByMutualFund(user.getId());
		List<UserScheme> userSchemes = userSchemeService.findSchemesByUser(user.getId());
		model.addAttribute("userSchemes", userSchemes);
		model.addAttribute("pieChartDataSummary", pieChartData);
		model.addAttribute("totalInvestedAmount", totalInvestedAmount);
		model.addAttribute("totalReturnAmount", totalReturnAmount);
		model.addAttribute("totalReturnsPercentage", totalReturnsPercentage);

		LOGGER.info("Home page displayed successfully for User: {}", user.getFirstName() + user.getLastName());

		return HOME_PAGE_VIEW;
	}

	@PostMapping("/mutualfund/register")
	public String processRegsiterForm(@ModelAttribute("userRegisterModel") UserRegisterModel userModel, RedirectAttributes redirectAttributes) {
		LOGGER.info("Handling registration request for user: {} with email: {}", userModel.getFirstName() + userModel.getLastName(), userModel.getEmail());
		User newUser = new User(StringUtils.capitalize(userModel.getFirstName()), StringUtils.capitalize(userModel.getLastName()), userModel.getAge(), userModel.getPhoneNumber(), userModel.getEmail(),
		      passwordEncoder.encode(userModel.getPassword()));
		try {
			userService.createUser(newUser);
			LOGGER.info("creating a new user");
			redirectAttributes.addFlashAttribute(MESSAGE, "User registration Successfull");

		} catch (MessagingException | IOException | TemplateException | MailException e) {
			LOGGER.error("Some error occured, Please try again later:{} ", e);
			redirectAttributes.addFlashAttribute(MESSAGE, "Some error occured, Please try again later.");
		} catch (DuplicateEmailException e) {
			redirectAttributes.addFlashAttribute(MESSAGE, e.getMessage());
		}
		return REGISTER_REDIRECT;
	}

	@GetMapping("/mutualfund/register")
	public String registerForm(@ModelAttribute(MESSAGE) String message, Model model) {
		LOGGER.info("Handling request to display registration form");

		model.addAttribute("message", message);
		LOGGER.info("Registration form displayed");

		return REGISTER_VIEW;
	}

	@GetMapping("/mutualfund/login")
	public String login(@ModelAttribute("passwordChangeMessage") String passwordChangeMessage, Model model) {
		LOGGER.info("Handling request to display login form");

		model.addAttribute("passwordChangeMessage", passwordChangeMessage);

		LOGGER.info("Login form displayed");

		return LOGIN_VIEW;
	}

	@GetMapping("/mutualfund")
	public String landingPage() {
		LOGGER.info("Handling request to display landing page");

		return LANDING_PAGE_VIEW;
	}

	@GetMapping("/error")
	public String error() {
		LOGGER.info("Displaying Error Page");
		return ERROR;
	}
}
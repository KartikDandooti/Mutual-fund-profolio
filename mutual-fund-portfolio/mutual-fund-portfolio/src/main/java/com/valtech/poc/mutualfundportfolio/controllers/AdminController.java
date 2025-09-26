package com.valtech.poc.mutualfundportfolio.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;
import com.valtech.poc.mutualfundportfolio.models.MutualFundSchemeModel;
import com.valtech.poc.mutualfundportfolio.services.MutualFundService;
import com.valtech.poc.mutualfundportfolio.models.MutualFundModel;

@Controller
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	private static final String ADMIN_PAGE_VIEW = "adminPage";
	private static final String SCHEME_FORM_VIEW = "schemesForm";
	private static final String MUTUAL_FUND_FORM_VIEW = "mutualFundsForm";
	private static final String SCHEMES_VIEW = "mutualFundSchemes";

	@Autowired
	private MutualFundService mutualFundService;

	

	@GetMapping("/mutualfund/addscheme")
	public String addNewScheme(Model model) { 
		LOGGER.info("Handling request to add a new scheme");
		
		model.addAttribute("mutualFundTypes", mutualFundService.getAllMutualFundTypes());
		model.addAttribute("scheme", new MutualFundScheme());

		return SCHEME_FORM_VIEW;
	}

	@GetMapping("/mutualfund/addmutualfund")
	public String addNewMutualFund(Model model) {
		LOGGER.info("Handling request to add a new MutualFund type");
		
		model.addAttribute("mutualFund", new MutualFund());

		return MUTUAL_FUND_FORM_VIEW;
	}

	@PostMapping("/mutualfund/editscheme")
	public String updateScheme(@ModelAttribute("mutualFundSchemeModel") MutualFundSchemeModel schemeModel,
			Model model) {
		LOGGER.info("Handling request to update a scheme with ID {}", schemeModel.getId());
		
		mutualFundService.updateMutualFundScheme(new MutualFundScheme(schemeModel.getId(), schemeModel.getSchemeName(),
				schemeModel.getDescription(), schemeModel.getNetAssetValue(), schemeModel.getMutualFund()));
		
		LOGGER.info("Successfully updated scheme with ID {}", schemeModel.getId());

		return "redirect:/mutualfund/admin#schemes";
	}

	@GetMapping("/mutualfund/editscheme")
	public String editScheme(@RequestParam("schemeId") int schemeId, Model model) {
		LOGGER.info("Handling request to edit a scheme with ID {}", schemeId);
		
		model.addAttribute("mutualFundTypes", mutualFundService.getAllMutualFundTypes());
		model.addAttribute("scheme", mutualFundService.getMutualFundSchemeById(schemeId));
		
		LOGGER.info("Successfully retrieved scheme with ID {} for editing", schemeId);

		return SCHEME_FORM_VIEW;
	}

	@PostMapping("/mutualfund/editmutualfund")
	public String updateMutualFund(@ModelAttribute("mutualFundModel") MutualFundModel mutualFundModel, Model model) {
		LOGGER.info("Handling request to update a mutual fund with ID {}", mutualFundModel.getId());
		
		mutualFundService.updateMutualFund(
				new MutualFund(mutualFundModel.getId(), mutualFundModel.getType(), mutualFundModel.getDescription()));
		
		LOGGER.info("Successfully updated mutual fund with ID {}", mutualFundModel.getId());

		return "redirect:/mutualfund/admin";
	}

	@GetMapping("/mutualfund/editmutualfund")
	public String editMutualFund(@RequestParam("mutualFundId") int mutualFundId, Model model) {
		LOGGER.info("Handling request to edit a mutual fund with ID {}", mutualFundId);
		
		MutualFund mutualFund = mutualFundService.getMutualFundById(mutualFundId);
		model.addAttribute("mutualFund", mutualFund);
		
		LOGGER.info("Successfully retrieved mutual fund with ID {} for editing", mutualFundId);

		return MUTUAL_FUND_FORM_VIEW;
	}

	@GetMapping("/mutualfund/schemes")
	public String schemesPage(@RequestParam("mutualFundId") int mutualFundId, Model model) {
		LOGGER.info("Handling request to view schemes for mutual fund with MutualFund type ID: {}", mutualFundId);
		
		model.addAttribute("mutualFundSchemes", mutualFundService.getAllSchemesByMutualFundType(mutualFundId));
		
		LOGGER.info("Successfully retrieved and populated schemes for mutual fund with MutualFund type ID: {}", mutualFundId);

		return SCHEMES_VIEW;
	}

	@GetMapping("/mutualfund/admin")
	public String adminPage(Model model) {
		LOGGER.info("Handling request to view admin page");

		return listByPage(model, 1);
	}

	@RequestMapping("/mutualfund/pages")
	public String listByPage(Model model, @RequestParam("pageNumbers") int currentPage) {
		LOGGER.info("Handling request to list mutual fund schemes for page {}", currentPage);
		
		Page<MutualFundScheme> page = mutualFundService.listAllSchemes(currentPage);
		long totalitems = page.getTotalElements();
		int totalpages = page.getTotalPages();
		List<MutualFundScheme> listMutualFundSchemes = page.getContent();
		model.addAttribute("mutualFunds", mutualFundService.getAllMutualFundTypes());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalitems", totalitems);
		model.addAttribute("totalpages", totalpages);
		model.addAttribute("mutualFundSchemes", listMutualFundSchemes);
		
		LOGGER.info("Successfully listed mutual fund schemes for page {}", currentPage);

		return ADMIN_PAGE_VIEW;
	}
}
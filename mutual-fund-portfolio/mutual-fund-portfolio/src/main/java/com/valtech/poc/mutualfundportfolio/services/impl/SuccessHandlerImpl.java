package com.valtech.poc.mutualfundportfolio.services.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SuccessHandlerImpl implements AuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuccessHandlerImpl.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		var authorities = authentication.getAuthorities();
		var role = authorities.stream().map(r -> r.getAuthority()).findFirst().orElse("");
		
		switch (role) {
		case "USER":
			response.sendRedirect("/mutualfund/home");
			LOGGER.info("Logged in as: {}", role);
			break;
		case "ADMIN":
			response.sendRedirect("/mutualfund/admin");
			LOGGER.info("Logged in as: {}", role);
			break;
		default:
			response.sendRedirect("mutualfund/error");
			LOGGER.error("Invalid Role: {}", role);
			break;
		}
	}
}

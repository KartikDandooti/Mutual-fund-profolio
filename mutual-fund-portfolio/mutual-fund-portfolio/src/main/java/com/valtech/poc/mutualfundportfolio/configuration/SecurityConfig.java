package com.valtech.poc.mutualfundportfolio.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.valtech.poc.mutualfundportfolio.services.impl.SuccessHandlerImpl;
import com.valtech.poc.mutualfundportfolio.services.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		LOGGER.debug("Configuring security filter chain...");

		httpSecurity.csrf(c -> c.disable())
				.authorizeHttpRequests(request -> request.requestMatchers("/mutualfund/admin").hasAuthority("ADMIN")
						.requestMatchers("/mutualfund/home", "/mutualfund/invest", "/mutualfund/redeem",
								"/mutualfund/view-profile", "/mutualfund/invest-transactions",
								"/mutualfund/redeem-transactions")
						.hasAuthority("USER")
						.requestMatchers("/mutualfund", "/mutualfund/register", "/css/**", "/error").permitAll()
						.anyRequest().authenticated());

		httpSecurity.formLogin(formLogin -> {
			LOGGER.debug("Configuring Login Page...");
			formLogin.loginPage("/mutualfund/login").loginProcessingUrl("/mutualfund/login")
					.successHandler(new SuccessHandlerImpl()).permitAll();
		}).logout(logout -> logout.invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/mutualfund/login?logout")
				.permitAll())

				.sessionManagement(session -> session.invalidSessionUrl("/login?timeout"));

		LOGGER.debug("Security filter chain configured successfully.");

		return httpSecurity.build();
	}

	@Bean
	private static PasswordEncoder passwordEncoder() {
		LOGGER.info("Encrypting Password");

		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		LOGGER.debug("Configuring authentication manager...");
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		LOGGER.debug("Authentication manager configured successfully.");
	}
}
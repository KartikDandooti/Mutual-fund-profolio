package com.valtech.poc.mutualfundportfolio.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.valtech.poc.mutualfundportfolio.entities.User;
import com.valtech.poc.mutualfundportfolio.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("Loading User Details of:{} ", username);

		User user = userRepository.findByportfolioNumber(username);
		if (user == null) {
			LOGGER.error("User not found for Username:{} ", username);
			throw new UsernameNotFoundException("User not found");
		}

		return new UserDetailsImpl(user);
	}
}

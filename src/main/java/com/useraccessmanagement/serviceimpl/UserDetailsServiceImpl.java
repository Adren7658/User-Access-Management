package com.useraccessmanagement.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.useraccessmanagement.jwt.CustomUserDetails;
import com.useraccessmanagement.model.UserManagement;
import com.useraccessmanagement.repository.UserManagementRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);
	
	@Autowired
	private UserManagementRepository userManagementRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		logger.debug("Entering in loadUserByUsername Method...");
		UserManagement userManagement = userManagementRepository.findByEmail(username).get();
		if (userManagement == null) {
			logger.error("Username not found: " + username);
			throw new UsernameNotFoundException("could not found user..!!");
		}
		logger.info("User Authenticated Successfully..!!!");
		return new CustomUserDetails(userManagement);
	}

}

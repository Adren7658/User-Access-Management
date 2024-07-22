package com.useraccessmanagement.service;

import java.util.List;
import java.util.Optional;

import com.useraccessmanagement.exception.BusinessException;
import com.useraccessmanagement.model.UserManagement;

import jakarta.servlet.http.HttpServletRequest;

public interface UserManagementService {
	
	boolean emailExists(String email);

	List<UserManagement> getAllUsers();

	Optional<UserManagement> getUserById(Long id);

	UserManagement createUser(UserManagement user) throws BusinessException;

	void deleteUser(Long id);
	
	String fetchRoleNameFromToken(HttpServletRequest request);
}

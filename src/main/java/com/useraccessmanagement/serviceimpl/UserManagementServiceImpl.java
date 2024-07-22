package com.useraccessmanagement.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.useraccessmanagement.exception.BusinessException;
import com.useraccessmanagement.model.UserManagement;
import com.useraccessmanagement.repository.UserManagementRepository;
import com.useraccessmanagement.service.UserManagementService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private final UserManagementRepository userManagementRepository;
	private final PasswordEncoder passwordEncoder;

	public UserManagementServiceImpl(UserManagementRepository userManagementRepository, PasswordEncoder passwordEncoder) {
		this.userManagementRepository = userManagementRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public boolean emailExists(String email) {
		return userManagementRepository.findByEmail(email).isPresent();
	}

	@Override
	public List<UserManagement> getAllUsers() {
		return userManagementRepository.findAll();
	}

	@Override
	public Optional<UserManagement> getUserById(Long id) {
		return userManagementRepository.findById(id);
	}

	@Override
    public UserManagement createUser(UserManagement user) throws BusinessException {
        if (user == null) {
            throw new BusinessException("User object cannot be null.");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BusinessException("Password is required.");
        }

        if (emailExists(user.getEmail())) {
            throw new BusinessException("Email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        
        return userManagementRepository.save(user);
    }

	@Override
	public void deleteUser(Long id) {
		userManagementRepository.deleteById(id);
	}

	@Override
	public String fetchRoleNameFromToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }
        token = token.substring(7);
		return token;
	}
}

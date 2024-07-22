package com.useraccessmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.useraccessmanagement.model.UserManagement;

@Repository
public interface UserManagementRepository extends JpaRepository<UserManagement, Long> {
    
	Optional<UserManagement> findByEmail(String email);
    
}

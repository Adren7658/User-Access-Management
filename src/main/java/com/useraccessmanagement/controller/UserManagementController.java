package com.useraccessmanagement.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.useraccessmanagement.dto.JwtResponseDTO;
import com.useraccessmanagement.dto.ResponseDTO;
import com.useraccessmanagement.dto.UserAuthDTO;
import com.useraccessmanagement.exception.BusinessException;
import com.useraccessmanagement.jwt.JwtService;
import com.useraccessmanagement.model.UserManagement;
import com.useraccessmanagement.service.UserManagementService;
import com.useraccessmanagement.utility.Constants;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserManagementController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserManagementService userManagementService;

    public UserManagementController(JwtService jwtService, AuthenticationManager authenticationManager,
                                    UserManagementService userManagementService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userManagementService = userManagementService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> authenticateAndGetToken(@RequestBody UserAuthDTO userAuthDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthDTO.getUsername(), userAuthDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                String accessToken = jwtService.generateToken(userAuthDTO.getUsername());
                JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
                jwtResponseDTO.setAccessToken(accessToken);

                ResponseDTO responseDto = new ResponseDTO(Constants.SUCCESS_MESSAGE, Constants.AUTH_SUCCESS_MESSAGE, jwtResponseDTO);
                return ResponseEntity.ok().body(responseDto);
            } else {
                ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.INVALID_USER_REQUEST, null);
                return ResponseEntity.status(Constants.HTTP_STATUS_UNAUTHORIZED).body(responseDto);
            }
        } catch (UsernameNotFoundException ex) {
            ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.INVALID_USER_REQUEST, null);
            return ResponseEntity.status(Constants.HTTP_STATUS_UNAUTHORIZED).body(responseDto);
        } catch (Exception ex) {
            ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.ERROR_PROCESSING_REQUEST, null);
            return ResponseEntity.status(Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        try {
            List<UserManagement> users = userManagementService.getAllUsers();
            ResponseDTO responseDto = new ResponseDTO(Constants.SUCCESS_MESSAGE, Constants.USERS_RETRIEVED_MESSAGE, users);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception ex) {
            ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.ERROR_RETRIEVING_USERS, null);
            return ResponseEntity.status(Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
        try {
            Optional<UserManagement> user = userManagementService.getUserById(id);
            if (user.isPresent()) {
                ResponseDTO responseDto = new ResponseDTO(Constants.SUCCESS_MESSAGE, Constants.USER_RETRIEVED_MESSAGE, user.get());
                return ResponseEntity.ok().body(responseDto);
            } else {
                ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.USER_NOT_FOUND, null);
                return ResponseEntity.status(Constants.HTTP_STATUS_NOT_FOUND).body(responseDto);
            }
        } catch (Exception ex) {
            ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.ERROR_RETRIEVING_USER, null);
            return ResponseEntity.status(Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserManagement user, HttpServletRequest request) {
        try {
            String roleName = jwtService.extractRole(userManagementService.fetchRoleNameFromToken(request));
            if (!Constants.ADMIN_ROLE.equalsIgnoreCase(roleName)) {
                ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.UNAUTHORIZED_ADMIN_ONLY + "create users.", null);
                return ResponseEntity.status(Constants.HTTP_STATUS_FORBIDDEN).body(responseDto);
            }

            UserManagement createdUser = userManagementService.createUser(user);
            ResponseDTO responseDto = new ResponseDTO(Constants.SUCCESS_MESSAGE, Constants.USER_CREATED_MESSAGE, createdUser);
            return ResponseEntity.status(Constants.HTTP_STATUS_CREATED).body(responseDto);
        } catch (BusinessException ex) {
            ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, ex.getMessage(), null);
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception ex) {
            ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.ERROR_CREATING_USER, null);
            return ResponseEntity.status(Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            String roleName = jwtService.extractRole(userManagementService.fetchRoleNameFromToken(request));
            if (!Constants.ADMIN_ROLE.equalsIgnoreCase(roleName)) {
                ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.UNAUTHORIZED_ADMIN_ONLY + "delete users.", null);
                return ResponseEntity.status(Constants.HTTP_STATUS_FORBIDDEN).body(responseDto);
            }
            userManagementService.deleteUser(id);
            ResponseDTO responseDto = new ResponseDTO(Constants.SUCCESS_MESSAGE, Constants.USER_DELETED_MESSAGE, null);
            return ResponseEntity.status(Constants.HTTP_STATUS_CREATED).body(responseDto);
        } catch (Exception ex) {
            ResponseDTO responseDto = new ResponseDTO(Constants.ERROR_MESSAGE, Constants.ERROR_DELETING_USER, null);
            return ResponseEntity.status(Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}

package com.useraccessmanagement.utility;

public class Constants {
	
	public static final String SUCCESS_MESSAGE = "success";
    public static final String ERROR_MESSAGE = "error";
    public static final String AUTH_SUCCESS_MESSAGE = "Authentication successful";
    public static final String USERS_RETRIEVED_MESSAGE = "Users retrieved successfully";
    public static final String USER_RETRIEVED_MESSAGE = "User retrieved successfully";
    public static final String USER_CREATED_MESSAGE = "User created successfully";
    public static final String USER_DELETED_MESSAGE = "User deleted successfully";
    
    public static final String INVALID_USER_REQUEST = "Invalid user request..!!";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String UNAUTHORIZED_ADMIN_ONLY = "Unauthorized: Only admins can ";
    public static final String ERROR_PROCESSING_REQUEST = "An error occurred while processing the request.";
    public static final String ERROR_RETRIEVING_USERS = "An error occurred while retrieving users.";
    public static final String ERROR_RETRIEVING_USER = "An error occurred while retrieving the user.";
    public static final String ERROR_CREATING_USER = "An error occurred while creating the user.";
    public static final String ERROR_DELETING_USER = "An error occurred while deleting the user.";
    
    // HTTP Status Codes
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_FORBIDDEN = 403;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_STATUS_CREATED = 201;
    
    // Roles
    public static final String ADMIN_ROLE = "ADMIN";

}

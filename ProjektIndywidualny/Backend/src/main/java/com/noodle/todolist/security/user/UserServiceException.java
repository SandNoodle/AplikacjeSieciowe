package com.noodle.todolist.security.user;

public class UserServiceException extends RuntimeException {
	public UserServiceException(String errorMessage) {
		super(errorMessage);
	}
	
	public UserServiceException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}

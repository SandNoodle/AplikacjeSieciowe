package com.noodle.todolist.api.list;

public class ListServiceException extends RuntimeException {
	public ListServiceException(String errorMessage) {
		super(errorMessage);
	}
	
	public ListServiceException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}

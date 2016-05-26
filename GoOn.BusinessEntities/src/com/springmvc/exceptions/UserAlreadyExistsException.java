package com.springmvc.exceptions;

public class UserAlreadyExistsException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String username)
	{
		System.out.println("El usuario " + username + " ya existe!!!!");
	}
}

package com.demo.data.event;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationExceptionEvent extends AbstractAuthenticationFailureEvent {

	private static final long serialVersionUID = 1L;

	public CustomAuthenticationExceptionEvent(Authentication authentication, AuthenticationException exception) {
		super(authentication, exception);
	}
}

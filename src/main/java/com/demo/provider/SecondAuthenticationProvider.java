package com.demo.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.demo.data.token.LoginAuthenticationToken;
import com.demo.exception.CustomAuthenticationException;

@Component
public class SecondAuthenticationProvider implements AuthenticationProvider {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LoginAuthenticationToken token = (LoginAuthenticationToken) authentication;
		String principal = (String) token.getPrincipal();
		String credentials = (String) token.getCredentials();
		if ("demo".equals(principal) && "demo123".equals(credentials)) {
			return new LoginAuthenticationToken(principal, credentials, null);
		}

		try {
			throwsNewEx();
		} catch (Exception e) {
			throw new CustomAuthenticationException("custom login fail!(SecondAuthenticationProvider)", -100);
		}

		return null;

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return LoginAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public void throwsNewEx() throws Exception {
		throw new Exception();
	}

}

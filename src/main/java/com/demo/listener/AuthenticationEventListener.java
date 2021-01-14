package com.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		Authentication authentication = event.getAuthentication();
		logger.debug("authentication=" + authentication.getClass());
		if (event instanceof AuthenticationSuccessEvent) {
			logger.debug("get login suc from AuthenticationSuccessEvent");
		}

		if (event instanceof AbstractAuthenticationFailureEvent) {
			logger.debug("get login fail from " + event.getClass().getName());

		}

	}

}

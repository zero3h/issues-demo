package com.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {

		if (event instanceof AbstractAuthenticationFailureEvent) {
			logger.debug("-------------------");
			logger.debug("-------------------");
			logger.debug("-------------------");
			logger.debug("custom exception enter listener!! event is {}", event.getClass().getName());
			logger.debug("-------------------");
			logger.debug("-------------------");
			logger.debug("-------------------");

		}

	}

}

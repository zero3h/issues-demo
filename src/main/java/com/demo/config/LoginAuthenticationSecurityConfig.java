package com.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.filter.LoginAuthenticationFilter;
import com.demo.handler.FailureHandler;
import com.demo.handler.SuccessHandler;
import com.demo.provider.LoginAuthenticationProvider;

@Configuration
public class LoginAuthenticationSecurityConfig
		extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SuccessHandler successHandler;
	@Autowired
	private FailureHandler failureHandler;
	@Autowired
	private LoginAuthenticationProvider loginAuthenticationProvider;

	public LoginAuthenticationSecurityConfig() {
		logger.info("CustomLoginAuthenticationSecurityConfig loading ...");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		LoginAuthenticationFilter filter = new LoginAuthenticationFilter("/front/login/normal",
				RequestMethod.POST.name());

		filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		filter.setAuthenticationSuccessHandler(successHandler);
		filter.setAuthenticationFailureHandler(failureHandler);

		http.authorizeRequests().antMatchers("/front/login/normal").permitAll().and()
				.authenticationProvider(loginAuthenticationProvider)
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	}
}

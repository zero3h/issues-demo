/*
 * Title: 	  TRS 身份服务器
 * Copyright: Copyright (c) 2004-2021, TRS信息技术股份有限公司. All rights reserved.
 * License:   see the license file.
 * Company:   TRS信息技术股份有限公司(www.trs.com.cn)
 * 
 * Created: guo.jiahua@2021-1-12 15:39:55
 */
package com.demo.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.data.event.CustomAuthenticationExceptionEvent;
import com.demo.exception.CustomAuthenticationException;
import com.demo.filter.LoginAuthenticationFilter;
import com.demo.handler.FailureHandler;
import com.demo.provider.LoginAuthenticationProvider;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private LoginAuthenticationProvider loginAuthenticationProvider;

	//
	// FIXME @link{AuthenticationEventListener } can't catch the
	// event when I add this code @link{@Component }.
	/**
	 * =====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING
	 * FIXME @link{AuthenticationEventListener } can't catch the event {@link }
	 * when the Provider was Autowired.
	 */

	@Autowired
	private FailureHandler failureHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

		// custom login url
		http.authorizeRequests().antMatchers("/front/login/**").permitAll();

		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().csrf().disable()
				.apply(new SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
					@Override
					public void configure(HttpSecurity builder) throws Exception {
						// custom login filter, to catch parameters(username and
						// password)
						LoginAuthenticationFilter filter = new LoginAuthenticationFilter("/front/login/normal",
								HttpMethod.GET.name());
						filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
						// custom login fail handler
						filter.setAuthenticationFailureHandler(failureHandler);
						// custom login authentication provider
						builder.authenticationProvider(loginAuthenticationProvider).addFilterBefore(filter,
								UsernamePasswordAuthenticationFilter.class);
						super.configure(builder);
					}
				}).and().logout().permitAll();
		logger.info("WebSecurityConfig loading successfully.");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpg", "/*.ico");
	}

	/**
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationEventPublisher(applicationContext.getBean(DefaultAuthenticationEventPublisher.class));
		super.configure(auth);
	}

	/**
	 * add custom exception {@link CustomAuthenticationException} to
	 * {@link DefaultAuthenticationEventPublisher}
	 * 
	 * 
	 * @param publisher
	 * @return
	 */
	@Bean
	public DefaultAuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publisher) {
		DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher = new DefaultAuthenticationEventPublisher(
				publisher);
		Properties additionalExceptionMappings = new Properties();
		additionalExceptionMappings.put(CustomAuthenticationException.class.getName(),
				CustomAuthenticationExceptionEvent.class.getName());
		defaultAuthenticationEventPublisher.setAdditionalExceptionMappings(additionalExceptionMappings);

		return defaultAuthenticationEventPublisher;
	}
}

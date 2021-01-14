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
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.demo.data.event.CustomAuthenticationExceptionEvent;
import com.demo.exception.CustomAuthenticationException;
import com.demo.intercepter.GlobalAuthenticationIntercepter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private GlobalAuthenticationIntercepter globalAuthenticationIntercepter;
	@Autowired
	private LoginAuthenticationSecurityConfig customLoginAuthenticationSecurityConfig;
	@Autowired
	private SecondAuthenticationSecurityConfig secondAuthenticationSecurityConfig;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// no session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

		http.authorizeRequests().antMatchers("/front/login/**").permitAll();

		// setting
		http.httpBasic().and()
				// to authenticated
				.authorizeRequests().anyRequest().authenticated()
				// global handler
				.and().exceptionHandling().authenticationEntryPoint(globalAuthenticationIntercepter).and()
				// no csrf
				.csrf().disable()
				// custom login config
				.apply(customLoginAuthenticationSecurityConfig)
				// custom second login config
				// =====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING=====WARNING
				// FIXME @link{AuthenticationEventListener } can't catch the
				// event when I add this code.
				.and().apply(secondAuthenticationSecurityConfig)
				// custom logout url
				.and().logout().logoutUrl("/front/logout");
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
	 * setAdditionalExceptionMappings to DefaultAuthenticationEventPublisher
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

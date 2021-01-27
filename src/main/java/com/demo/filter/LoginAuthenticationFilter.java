package com.demo.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.demo.data.bo.LoginBo;
import com.demo.data.token.LoginAuthenticationToken;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public LoginAuthenticationFilter(String loginUrl, String httpMethod) {
		super(new AntPathRequestMatcher(loginUrl, httpMethod));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		LoginBo bo = new LoginBo();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(bo);
		binder.bind(request);

		if (logger.isDebugEnabled()) {
			logger.debug("equest params,{}", bo);
		}

		// assemble idsToken
		LoginAuthenticationToken authRequest = new LoginAuthenticationToken(bo.getUsername(), bo.getPassword());

		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * 设置身份认证的详情信息
	 */
	private void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

}

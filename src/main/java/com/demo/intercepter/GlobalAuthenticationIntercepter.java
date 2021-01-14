package com.demo.intercepter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * security未有登录权限时拦截器 <BR>
 * 
 * @author TRS信息技术有限公司
 * @since guo.jiahua@2018年7月13日
 */
@Component
public class GlobalAuthenticationIntercepter implements AuthenticationEntryPoint {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public GlobalAuthenticationIntercepter() {
		logger.info("GlobalAuthenticationEntryPoint loading ...");
	}

	/**
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse,
	 *      org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.debug(" *** UnauthorizedEntryPoint.commence: " + request.getRequestURI());
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		String msg = authException.toString();

		response.getWriter()
				.write("{\"code\":-1,\"msg\":\"login fail!(GlobalAuthenticationIntercepter," + msg + ")\"}");
	}

}

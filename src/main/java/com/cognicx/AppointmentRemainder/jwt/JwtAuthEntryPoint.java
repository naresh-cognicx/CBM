package com.cognicx.AppointmentRemainder.jwt;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.service.UserService;
 
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	UserService userService;
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String exceptionCalssName = exception.getClass().getSimpleName();
		String subException = exception.getCause() != null ? exception.getCause().getClass().getSimpleName():"";
		logger.error("Unauthorized error. Message - {}", exception.getMessage());
		if (exception instanceof UsernameNotFoundException) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
		} else if (exception instanceof InternalAuthenticationServiceException && "LockedException".equalsIgnoreCase(subException)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
		} else if (exception instanceof InternalAuthenticationServiceException && "DisabledException".equalsIgnoreCase(subException)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
		} else if (exception instanceof BadCredentialsException) {
			Object userName = request.getAttribute("tempUserName");
			//Update login Attempt
			Object[] loginInfo = new Object[10];
			loginInfo[0] = userName;
			loginInfo[1] = true;
			loginInfo[3] = 1;
			loginInfo[5] = "System";
			loginInfo[6] = "System";
			try {
				userService.saveOrUpdateLoginDetails(true, loginInfo);
			} catch (Exception e) {
				logger.error("Exception::JwtAuthEntryPoint.Class:commence()", e);
			}
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ApplicationConstant.BADCREDENTIALS_EXCEPTION);
		} else if ("InsufficientAuthenticationException".equalsIgnoreCase(exceptionCalssName)
				|| "AccessDeniedException".equalsIgnoreCase(exceptionCalssName)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
		}
	}
}

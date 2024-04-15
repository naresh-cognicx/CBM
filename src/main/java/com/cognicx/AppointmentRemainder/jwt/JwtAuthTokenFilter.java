package com.cognicx.AppointmentRemainder.jwt;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cognicx.AppointmentRemainder.service.UserService;
import com.cognicx.AppointmentRemainder.Dto.TokenDetailsDto;

/**
 * This filter does the token validation for all the API calls. Those APIs
 * skipped for security in WebSecurityConfig, token validation is done to
 * provide access if token is passed. Otherwise IP whitelisting is mandated
 * (atleast one IP to be whitelisted in the configuration) and the requestor IP
 * is validated against the whitelisted IPs to provide the access.
 * 
 * @author Hinduja
 *
 */
public class JwtAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtProvider tokenProvider;

	@Autowired
	private UserService userService;

	// @Value("${origin.url}")
	private String originUrl;

	@Value("${app.whitelist.ip}")
	private String whiteListedIPs = "";

	@Value("${app.auth.enabled}")
	private boolean authEnabled;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			if (authEnabled) {
				String jwt = getJwt(request);
				if (null != jwt) {
					Object[] tokenObj = tokenProvider.validateJwtTokenObj(jwt, response);
					boolean tokenFlag = (boolean) tokenObj[0];
					logger.info("Token Flag Status:"+tokenFlag);
					if (tokenFlag) {
						String username = tokenProvider.getUserNameFromJwtToken(jwt);
						TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
						tokenDetailsDto.setEmployeeId(username);
						tokenDetailsDto.setToken(jwt);
						boolean tokenExist = userService.checkExistingTokenDetails(tokenDetailsDto);
						if (tokenExist && username != null && !username.isEmpty()) {
							logger.info("Token Exist and User Name Available");
							UserDetails userDetails = tokenProvider.getUserDetailsFromJwtToken(jwt);
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}else {
							logger.info("Token Not Exist");
						}
					} else {
						response = (HttpServletResponse) tokenObj[1];
					}
					filterChain.doFilter(request, response);
				} else {
					List<String> whiteListedIPList = new ArrayList<String>();
					if (!StringUtils.isEmpty(whiteListedIPs)) {
						whiteListedIPList = Arrays.asList(whiteListedIPs.split(","));
					}
					List<String> allowedUrls = Arrays.asList("/api/token/authenticate", "/user/usersList", "/api/login",
							"/api/logout", "/api/forgetpassword/request", "/api/forgetpassword/reset",
							"/api/forgetpassword/validateOTP", "/v2/api-docs", "/configuration", "/swagger",
							"/webjars","/api/changepassword","/campaign/updateCallDetail");
					if (allowedUrls.contains(request.getServletPath())) {
						filterChain.doFilter(request, response);
					} else if (whiteListedIPList.isEmpty()) {
						logger.error("No IP whitelisted for access");
						((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
								"No IP whitelisted for access");
					} else if (!(StringUtils.isEmpty(request.getRemoteAddr()))
							&& !(whiteListedIPList.contains(request.getRemoteAddr()))) {
						logger.error("Access blocked for IP : {}", request.getRemoteAddr());
						((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
								"Host not allowed to access the resource");
					} else {
						logger.debug("IP {} permitted", request.getRemoteAddr());
						filterChain.doFilter(request, response);
					}
				}

			} else {
				filterChain.doFilter(request, response);
			}
			/*
			 * else { ((HttpServletResponse)
			 * response).sendError(HttpServletResponse.SC_PARTIAL_CONTENT,
			 * "JWT token should not be empty."); }
			 */
		} catch (Exception e) {
			logger.error("Can NOT set user authentication -> Message: {}", e);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}

	}

	private String getJwt(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ", "");
		}

		return null;
	}

	private final String URL = originUrl;

	private String setAccessControlAllowOrigin(HttpServletRequest request) {
		// if (URL.equals(request.getHeader("Origin"))) {
		return URL;
		// }
		// return "";
	}
}

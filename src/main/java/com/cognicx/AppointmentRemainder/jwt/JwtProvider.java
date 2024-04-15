package com.cognicx.AppointmentRemainder.jwt;

import java.io.IOException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.cognicx.AppointmentRemainder.service.impl.UserPrinciple;
import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.util.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpiration}")
	private int jwtExpiration;

	@Autowired
	private UserInfo userInfo;

	public String generateJwtToken(Authentication authentication) {

		UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

		return Jwts.builder().setId(String.valueOf(userPrincipal.getAutogenUsersId()))
				.setSubject(userPrincipal.getEmployeeId()).claim("UserDetails", userPrincipal).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public UserDetails getUserDetailsFromJwtToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		LinkedHashMap<?, ?> claimMap = (LinkedHashMap<?, ?>) claims.get("UserDetails");
		UserDto userDto = new UserDto();
		userDto.setAutogenUsersId(new BigInteger(String.valueOf(claimMap.get("autogenUsersId"))));
		userDto.setEmployeeId((String) claimMap.get("employeeId"));
		userDto.setFirstName((String) claimMap.get("firstName"));
		userDto.setLastName((String) claimMap.get("lastName"));
		userDto.setStatus((String) claimMap.get("status"));
		userDto.setEmail((String) claimMap.get("email"));
		userDto.setMobileNumber((String) claimMap.get("mobileNumber"));
		
		List<GrantedAuthority> authorities1 = new ArrayList<>();
		List<LinkedHashMap<?, ?>> authoritiesMap = (List<LinkedHashMap<?, ?>>) claimMap.get("authorities");
		for (LinkedHashMap<?, ?> string : authoritiesMap) {
			authorities1.add(new SimpleGrantedAuthority((String) string.get("authority")));
		}
		userDto.setAuthorities(authorities1);

		userInfo.setEmployeeId(userDto.getEmployeeId());
		userInfo.setAutogenUsersId(userDto.getAutogenUsersId());
		userInfo.setFirstName(userDto.getFirstName());
		userInfo.setLastName(userDto.getLastName());
		userInfo.setRolesName(userDto.getAuthorities().toString());

		UserDetails userDetails = UserPrinciple.build(new UserDto(userDto));
		return userDetails;
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature -> Message: {} ", e);
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token -> Message: {}", e);
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token -> Message: {}", e);
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token -> Message: {}", e);
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message: {}", e);
		}

		return false;
	}
	public Object[] validateJwtTokenObj(String authToken, HttpServletResponse response) throws IOException {
		Object[] jwtTokenResult = new Object[2];
		String message = "";
		jwtTokenResult[1] = response;
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			jwtTokenResult[0] = true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature -> Message: {} ", e.getMessage());
			message = "Invalid JWT signature -> Message: {}";
			throw new SignatureException(message);
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token -> Message: {}", e.getMessage());
			message = "Invalid JWT token -> Message: {}";
			throw new MalformedJwtException(message);
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token -> Message: {}", e.getMessage());
			message = "Expired JWT token -> Message: {}";
			throw new ExpiredJwtException(null, null, message);
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token -> Message: {}", e.getMessage());
			message = "Unsupported JWT token -> Message: {}";
			throw new UnsupportedJwtException(message);
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message: {}", e.getMessage());
			message = "JWT claims string is empty -> Message: {}";
			throw new IllegalArgumentException(message);
		} catch (AccessDeniedException e) {
			jwtTokenResult[0] = false;
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
					"Expired JWT token -> Message: {}");
			jwtTokenResult[1] = response;
			logger.error("Expired JWT token -> Message: {}", e.getMessage());
			message = "Expired JWT token -> Message: {}";
			throw new AccessDeniedException(message);
		}

		return jwtTokenResult;
	}
}
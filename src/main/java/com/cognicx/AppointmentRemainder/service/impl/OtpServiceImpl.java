package com.cognicx.AppointmentRemainder.service.impl;

import java.security.NoSuchAlgorithmException;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.cognicx.AppointmentRemainder.controller.UserAuthentication;
import com.cognicx.AppointmentRemainder.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService {

	private static final Integer EXPIRE_MINS = 5;
	private LoadingCache<String, Integer> otpCache;
	
	private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

	public OtpServiceImpl() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	// This method is used to push the opt number against Key. Rewrite the OTP if it
	// exists
	// Using user id as key
	public int generateOTP(String key) {
		Random random = null;
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			logger.error("Exception::OtpServiceImpl.Class:generateOTP()", e);
		}
		int otp = 0;
		if(random != null) {
		otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		}
		return otp;
	}

	// This method is used to return the OPT number against Key->Key values is
	// username
	public int getOtp(String key) {
		try {
			return otpCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}

	// This method is used to clear the OTP catched already
	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}
}

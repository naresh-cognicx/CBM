package com.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class RetryTest {
public static void main(String[] args) {
	String encodedPassword=new BCryptPasswordEncoder().encode("Test1234");
	System.out.println(encodedPassword);
}
}

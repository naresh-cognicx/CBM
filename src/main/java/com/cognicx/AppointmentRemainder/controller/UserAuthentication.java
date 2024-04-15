package com.cognicx.AppointmentRemainder.controller;

import java.math.BigInteger;


import java.security.PrivateKey;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.directory.DirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cognicx.AppointmentRemainder.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognicx.AppointmentRemainder.configuration.ActiveDirectory;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
//import com.ison.Survey;
//import com.ison.UserLogin;
import com.cognicx.AppointmentRemainder.constant.StatusCodeConstants;
import com.cognicx.AppointmentRemainder.Request.AuthenticateRequest;
import com.cognicx.AppointmentRemainder.Request.ChangePasswordRequest;
import com.cognicx.AppointmentRemainder.Request.ForgetPasswordRequest;
import com.cognicx.AppointmentRemainder.Request.LoginForm;
import com.cognicx.AppointmentRemainder.Request.LogoutRequest;
import com.cognicx.AppointmentRemainder.Request.ResetPasswordRequest;
import com.cognicx.AppointmentRemainder.Request.ValidateOtpRequest;
import com.cognicx.AppointmentRemainder.jwt.JwtProvider;
import com.cognicx.AppointmentRemainder.mail.EmailService;
import com.cognicx.AppointmentRemainder.response.AuthenticateResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.JwtResponse;
import com.cognicx.AppointmentRemainder.response.ModuleListResponse;
import com.cognicx.AppointmentRemainder.service.OtpService;

import com.cognicx.AppointmentRemainder.service.UserService;
import com.cognicx.AppointmentRemainder.service.impl.UserDetailsServiceImpl;
import com.cognicx.AppointmentRemainder.service.impl.UserPrinciple;
import com.cognicx.AppointmentRemainder.Dto.TokenDetailsDto;
import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.util.Encryption;
import com.cognicx.AppointmentRemainder.util.UserInfo;
import com.cognicx.AppointmentRemainder.service.RolesService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserAuthentication {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    RolesService rolesService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    OtpService otpService;

    @Autowired
    UserInfo userInfo;

    @Autowired
    JwtProvider jwtProvider;

    @Value("${app.jwtExpiration}")
    private int jwtExpiration;

    @Value("${app.ldap.enabled}")
    private boolean ldapEnabled;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    EmailService emailService;

    @Value("${app.ldap.url}")
    private String ldapUrl;

    @Value("${app.ldap.userdomain}")
    private String ldapUserDomain;

    @Value("${app.user.superadmin}")
    private String superAdmin;

    private static final Logger logger = LoggerFactory.getLogger(UserAuthentication.class);

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> authenticateUser(@Valid @RequestBody LoginForm loginRequest,
                                                            HttpServletRequest request) throws Exception {

        //		logger.info("Request Body:"+loginRequest.toString());
        logger.info("userName : {}", loginRequest.getUsername());

        GenericResponse genericResponse = new GenericResponse();
        request.setAttribute("tempUserName", loginRequest.getUsername());
        PrivateKey privateKey = Encryption.loadPrivateKeyFromFile("key_pvt");
        String password = Encryption.decrypt(loginRequest.getPassword(), privateKey);
        logger.info("Invoking User Name Password Auth for the Password :" + password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), password);


        String sourceIP = "";
        if (request != null) {
            sourceIP = request.getHeader("X-FORWARDED-FOR");
            if (StringUtils.isEmpty(sourceIP)) {
                sourceIP = request.getRemoteAddr();
            }
        }
        logger.debug("userName : {}", loginRequest.getUsername());

        try {

            if (ldapEnabled && !(superAdmin.equalsIgnoreCase(loginRequest.getUsername()))) {

                if (!userDetailsService.existsByApprovedUsername(loginRequest.getUsername())) {
                    throw new UsernameNotFoundException(ApplicationConstant.USERNAMENOTFOUNDEXCEPTION);
                }
                logger.debug("ldapUrl : {} ", ldapUrl);
                logger.debug("ldapUserDomain : {}", ldapUserDomain);

                String userName = loginRequest.getUsername();
                if (userName.indexOf("\\") == -1) {
                    userName = ldapUserDomain.concat("\\").concat(userName);
                    logger.debug("loginUserName : {}", userName);
                }

                DirContext con = ActiveDirectory.authenticate(userName, password, ldapUrl);

                logger.info("UserAuthentication.Class::authenticateUser()" +
                        "loginRequest.getUsername() :" + loginRequest.getUsername() +
                        "ldapUrl :" + ldapUrl);

                logger.debug("Login IsAuthenticated {}", (null != con));

                if (null != con) {
                    con.close();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
                    logger.debug("login userDetails present : {}",
                            (null != userDetails) ? userDetails.getUsername() : "No");
                    authentication = new UsernamePasswordAuthenticationToken(userDetails,
                            authentication.getCredentials().toString(), userDetails.getAuthorities());
                    logger.debug("login Authentication Principal type for ldap : {} ",
                            authentication.getPrincipal().getClass());
                } else {
                    throw new BadCredentialsException(ApplicationConstant.BADCREDENTIALS_EXCEPTION);
                }
            } else {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), password));
            }

            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Security Context Holder Updated");
            userInfo.setEmployeeId(userPrinciple.getEmployeeId());

            /** Disabled below piece of code for testing
             UserDto userDto = userService.getModuleScreenDet(userPrinciple.getRolesList());
             List<ModuleListResponse> moduleListResponse = null;
             if (null != userDto && userDto.getResultObj() != null) {
             moduleListResponse = new ArrayList<>();
             moduleListResponse = (List<ModuleListResponse>) userDto.getResultObj();
             }

             userDto = userService.getApprovedDomainDetails(String.valueOf(userPrinciple.getAutogenUsersId()),
             userPrinciple.getAutogenUsersDetailsId());
             */

            String jwtToken = jwtProvider.generateJwtToken(authentication);
            Date date = new Date((new Date()).getTime() + jwtExpiration);
            long dateTime = date.getTime();
            Timestamp expiryDate = new Timestamp(dateTime);
            TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
            tokenDetailsDto.setEmployeeId(userPrinciple.getEmployeeId());
            tokenDetailsDto.setExpiryDate(expiryDate);
            tokenDetailsDto.setExpirySeconds(jwtExpiration);
            tokenDetailsDto.setToken(jwtToken);
            tokenDetailsDto.setRefreshToken("");
            tokenDetailsDto.setStatus("ACTIVE");
            tokenDetailsDto.setCreatedBy("System");
            userService.saveTokenDetails(tokenDetailsDto);

            genericResponse.setStatus(StatusCodeConstants.SUCCESS);
            genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
            genericResponse.setMessage("Logged in Successfully");
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(jwtToken);
            jwtResponse.setUserName(userPrinciple.getEmployeeId());
            jwtResponse.setRoles(userPrinciple.getAuthorities().toString());
            jwtResponse.setExpiryDate(expiryDate);
            jwtResponse.setExpirySeconds(String.valueOf(jwtExpiration));
            jwtResponse.setUserGroupName(userPrinciple.getUsergroupName());
            //	jwtResponse.setModulescreens(moduleListResponse);
            jwtResponse.setFirstName(userPrinciple.getFirstName());
            jwtResponse.setLastName(userPrinciple.getLastName());
            jwtResponse.setMobileNumber(userPrinciple.getMobileNumber());
            //	jwtResponse.setDomain(userDto.getDomain());
            //	jwtResponse.setBusinessUnit(userDto.getBusinessUnit());
            jwtResponse.setLdapEnabled(ldapEnabled);
            jwtResponse.setFeatureResponse(userManagementService.getFeatures());
            genericResponse.setValue(new JwtResponse(jwtResponse));

            Object[] loginInfo = new Object[10];
            loginInfo[0] = userPrinciple.getEmployeeId();
            loginInfo[1] = true;
            loginInfo[3] = BigInteger.ZERO;
            loginInfo[5] = "System";
            loginInfo[6] = "System";
            loginInfo[7] = sourceIP;
            loginInfo[8] = userPrinciple.getEmail();
            try {
                userService.saveOrUpdateLoginDetails(true, loginInfo);
            } catch (Exception e) {
                logger.error("Exception::UserAuthentication.Class:authenticateUser()", e);
            }

        } catch (Exception e) {
            logger.error("Exception::authenticateUser()", e);
            genericResponse.setStatus(StatusCodeConstants.FAILURE);
            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
            genericResponse.setMessage(e.getMessage());
            logger.debug("Logging the invalid attempt");
            Object[] loginInfo = new Object[10];
            loginInfo[0] = loginRequest.getUsername();
            loginInfo[1] = true;
            loginInfo[3] = BigInteger.ONE;
            loginInfo[4] = e.getMessage();
            loginInfo[5] = "System";
            loginInfo[6] = "System";
            loginInfo[7] = sourceIP;
            try {
                userService.saveOrUpdateLoginDetails(true, loginInfo);
            } catch (Exception ex) {
                logger.error("Exception::saveOrUpdateLoginDetails()", ex);
            }
        }
        return ResponseEntity.ok(new GenericResponse(genericResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse> logout(@Valid @RequestBody LogoutRequest logoutRequest,
                                                  HttpServletRequest request) throws Exception {

        boolean logoutStatus = false;
        // Update login Attempt
        Object[] loginInfo = new Object[10];
        loginInfo[0] = logoutRequest.getEmployeeId();
        loginInfo[1] = false;
        loginInfo[2] = true;
        loginInfo[3] = true;
        loginInfo[5] = "System";
        loginInfo[6] = logoutRequest.getEmployeeId();
        try {
            Object[] object = userService.saveOrUpdateLoginDetails(false, loginInfo);
            if (object != null && object[0] != null) {
                logoutStatus = (boolean) object[0];
            }

        } catch (Exception e) {
            logger.error("Exception::UserAuthentication.Class:logout()", e);
        }

        if (logoutStatus) {
            TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
            tokenDetailsDto.setToken(logoutRequest.getToken());
            tokenDetailsDto.setEmployeeId(logoutRequest.getEmployeeId());
            tokenDetailsDto = userService.updateTokenStatus(tokenDetailsDto);
            logoutStatus = tokenDetailsDto.isFlag();
        }
        GenericResponse genericResponse = new GenericResponse();
        if (logoutStatus) {
            genericResponse.setStatus(StatusCodeConstants.SUCCESS);
            genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
            genericResponse.setMessage(logoutRequest.getEmployeeId() + " Logged Out Successfully!!!");
            genericResponse.setValue("");
        }
        return ResponseEntity.ok(new GenericResponse(genericResponse));
    }

    @PostMapping("/token/authenticate")
    public ResponseEntity<GenericResponse> tokenAuthenticate(
            @Valid @RequestBody AuthenticateRequest authenticateRequest, HttpServletResponse response)
            throws Exception {
        GenericResponse genericResponse = new GenericResponse();
        String token = getToken(authenticateRequest.getToken());
        if (token != null && !token.isEmpty()) {
            Object[] tokenObj = jwtProvider.validateJwtTokenObj(token, response);
            boolean tokenFlag = (boolean) tokenObj[0];
            if (tokenFlag) {
                String username = jwtProvider.getUserNameFromJwtToken(token);
                TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
                tokenDetailsDto.setEmployeeId(username);
                tokenDetailsDto.setToken(token);
                boolean tokenExist = userService.checkExistingTokenDetails(tokenDetailsDto);
                if (tokenExist && username != null && !username.isEmpty()) {
                    UserDetails userDetails = jwtProvider.getUserDetailsFromJwtToken(token);
                    UserPrinciple userPrinciple = (UserPrinciple) userDetails;
                    if (userPrinciple != null) {
                        AuthenticateResponse authenticateResponse = new AuthenticateResponse(
                                userPrinciple.getAutogenUsersId(), userPrinciple.getEmail(),
                                userPrinciple.getEmployeeId(), userPrinciple.getFirstName(),
                                userPrinciple.getLastName(), userPrinciple.getMobileNumber(),
                                userPrinciple.getStatus());
                        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
                        genericResponse.setMessage("Token verification success!!!");
                        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
                        genericResponse.setValue(authenticateResponse);
                    }
                } else {
                    genericResponse.setStatus(StatusCodeConstants.FAILURE);
                    genericResponse.setMessage("Invalid token. Verification failure!!!");
                    genericResponse.setError(StatusCodeConstants.FAILURE_STR);

                }
            }

        } else {
            genericResponse.setStatus(StatusCodeConstants.FAILURE);
            genericResponse.setMessage("Invalid token. Verification failure!!!");
            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
        }
        return ResponseEntity.ok(new GenericResponse(genericResponse));

    }

    private String getToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.replace("Bearer ", "");
        }
        return null;
    }

    @PostMapping(path = "/forgetpassword/request")
    public ResponseEntity<GenericResponse> forgetPasswordRequest(
            @Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest, HttpServletResponse response)
            throws Exception {
        GenericResponse genericResponse = new GenericResponse();
        try {
            UserPrinciple userPrincipal = userDetailsService
                    .loadUserDetailByUsername(forgetPasswordRequest.getEmployeeId());
            if (userPrincipal != null) {
                if (userPrincipal.getEmail() == null || userPrincipal.getEmail().isEmpty()) {
                    genericResponse.setStatus(StatusCodeConstants.FAILURE);
                    genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                    genericResponse.setMessage("Please contact the admin for the password.");
                    genericResponse.setValue(null);
                } else {
                    int otpNumber = otpService.generateOTP(forgetPasswordRequest.getEmployeeId());
                    String mailBody = "Hi " + forgetPasswordRequest.getEmployeeId() + "," + ""
                            + "Password reset otp is " + otpNumber;
                    emailService.sendSimpleMessage(userPrincipal.getEmail(),
                            "User Password Reset OTP::" + forgetPasswordRequest.getEmployeeId(), mailBody);
                    logger.debug("OTP::{}::{}", forgetPasswordRequest.getEmployeeId(), otpNumber);
                    genericResponse.setStatus(StatusCodeConstants.SUCCESS);
                    genericResponse.setMessage(
                            "OTP sent to registered Email Id. Please enter OTP and Validate to reset password.");
                    genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
                    genericResponse.setValue(null);
                }
            }
        } catch (Exception e) {
            genericResponse.setStatus(StatusCodeConstants.FAILURE);
            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
            genericResponse.setMessage("Forget password failure. Please contact admin.");
            genericResponse.setValue(null);
            logger.error("Exception::UserAuthentication.Class:forgetPasswordRequest()", e);
        }
        return ResponseEntity.ok(new GenericResponse(genericResponse));

    }

    @PostMapping(path = "/forgetpassword/validateOTP")
    public ResponseEntity<GenericResponse> validateOTP(@Valid @RequestBody ValidateOtpRequest validateOTP,
                                                       HttpServletResponse response) throws Exception {
        GenericResponse genericResponse = new GenericResponse();
        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";
        try {
            logger.debug(" Otp Number : " + validateOTP.getOtpNumber());
            // Validate the Otp
            if (validateOTP.getOtpNumber() >= 0) {
                int serverOtp = otpService.getOtp(validateOTP.getEmployeeId());
                if (serverOtp > 0) {
                    if (validateOTP.getOtpNumber() == serverOtp) {
                        otpService.clearOTP(validateOTP.getEmployeeId());
                        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
                        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
                        genericResponse.setMessage(SUCCESS);
                        genericResponse.setValue(null);
                    } else {
                        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
                        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
                        genericResponse.setMessage(SUCCESS);
                        genericResponse.setValue(null);
                    }
                } else {
                    genericResponse.setStatus(StatusCodeConstants.FAILURE);
                    genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                    genericResponse.setMessage(FAIL);
                    genericResponse.setValue(null);
                }
            }
        } catch (Exception e) {
            genericResponse.setStatus(StatusCodeConstants.FAILURE);
            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
            genericResponse.setMessage("OTP validation failure. Please contact admin.");
            genericResponse.setValue(null);
            logger.error("Exception::UserAuthentication.Class:validateOTP()", e);
        }
        return ResponseEntity.ok(new GenericResponse(genericResponse));

    }

    @PostMapping(path = "/forgetpassword/reset")
    public ResponseEntity<GenericResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
                                                         HttpServletResponse response) throws Exception {
        GenericResponse genericResponse = new GenericResponse();
        try {
            if (resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
                UserDto userDto = new UserDto();
                userDto.setEmployeeId(resetPasswordRequest.getEmployeeId());
                userDto.setPassword(encoder.encode(resetPasswordRequest.getPassword()));
                boolean resetStatus = userService.resetPassword(userDto);
                if (resetStatus) {
                    genericResponse.setStatus(StatusCodeConstants.SUCCESS);
                    genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
                    genericResponse.setMessage("Password reset successfully.");
                    genericResponse.setValue(null);
                } else {
                    genericResponse.setStatus(StatusCodeConstants.FAILURE);
                    genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                    genericResponse.setMessage("Reset password failure. Please contact admin.");
                    genericResponse.setValue(null);
                    logger.error("Exception::Reset password failure. Please contact admin.");
                }
            } else {
                genericResponse.setStatus(StatusCodeConstants.FAILURE);
                genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                genericResponse.setMessage("The Confirm password confirmation does not match.");
                genericResponse.setValue(null);
            }
        } catch (Exception e) {
            genericResponse.setStatus(StatusCodeConstants.FAILURE);
            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
            genericResponse.setMessage("Reset password failure. Please contact admin.");
            genericResponse.setValue(null);
            logger.error("Exception::UserAuthentication.Class:resetPassword()", e);
        }
        return ResponseEntity.ok(new GenericResponse(genericResponse));
    }


    @PostMapping("/changepassword")
    public ResponseEntity<GenericResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,HttpServletRequest header) throws Exception {
        GenericResponse genericResponse = new GenericResponse();
        String userName;
        String token;
        try {
            token = header.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
        if (jwtProvider.validateJwtToken(token)) {
            userName = jwtProvider.getUserNameFromJwtToken(token);
            if (userName.equalsIgnoreCase(changePasswordRequest.getUserId())) {
                logger.info("Change Password Request for User Id : "+userName);
                logger.info("Change Password Request New Password:" + changePasswordRequest.getNewPassword());
//                logger.info("Change Password Request Old Password:" + changePasswordRequest.getOldPassword());
                logger.info("Change Password Request Confirm Password:" + changePasswordRequest.getConfirmPassword());
                if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
                    UserDto userDto = new UserDto();
                    userDto.setEmployeeId(changePasswordRequest.getUserId());
                    userDto.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));

                    String encodedPassword = userService.getEncodedPassword(userDto);
//                    boolean isExistPassMatches = encoder.matches(changePasswordRequest.getOldPassword(), encodedPassword);
//                    if (isExistPassMatches) {
//                        if (!changePasswordRequest.getOldPassword().equalsIgnoreCase(changePasswordRequest.getNewPassword())) {
                            boolean resetStatus = userService.changePassword(userDto);
                            if (resetStatus) {
                                genericResponse.setStatus(StatusCodeConstants.SUCCESS);
                                genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
                                genericResponse.setMessage("Change Password successfully.");
                                genericResponse.setValue(null);
                            } else {
                                genericResponse.setStatus(StatusCodeConstants.FAILURE);
                                genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                                genericResponse.setMessage("Old Password and New Password are same");
                                genericResponse.setValue(null);
                                logger.error("Exception::Change password failure. Please contact admin.");
                            }
//                        } else {
//                            genericResponse.setStatus(StatusCodeConstants.FAILURE);
//                            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
//                            genericResponse.setMessage("Old Password and New Password are same");
//                            genericResponse.setValue(null);
//                            logger.error("Old Password and New Password are same");
//                        }
                } else {
                    genericResponse.setStatus(StatusCodeConstants.FAILURE);
                    genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                    genericResponse.setMessage("The Confirm password confirmation does not match.");
                    genericResponse.setValue(null);
                }
            } else {
                genericResponse.setStatus(StatusCodeConstants.FAILURE);
                genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                genericResponse.setMessage("Invalid User Id");
                genericResponse.setValue(null);
            }
        }else {
            genericResponse.setStatus(StatusCodeConstants.FAILURE);
            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
            genericResponse.setMessage("Invalid Token");
            genericResponse.setValue(null);
        }

        } catch (Exception e) {
            genericResponse.setStatus(StatusCodeConstants.FAILURE);
            genericResponse.setError(StatusCodeConstants.FAILURE_STR);
            genericResponse.setMessage("Change password failure. Please contact admin.");
            genericResponse.setValue(null);
            logger.error("Exception::UserAuthentication.Class:changePassword()", e);
        }
        return ResponseEntity.ok(new GenericResponse(genericResponse));
    }
}


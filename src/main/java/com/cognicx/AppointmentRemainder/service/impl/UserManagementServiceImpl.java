package com.cognicx.AppointmentRemainder.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.cognicx.AppointmentRemainder.response.FeatureResponse;
import com.cognicx.AppointmentRemainder.util.Encryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.Request.AgentDetail;
import com.cognicx.AppointmentRemainder.Request.AgentRealTimeDashboard;
import com.cognicx.AppointmentRemainder.Request.AvailAgentDetail;
import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.dao.UserManagementDao;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.UserManagementService;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    UserManagementDao userManagementDao;
    private Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    @Override
    public ResponseEntity<GenericResponse> createUser(UserManagementDetRequest userDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            String userKey = userManagementDao.createUser(userDetRequest);
            if (userKey != null) {
                genericResponse.setStatus(200);
                genericResponse.setValue("Success");
                genericResponse.setMessage("user created successfully, user key:  " + userKey);
            } else {
                genericResponse.setStatus(400);
                genericResponse.setValue("Failure");
                genericResponse.setMessage("Error occured while creating user");
            }
        } catch (Exception e) {
            logger.error("Error in UserManagementServiceImpl::createUser " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("Error occured while creating User");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> getUserDetail(String userGroup) {

        GenericResponse genericResponse = new GenericResponse();
        List<UserManagementDetRequest> userDetList = null;
        try {
            userDetList = getUserDetList(userGroup);
            genericResponse.setStatus(200);
            genericResponse.setValue(userDetList);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in UserManagementServiceImpl::getUserDetail " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> getUserDetail() {

        GenericResponse genericResponse = new GenericResponse();
        List<UserManagementDetRequest> userDetList = null;
        try {
            userDetList = getUserDetList();
            genericResponse.setStatus(200);
            genericResponse.setValue(userDetList);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in UserManagementServiceImpl::getUserDetail " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<GenericResponse> updateUser(UserManagementDetRequest userDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        boolean isUpdated = false;
        try {
            if (userDetRequest.getPassword() == null || userDetRequest.getPassword().isEmpty()) {
                isUpdated = userManagementDao.updateUser(userDetRequest);
            } else {
                isUpdated = userManagementDao.updateUserByPassword(userDetRequest);
            }
            if (isUpdated) {
                genericResponse.setStatus(200);
                genericResponse.setValue("Success");
                genericResponse.setMessage("user updated successfully");
            } else {
                genericResponse.setStatus(400);
                genericResponse.setValue("Failure");
                genericResponse.setMessage("Error occured while updating user");
            }
        } catch (Exception e) {
            logger.error("Error in UserManagementServiceImpl::updateUser " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("Error occured while updating user");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }


    public List<UserManagementDetRequest> getUserDetList(String userGroup) throws Exception {
        List<UserManagementDetRequest> userDetList;
        userDetList = new ArrayList<>();
        List<Object[]> userDetObjList = userManagementDao.getUserDetail(userGroup);
        if (userDetObjList != null && !userDetObjList.isEmpty()) {
            for (Object[] obj : userDetObjList) {
                UserManagementDetRequest userDetRequest = new UserManagementDetRequest();
                userDetRequest.setUserKey(String.valueOf(obj[0]));
                userDetRequest.setFirstName(String.valueOf(obj[1]));
                userDetRequest.setLastName(String.valueOf(obj[2]));
                userDetRequest.setEmailId(String.valueOf(obj[3]));
                userDetRequest.setMobNum(String.valueOf(obj[4]));
                userDetRequest.setUserId(String.valueOf(obj[5]));
                userDetRequest.setPassword(String.valueOf(obj[6]));
                userDetRequest.setRole(String.valueOf(obj[7]));
                userDetRequest.setPbxExtn(String.valueOf(obj[8]));
                userDetRequest.setSkillSet(String.valueOf(obj[9]));

                String agentDet = String.valueOf(obj[10]);
                if (agentDet != null) {
                    userDetRequest.setAgent(agentDet);
                    String[] arrAgent = agentDet.split("\\,");
                    List<AgentDetail> agentDetailList = new ArrayList<>();
                    for (String userId : arrAgent) {
                        List<Object[]> agentDetObjList = userManagementDao.getAgentDetail(userId);
                        if (agentDetObjList != null && !agentDetObjList.isEmpty())
                            for (Object[] agentObj : agentDetObjList) {
                                AgentDetail agentDetail = new AgentDetail();
                                agentDetail.setFirstName(String.valueOf(agentObj[0]));
                                agentDetail.setLastName(String.valueOf(agentObj[1]));
                                agentDetail.setUserId(String.valueOf(agentObj[2]));
                                agentDetailList.add(agentDetail);
                            }
                    }
                    userDetRequest.setAgentDetails(agentDetailList);
                }
                userDetRequest.setUserGroup(String.valueOf(obj[11]));
                userDetRequest.setStatus(String.valueOf(obj[12]));
                userDetList.add(userDetRequest);
                logger.info("user Details :" + userDetRequest.toString());
            }
        }
        return userDetList;
    }

    @Override
    public List<UserManagementDetRequest> getUserDetList() throws Exception {
        List<UserManagementDetRequest> userDetList;
        userDetList = new ArrayList<>();
        List<Object[]> userDetObjList = userManagementDao.getUserDetail();
        if (userDetObjList != null && !userDetObjList.isEmpty()) {
            for (Object[] obj : userDetObjList) {
                UserManagementDetRequest userDetRequest = new UserManagementDetRequest();
                userDetRequest.setUserKey(String.valueOf(obj[0]));
                userDetRequest.setFirstName(String.valueOf(obj[1]));
                userDetRequest.setLastName(String.valueOf(obj[2]));
                userDetRequest.setEmailId(String.valueOf(obj[3]));
                userDetRequest.setMobNum(String.valueOf(obj[4]));
                userDetRequest.setUserId(String.valueOf(obj[5]));

                userDetRequest.setPassword(String.valueOf(obj[6]));
                userDetRequest.setRole(String.valueOf(obj[7]));
                userDetRequest.setPbxExtn(String.valueOf(obj[8]));
                userDetRequest.setSkillSet(String.valueOf(obj[9]));
                String agentDet = String.valueOf(obj[10]);
                if (agentDet != null) {
                    userDetRequest.setAgent(agentDet);
                    String[] arrAgent = agentDet.split("\\,");
                    List<AgentDetail> agentDetailList = new ArrayList<>();
                    for (String userId : arrAgent) {
                        List<Object[]> agentDetObjList = userManagementDao.getAgentDetail(userId);
                        if (agentDetObjList != null && !agentDetObjList.isEmpty())
                            for (Object[] agentObj : agentDetObjList) {
                                AgentDetail agentDetail = new AgentDetail();
                                agentDetail.setFirstName(String.valueOf(agentObj[0]));
                                agentDetail.setLastName(String.valueOf(agentObj[1]));
                                agentDetail.setUserId(String.valueOf(agentObj[2]));
                                agentDetailList.add(agentDetail);
                            }
                    }
                    userDetRequest.setAgentDetails(agentDetailList);
                }
                userDetRequest.setUserGroup(String.valueOf(obj[11]));
                userDetRequest.setStatus(String.valueOf(obj[12]));
                userDetList.add(userDetRequest);
                logger.info("user Details :" + userDetRequest.toString());
            }
        }
        return userDetList;
    }


    @Override
    public ResponseEntity<GenericResponse> getAvailAgents() {

        GenericResponse genericResponse = new GenericResponse();
        List<AvailAgentDetail> availAgentList = null;
        try {
            availAgentList = getAvailAgentList();
            genericResponse.setStatus(200);
            genericResponse.setValue(availAgentList);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            StringWriter str = new StringWriter();
            e.printStackTrace(new PrintWriter(str));
            logger.error("Error in UserManagementServiceImpl::get Avail Agent " + str.toString());
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }


    public List<AvailAgentDetail> getAvailAgentList() {
        List<AvailAgentDetail> availAgentList;
        availAgentList = new ArrayList<>();
        List<Object[]> agentObjList = userManagementDao.getAvailAgent();
        if (agentObjList != null && !agentObjList.isEmpty()) {

            for (Object[] obj : agentObjList) {
                AvailAgentDetail agentDetail = new AvailAgentDetail();
                agentDetail.setFirstName(String.valueOf(obj[0]));
                agentDetail.setLastName(String.valueOf(obj[1]));
                agentDetail.setUserId(String.valueOf(obj[2]));
                agentDetail.setAgent(String.valueOf(obj[3]));
                availAgentList.add(agentDetail);
            }
            logger.info("Available Details :" + availAgentList.toString());
        }
        return availAgentList;
    }

    @Override
    public ResponseEntity<GenericResponse> getRoleDetail() {

        GenericResponse genericResponse = new GenericResponse();
        List<Map<String, String>> availRoles = null;
        try {
            availRoles = getAvailRole();
            genericResponse.setStatus(200);
            genericResponse.setValue(availRoles);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            StringWriter str = new StringWriter();
            e.printStackTrace(new PrintWriter(str));
            logger.error("Error in UserManagementServiceImpl::get role detail " + str.toString());
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }


    private List<Map<String, String>> getAvailRole() {
        Map<String, String> rolemap = null;
        List<Map<String, String>> rolesMapList = new ArrayList<>();
        try {

            List<Object[]> roleDetails = userManagementDao.getRoleDetail();
            if (roleDetails != null && !roleDetails.isEmpty()) {
                for (Object[] obj : roleDetails) {
                    rolemap = new HashMap<>();
                    rolemap.put("role_id", String.valueOf(obj[0]));
                    rolemap.put("role", String.valueOf(obj[1]));
                    rolesMapList.add(rolemap);
                }
            }
            logger.info("Roles Map List :" + rolesMapList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rolesMapList;
    }

    @Override
    public ResponseEntity<GenericResponse> validateUserId(UserManagementDetRequest userDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            boolean isValidated = userManagementDao.validateUserId(userDetRequest);
            genericResponse.setStatus(200);
            genericResponse.setValue(isValidated);
            genericResponse.setMessage("Validation done");
        } catch (Exception e) {
            logger.error("Error in CampaignServiceImpl::Validate User Id " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue(true);
            genericResponse.setMessage("Error occured Validating Details");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> validateUserExtn(UserManagementDetRequest userDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            boolean isValidated = userManagementDao.validateUserExtn(userDetRequest);
            genericResponse.setStatus(200);
            genericResponse.setValue(isValidated);
            genericResponse.setMessage("Validation done");
        } catch (Exception e) {
            logger.error("Error in CampaignServiceImpl::Validate User Extn " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue(true);
            genericResponse.setMessage("Error occured Validating Details");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> validateUserEmail(UserManagementDetRequest userDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            boolean isValidated = userManagementDao.validateUserEmail(userDetRequest);
            genericResponse.setStatus(200);
            genericResponse.setValue(isValidated);
            genericResponse.setMessage("Validation done");
        } catch (Exception e) {
            logger.error("Error in CampaignServiceImpl::Validate User Email " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue(true);
            genericResponse.setMessage("Error occured Validating User Email");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> validateUserPhone(UserManagementDetRequest userDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            boolean isValidated = userManagementDao.validateUserPhone(userDetRequest);
            genericResponse.setStatus(200);
            genericResponse.setValue(isValidated);
            genericResponse.setMessage("Validation done");
        } catch (Exception e) {
            logger.error("Error in CampaignServiceImpl::Validate User phone " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue(true);
            genericResponse.setMessage("Error occured Validating Details");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> getAgentRealTimeDashboard() {

        GenericResponse genericResponse = new GenericResponse();
        List<AgentRealTimeDashboard> agentRTdetailReq = null;
        try {
            agentRTdetailReq = agentRTdetails();
            genericResponse.setStatus(200);
            genericResponse.setValue(agentRTdetailReq);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in UserManagementServiceImpl::getUserDetail " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public FeatureResponse getFeatures() {
        return userManagementDao.getFeatures();
    }

    //    public List<AgentRealTimeDashboard> agentRTdetails() throws Exception {
//		List<AgentRealTimeDashboard> listAgentRT=null;
//		try {
//			listAgentRT = new ArrayList<>();
//			boolean isLogin=false;
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//			List<Object[]> agentRTObjList = userManagementDao.getRTAgentDetail();
//			if (agentRTObjList != null && !agentRTObjList.isEmpty()) {
//				for (Object[] obj : agentRTObjList) {
//					AgentRealTimeDashboard agentRTDet= new AgentRealTimeDashboard();
//					agentRTDet.setUserId(String.valueOf(obj[0]));
//					agentRTDet.setCurrentStatus(String.valueOf(obj[1]));
//					agentRTDet.setDevice(String.valueOf(obj[2]));
//					isLogin=(boolean)obj[3];
//					agentRTDet.setLogin(isLogin);
//					String recUpdateTime=String.valueOf(obj[4]);
//					String loginTime=String.valueOf(obj[5]);
//					Date dtloginTime=sdf.parse(loginTime);
//					if(isLogin) {
//						agentRTDet.setActiveloginDuration(getDuration(dtloginTime));
//					}else {
//						agentRTDet.setActiveloginDuration("NA");
//					}
//					Date updateTime=sdf.parse(recUpdateTime);
//					agentRTDet.setCurrentDuration(getDuration(updateTime));
//					agentRTDet.setLastLogoutTime(String.valueOf(obj[6]));
//					listAgentRT.add(agentRTDet);
//					agentRTDet.setShortBreak("0");
//					agentRTDet.setAftercallwork("0");
//					agentRTDet.setNotready("0");
//				}
//				logger.info("Available Details :" + listAgentRT.toString());
//			}
//		}catch(Exception e) {
//			StringWriter str = new StringWriter();
//			e.printStackTrace(new PrintWriter(str));
//			logger.error("Error in UserManagementServiceImpl::agentRT detail " + str.toString());
//		}
//		return listAgentRT;
//	}
    public List<AgentRealTimeDashboard> agentRTdetails() throws Exception {
        List<AgentRealTimeDashboard> listAgentRT = null;
        try {
            listAgentRT = new ArrayList<>();
            boolean isLogin = false;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            List<Object[]> agentRTObjList = userManagementDao.getRTAgentDetail();
            if (agentRTObjList != null && !agentRTObjList.isEmpty()) {
                for (Object[] obj : agentRTObjList) {
                    AgentRealTimeDashboard agentRTDet = new AgentRealTimeDashboard();
                    agentRTDet.setUserId(String.valueOf(obj[0]));
                    agentRTDet.setCurrentStatus(String.valueOf(obj[1]));
                    agentRTDet.setDevice(String.valueOf(obj[2]));
                    isLogin = (boolean) obj[3];
                    agentRTDet.setLogin(isLogin);
                    String recUpdateTime = String.valueOf(obj[4]);
                    String loginTime = String.valueOf(obj[5]);
                    Date dtloginTime = sdf.parse(loginTime);
                    agentRTDet.setLoginTime(String.valueOf(obj[5]));
                    if (isLogin) {
                        agentRTDet.setActiveloginDuration(getDuration(dtloginTime));
                    } else {
                        agentRTDet.setActiveloginDuration("NA");
                    }
                    Date updateTime = sdf.parse(recUpdateTime);
                    agentRTDet.setCurrentDuration(getDuration(updateTime));
                    agentRTDet.setLastLogoutTime(String.valueOf(obj[6]));

                    listAgentRT.add(agentRTDet);

                    /** Currently setting this value as Static, Once the API is ready, it needs to be changed to dynamic */
                    agentRTDet.setShortBreak("30");
                    agentRTDet.setAftercallwork("30");
                    agentRTDet.setNotready("0");
                    agentRTDet.setAvgHandlingTime("0");
                    agentRTDet.setAvgTalkTime("0");
                    agentRTDet.setCallsAbondend("0");
                    agentRTDet.setCallsAnswered("0");
                    agentRTDet.setCallsOffered("0");
                    agentRTDet.setStaffTime("0");
                    agentRTDet.setCampiagnSkillset("0");

                }
                logger.info("Available Details :" + listAgentRT.toString());
//            for (Object[] obj : agentRTObjList) {
//                AgentRealTimeDashboard agentRTDet= new AgentRealTimeDashboard();
//                agentRTDet.setUserId(String.valueOf(obj[0]));
//                agentRTDet.setCurrentStatus((String.valueOf(obj[1])));
//                agentRTDet.setDevice(String.valueOf(obj[2]));
//                isLogin=(boolean)obj[3];
//                agentRTDet.setLogin(isLogin);
//                String recUpdateTime=String.valueOf(obj[4]);
//                String loginTime=String.valueOf(obj[5]);
//                Date dtloginTime=sdf.parse(loginTime);
//                agentRTDet.setLoginTime(String.valueOf(obj[5]));
//                if(isLogin) {
//                    agentRTDet.setActiveloginDuration(getDuration(dtloginTime));
//                }else {
//                    agentRTDet.setActiveloginDuration("NA");
//                }
//                Date updateTime=sdf.parse(recUpdateTime);
//                agentRTDet.setCurrentDuration(getDuration(updateTime));
//                agentRTDet.setLastLogoutTime(String.valueOf(obj[6]));
//
//                listAgentRT.add(agentRTDet);
//
//                /** Currently setting this value as Static, Once the API is ready, it needs to be changed to dynamic */
//                agentRTDet.setShortBreak("30");
//                agentRTDet.setAftercallwork("30");
//                agentRTDet.setNotready("0");
//            }
//            logger.info("Available Details :" + listAgentRT.toString());
            }
        } catch (Exception e) {
            StringWriter str = new StringWriter();
            e.printStackTrace(new PrintWriter(str));
            logger.error("Error in UserManagementServiceImpl::agentRT detail " + str.toString());
        }
        return listAgentRT;
    }


    private String getDuration(Date date) {
        String remDuration = null;
        try {
            long lngCurrTime = System.currentTimeMillis();
            long lngupdTime = date.getTime();
            long remainngDurationinMillis = lngCurrTime - lngupdTime;
            long longdiffinSec = TimeUnit.MILLISECONDS.toSeconds(remainngDurationinMillis);
            int intRemDuration = (int) longdiffinSec;
            int inthours = intRemDuration / 3600;
            int intminutes = (intRemDuration % 3600) / 60;
            int intseconds = intRemDuration % 60;

            remDuration = getWholeValue(inthours) + ":" + getWholeValue(intminutes) + ":" + getWholeValue(intseconds);
        } catch (Exception e) {
            StringWriter str = new StringWriter();
            e.printStackTrace(new PrintWriter(str));
            logger.error("Error in UserManagementServiceImpl::agentRT detail " + str.toString());
        }
        return remDuration;
    }

    private String getWholeValue(int data) {
        return data > 9 ? String.valueOf(data) : "0" + String.valueOf(data);
    }
}

package com.cognicx.AppointmentRemainder.dao.impl;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cognicx.AppointmentRemainder.response.FeatureResponse;
import com.cognicx.AppointmentRemainder.util.LicenseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.CampaignQueryConstant;
import com.cognicx.AppointmentRemainder.constant.UserManagementQueryConstant;
import com.cognicx.AppointmentRemainder.dao.UserManagementDao;
import com.cognicx.AppointmentRemainder.model.Roles;


@Repository("UserManagementDao")
@Transactional
public class UserManagementDaoImpl implements UserManagementDao {

    private final Logger logger = LoggerFactory.getLogger(UserManagementDaoImpl.class);

    @PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
    public EntityManager firstEntityManager;

    @Autowired
    private final LicenseUtil licenseUtil;

    public UserManagementDaoImpl(LicenseUtil licenseUtil) {
        this.licenseUtil = licenseUtil;
    }

    @Value("${license.key}")
    private String licensekey;

    @Override
    public String createUser(UserManagementDetRequest userDetRequest) throws Exception {
        String userKey = null;
        boolean isInserted;
        int insertVal;
        try {
            int idValue = getUserKey();
            if (idValue > 9)
                userKey = "U_" + String.valueOf(idValue);
            else
                userKey = "U_0" + String.valueOf(idValue);
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.INSERT_USER_DET);
            queryObj.setParameter("userKey", userKey);
            queryObj.setParameter("FirstName", userDetRequest.getFirstName());
            queryObj.setParameter("LastName", userDetRequest.getLastName());
            queryObj.setParameter("EmailId", userDetRequest.getEmailId());
            queryObj.setParameter("MobNum", userDetRequest.getMobNum());
            queryObj.setParameter("UserId", userDetRequest.getUserId());
            queryObj.setParameter("UserPassword", new BCryptPasswordEncoder().encode(userDetRequest.getPassword()));
            queryObj.setParameter("UserRole", userDetRequest.getRole());
            queryObj.setParameter("PBXExtn", userDetRequest.getPbxExtn());
            queryObj.setParameter("SkillSet", userDetRequest.getSkillSet());
            queryObj.setParameter("Agent", userDetRequest.getAgent());
            queryObj.setParameter("userGroup", userDetRequest.getUserGroup());
            queryObj.setParameter("status", "ACTIVE");
            insertVal = queryObj.executeUpdate();
            if (insertVal > 0) {
                if (userDetRequest.getRole() != null && userDetRequest.getRole().equalsIgnoreCase("Agent")) {
                    Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.INSERT_AGENT_DET);
                    queryUserMapObj.setParameter("Agent", userDetRequest.getUserId());
                    queryUserMapObj.executeUpdate();
                } else if (userDetRequest.getRole() != null && userDetRequest.getRole().equalsIgnoreCase("Supervisor")) {
                    String agentDetails = userDetRequest.getAgent();
                    String[] agentDetArr = agentDetails.split("\\,");
                    for (String agentID : agentDetArr) {
                        Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_AGENT_DET);
                        queryUserMapObj.setParameter("Agent", agentID);
                        queryUserMapObj.setParameter("Supervisor", userDetRequest.getUserId());
                        queryUserMapObj.executeUpdate();
                    }
                }
                return userKey;
            }
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::createuser" + e);
            return null;
        }
        return null;
    }

    public Integer getUserKey() {
        String maxVal;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_ID);
            maxVal = (String) queryObj.getSingleResult();
            if (maxVal == null || maxVal.isEmpty()) {
                maxVal = "0";
            }
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::getUserKey" + e);
            return 1;
        }
        return Integer.valueOf(maxVal) + 1;
    }

    @Override
    public List<Object[]> getUserDetail() {
        List<Object[]> resultList = null;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_DET);
            resultList = queryObj.getResultList();
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::getUserDetail" + e);
            return resultList;
        }
        return resultList;
    }

    @Override
    public List<Object[]> getUserDetail(String userGroup) {
        List<Object[]> resultList = null;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_DET_USERGROUP);
            queryObj.setParameter("groupName", userGroup);
            resultList = queryObj.getResultList();
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::getUserDetail" + e);
            return resultList;
        }
        return resultList;
    }

    @Override
    public boolean updateUser(UserManagementDetRequest userDetRequest) throws Exception {
        boolean isupdated;
        int insertVal;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_USER_DET);
            //queryObj.setParameter("userKey", userDetRequest.getUserKey());
            queryObj.setParameter("FirstName", userDetRequest.getFirstName());
            queryObj.setParameter("LastName", userDetRequest.getLastName());
            queryObj.setParameter("EmailId", userDetRequest.getEmailId());
            queryObj.setParameter("MobNum", userDetRequest.getMobNum());
            queryObj.setParameter("UserId", userDetRequest.getUserId());
//			queryObj.setParameter("UserPassword",userDetRequest.getPassword());
            queryObj.setParameter("UserRole", userDetRequest.getRole());
            queryObj.setParameter("PBXExtn", userDetRequest.getPbxExtn());
            queryObj.setParameter("SkillSet", userDetRequest.getSkillSet());
            queryObj.setParameter("Agent", userDetRequest.getAgent());
            queryObj.setParameter("userGroup", userDetRequest.getUserGroup());
            queryObj.setParameter("status", userDetRequest.getStatus());
            insertVal = queryObj.executeUpdate();
            if (insertVal > 0) {
                if (userDetRequest.getRole().equalsIgnoreCase("Supervisor")) {
                    Query querySuperMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_AGENT_DET_REMOVE_SUP);
                    querySuperMapObj.setParameter("Supervisor", userDetRequest.getUserId());
                    int removeresult = querySuperMapObj.executeUpdate();
                    if (removeresult > 0) {
                        String agentDetails = userDetRequest.getAgent();
                        String[] agentDetArr = agentDetails.split("\\,");
                        for (String agentID : agentDetArr) {
                            Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_AGENT_DET);
                            queryUserMapObj.setParameter("Supervisor", userDetRequest.getUserId());
                            queryUserMapObj.setParameter("Agent", agentID);
                            queryUserMapObj.executeUpdate();
                            logger.info("Updated Supervisor :" + userDetRequest.getUserId() + "for the Agent ID:" + agentID);
                        }
                    }
                }
                return true;
            }

        } catch (Exception e) {
            logger.error("Error occured in UserManagementImpl::updateUser" + e);
            return false;
        }
        return false;
    }

    @Override
    public boolean updateUserByPassword(UserManagementDetRequest userDetRequest) {
        boolean isupdated;
        int insertVal;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_USER_DET_BY_PASSWORD);
            //queryObj.setParameter("userKey", userDetRequest.getUserKey());
            queryObj.setParameter("FirstName", userDetRequest.getFirstName());
            queryObj.setParameter("LastName", userDetRequest.getLastName());
            queryObj.setParameter("EmailId", userDetRequest.getEmailId());
            queryObj.setParameter("MobNum", userDetRequest.getMobNum());
            queryObj.setParameter("UserId", userDetRequest.getUserId());
            queryObj.setParameter("UserPassword", new BCryptPasswordEncoder().encode(userDetRequest.getPassword()));
            queryObj.setParameter("UserRole", userDetRequest.getRole());
            queryObj.setParameter("PBXExtn", userDetRequest.getPbxExtn());
            queryObj.setParameter("SkillSet", userDetRequest.getSkillSet());
            queryObj.setParameter("Agent", userDetRequest.getAgent());
            queryObj.setParameter("userGroup", userDetRequest.getUserGroup());
            queryObj.setParameter("status", userDetRequest.getStatus());
            insertVal = queryObj.executeUpdate();
            if (insertVal > 0) {
                if (userDetRequest.getRole().equalsIgnoreCase("Supervisor")) {
                    Query querySuperMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_AGENT_DET_REMOVE_SUP);
                    querySuperMapObj.setParameter("Supervisor", userDetRequest.getUserId());
                    int removeresult = querySuperMapObj.executeUpdate();
                    if (removeresult > 0) {
                        String agentDetails = userDetRequest.getAgent();
                        String[] agentDetArr = agentDetails.split("\\,");
                        for (String agentID : agentDetArr) {
                            Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_AGENT_DET);
                            queryUserMapObj.setParameter("Supervisor", userDetRequest.getUserId());
                            queryUserMapObj.setParameter("Agent", agentID);
                            queryUserMapObj.executeUpdate();
                            logger.info("Updated Supervisor :" + userDetRequest.getUserId() + "for the Agent ID:" + agentID);
                        }
                    }
                }
                return true;
            }

        } catch (Exception e) {
            logger.error("Error occured in UserManagementImpl::updateUser" + e);
            return false;
        }
        return false;
    }

    @Override
    public FeatureResponse getFeatures() {
        FeatureResponse response = new FeatureResponse();
        try {
            String value = null;
            if (licensekey.isEmpty()) {
                logger.error("License value are null");
            } else {
                value = licenseUtil.decrypt(licensekey);
            }
            assert value != null;
            if (value.isEmpty()) {
                logger.error("License value are null");
            } else {
                if (value.contains("SIPGatewayValue")) {
                    response.setSipGateway(true);
                }
                if (value.contains("CTIIntegrationValue")) {
                    response.setCtiIntegration(true);
                }
                if (value.contains("CRMIntegrationValue")) {
                    response.setCrmIntegration(true);
                }
                if (value.contains("SMSGatewayValue")) {
                    response.setSmsGateway(true);
                }
                if (value.contains("ReportsValue")) {
                    response.setReports(true);
                }
                if (value.contains("VoiceMailValue")) {
                    response.setVoiceMail(true);
                }
                if (value.contains("CallRecordingValue")) {
                    response.setCallRecording(true);
                }
                if (value.contains("AgentDesktopValue")) {
                    response.setAgentDesktop(true);
                }
                if (value.contains("AgentPopupValue")) {
                    response.setAgentPopup(true);
                }
                if (value.contains("EmailGatewayValue")) {
                    response.setEmailGateway(true);
                }
                if (value.contains("TTSEngineValue")) {
                    response.setTtsEngine(true);
                }
                if (value.contains("MultiTimeZoneValue")) {
                    response.setMultiTimeZone(true);
                }
                if (value.contains("MultiLevelIVRValue")) {
                    response.setMultiLevelIvr(true);
                }
                if (value.contains("DashboardValue")) {
                    response.setDashboard(true);
                }
                if (value.contains("VoiceValue")) {
                    response.setVoice(true);
                }
                if (value.contains("SMSValue")) {
                    response.setSms(true);
                }
                if (value.contains("EmailValue")) {
                    response.setEmail(true);
                }
                if (value.contains("WhatsAppValue")) {
                    response.setWhatsapp(true);
                }
                if (value.contains("WebchatValue")) {
                    response.setWebchat(true);
                }
                if (value.contains("SocialMediaValue")) {
                    response.setSocialmedia(true);
                }
                if (value.contains("ManualValue")) {
                    response.setManual(true);
                }
                if (value.contains("PreviewValue")) {
                    response.setPreview(true);
                }
                if (value.contains("PredictiveValue")) {
                    response.setPredictive(true);
                }
                if (value.contains("ProgressiveValue")) {
                    response.setProgressive(true);
                }
                if (value.contains("RoboValue")) {
                    response.setRobo(true);
                }
                String macaddress = "";
                String[] pairs = value.split("\\)");

                for (String pair : pairs) {
                    String[] keyValue = pair.split("\\(");
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String values = keyValue[1];
                        switch (key) {
                            case "CONCURRENCY":
                                response.setConCurrency(Integer.parseInt(values));
                                break;
                            case "LINES":
                                response.setNoOflines(Integer.parseInt(values));
                                break;
                            case "USERS":
                                response.setNoOfUsers(Integer.parseInt(values));
                                break;
                            case "TENANTID":
                                response.setTenantId(values);
                                break;
                            case "MAC":
                                macaddress = values;
                                break;
                            case "ExpireDate":
                                response.setExpireDate(values);
                                break;
                            default:
                                break;
                        }
                    }
                }
                response.setMacAddress(checkMacaddress(macaddress));
                logger.info("Mac Address is present : "+ checkMacaddress(macaddress));

                return response;
            }
        } catch (Exception e) {
            logger.error("Error occured in UserManagementImpl::getFeatures" + e);
        }
        return response;
    }

    private boolean checkMacaddress(String macaddress) {
        try {
            InetAddress localhost = InetAddress.getLocalHost();

            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhost);

            byte[] macBytes = networkInterface.getHardwareAddress();

            StringBuilder macStringBuilder = new StringBuilder();
            for (int i = 0; i < macBytes.length; i++) {
                macStringBuilder.append(String.format("%02X%s", macBytes[i], (i < macBytes.length - 1) ? "-" : ""));
            }
            String systemMac = macStringBuilder.toString();
            System.out.println(systemMac);
            return systemMac.equalsIgnoreCase(macaddress);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object extractDate(String input) {
        int startIndex = input.indexOf("ExpireDate(");
        if (startIndex != -1) {
            startIndex += "ExpireDate(".length(); // Move startIndex past the "ExpireDate(" string
            int endIndex = input.indexOf(")", startIndex);
            if (endIndex != -1) {
                return input.substring(startIndex, endIndex);
            }
        }
        return null;
    }


    @Override
    public List<Object[]> getAvailAgent() {
        List<Object[]> resultList = null;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_AVAIL_AGENT_DETAILS);
            resultList = queryObj.getResultList();
            logger.info("getAvailAgent resultList:: " + resultList);
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::getAvailAgent" + e);
            return resultList;
        }
        return resultList;
    }

    @Override
    public List<Object[]> getRoleDetail() {
        List<Object[]> resultList = null;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_ROLE_DET);
            resultList = queryObj.getResultList();
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::getRoleDetail" + e);
            return resultList;
        }
        return resultList;
    }

    @Override
    public Optional<UserDto> findByUsername(String username) throws Exception {
        StringBuilder sqlQry = null;
        Optional<UserDto> userOptional = null;
        UserDto userDto = new UserDto();
        List<Object[]> resultObj = null;
        try {
            logger.info("Getting user details by Id:" + username);
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_DET_BYID);
            queryObj.setParameter("UserId", username);
            resultObj = (List<Object[]>) queryObj.getResultList();

            if (resultObj != null && !resultObj.isEmpty()) {
                for (Object[] objects : resultObj) {
                    userDto = new UserDto();
                    userDto.setAutogenUsersId(new BigInteger(String.valueOf(1)));
                    userDto.setEmployeeId(objects[1].toString());
                    userDto.setPassword(objects[2].toString());
                    userDto.setStatus(objects[12].toString());
                    userDto.setEmail(objects[4].toString());
                    userDto.setFirstName(objects[5].toString());
                    userDto.setLastName(objects[6].toString());
                    if (null != objects[7]) {
                        userDto.setMobileNumber(objects[7].toString());
                    }
                    userDto.setAutogenUsersDetailsId(objects[9].toString());
                    userDto.setGroupName(objects[8].toString());
                    Set<Roles> roleset = new HashSet<>();
                    Roles roles = new Roles();
                    roles.setRolesName(objects[3].toString());
                    roleset.add(roles);
                    userDto.setRoles(roleset);
                    List<String> rolesList = new ArrayList<String>();
                    rolesList.add(roles.getRolesName());
                    userDto.setRolesList(rolesList);

                }
            }
            userOptional = Optional.ofNullable(userDto);
        } catch (Exception e) {
            logger.error("Exception findByUsername() : {}", e.getMessage());
        } finally {
            firstEntityManager.close();
        }
        return userOptional;
    }

    @Override
    public List<Object[]> getAgentDetail(String userID) {
        List<Object[]> resultList = null;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_AGENT_DETAILS_BYID);
            queryObj.setParameter("userId", userID);
            resultList = queryObj.getResultList();
            logger.info("getAvailAgent resultList:: " + resultList);
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::getAvailAgent" + e);
            return resultList;
        }
        return resultList;
    }

    @Override
    public boolean validateUserId(UserManagementDetRequest userDetRequest) throws Exception {
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.VALIDATE_USER_ID);
            queryObj.setParameter("UserId", userDetRequest.getUserId());
            int result = (int) queryObj.getSingleResult();
            if (result > 0)
                return false;
            else
                return true;
        } catch (Exception e) {
            logger.error("Error occured in CampaignDaoImpl::validateUserId" + e);
            return true;
        }
    }

    @Override
    public boolean validateUserExtn(UserManagementDetRequest userDetRequest) throws Exception {
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.VALIDATE_USER_EXTN);
            queryObj.setParameter("PBXExtn", userDetRequest.getPbxExtn());
            int result = (int) queryObj.getSingleResult();
            if (result > 0)
                return false;
            else
                return true;
        } catch (Exception e) {
            logger.error("Error occured in CampaignDaoImpl::validateUserExtn" + e);
            return true;
        }
    }

    @Override
    public boolean validateUserEmail(UserManagementDetRequest userDetRequest) throws Exception {
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.VALIDATE_USER_EMAIL);
            queryObj.setParameter("EmailId", userDetRequest.getEmailId());
            int result = (int) queryObj.getSingleResult();
            if (result > 0)
                return false;
            else
                return true;
        } catch (Exception e) {
            logger.error("Error occured in CampaignDaoImpl::validateUserEmail" + e);
            return true;
        }
    }

    @Override
    public boolean validateUserPhone(UserManagementDetRequest userDetRequest) throws Exception {
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.VALIDATE_USER_PHONE);
            queryObj.setParameter("MobNum", userDetRequest.getMobNum());
            int result = (int) queryObj.getSingleResult();
            if (result > 0)
                return false;
            else
                return true;
        } catch (Exception e) {
            logger.error("Error occured in CampaignDaoImpl::validateUserPhone" + e);
            return true;
        }
    }

    @Override
    public List<Object[]> getRTAgentDetail() {
        List<Object[]> resultList = null;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_RT_AGENT_DET);
            resultList = queryObj.getResultList();
        } catch (Exception e) {
            logger.error("Error occured in UserManagementDaoImpl::getUserDetail" + e);
            return resultList;
        }
        return resultList;
    }
}

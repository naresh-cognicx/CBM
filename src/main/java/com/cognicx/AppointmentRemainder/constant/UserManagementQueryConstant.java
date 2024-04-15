package com.cognicx.AppointmentRemainder.constant;

public class UserManagementQueryConstant {
	public static final String INSERT_USER_DET = "insert into appointment_remainder.usermanagement_det(userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup,status) values (:userKey,:FirstName,:LastName,:EmailId,:MobNum,:UserId,:UserPassword,:UserRole,:PBXExtn,:SkillSet,:Agent,:userGroup,:status)";
	
	public static final String INSERT_AGENT_DET = "insert into appointment_remainder.[agent_sup_mapping](Agent) values (:Agent)";
	public static final String UPDATE_AGENT_DET = "UPDATE appointment_remainder.[agent_sup_mapping] SET Supervisor=:Supervisor where Agent=:Agent";
	
	
	public static final String GET_USER_ID = "select max(SUBSTRING(userKey, 3, 100)) from appointment_remainder.usermanagement_det";
//	public static final String GET_USER_DET = "SELECT userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup,status from appointment_remainder.usermanagement_det";

	public static final String GET_USER_DET = "SELECT userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup,status from appointment_remainder.usermanagement_det where UserRole != 'Admin'";
	public static final String UPDATE_USER_DET = "UPDATE appointment_remainder.usermanagement_det SET FirstName = :FirstName,LastName = :LastName,EmailId = :EmailId,MobNum = :MobNum,UserRole = :UserRole,PBXExtn = :PBXExtn,SkillSet = :SkillSet, Agent=:Agent, userGroup=:userGroup, status=:status WHERE UserId = :UserId";

	public static final String GET_AVAIL_AGENT = "select Agent from appointment_remainder.agent_sup_mapping where Supervisor is NULL";
	public static final String GET_ROLE_DET = "select RoleId, Role from appointment_remainder.user_role_det";
	
	public static final String GET_AVAIL_AGENT_DETAILS ="SELECT FirstName,LastName,UserId,Agent FROM appointment_remainder.usermanagement_det where UserId IN (select Agent from appointment_remainder.agent_sup_mapping where Supervisor is NULL)";
	
	
	public static final String GET_AGENT_DETAILS_BYID ="SELECT FirstName,LastName,UserId from appointment_remainder.usermanagement_det where UserId=:userId";
	
	
	
	public static final String GET_USER_DET_BYID = "SELECT userKey, UserId, UserPassword, UserRole, EmailId, FirstName, LastName, MobNum, userGroup, PBXExtn, SkillSet, Agent, status FROM appointment_remainder.usermanagement_det WHERE UserId =:UserId COLLATE Latin1_General_CS_AS";

	public static final String GET_USER_DET_USERGROUP = "SELECT userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup,status  FROM appointment_remainder.usermanagement_det where userGroup=:groupName";
	public static final String UPDATE_AGENT_DET_REMOVE_SUP = "UPDATE appointment_remainder.agent_sup_mapping SET Supervisor = NULL WHERE Supervisor = :Supervisor";
	
	public static final String VALIDATE_USER_ID = "select count(1) from [appointment_remainder].[usermanagement_det] where UserId=:UserId";
	public static final String VALIDATE_USER_EXTN = "select count(1) from [appointment_remainder].[usermanagement_det] where PBXExtn=:PBXExtn";
	public static final String VALIDATE_USER_EMAIL = "select count(1) from [appointment_remainder].[usermanagement_det] where EmailId=:EmailId";
	public static final String VALIDATE_USER_PHONE = "select count(1) from [appointment_remainder].[usermanagement_det] where MobNum=:MobNum";
	public static final String GET_RT_AGENT_DET = "SELECT userId,State,Device,logstatus,rec_upt_date,rec_login_date,rec_logout_date from [appointment_remainder].[agent_status_det]";

	public static final String GET_AVAIL_AGENT_FORCAMPAIGN = "SELECT TOP 1 * FROM[appointment_remainder].[agent_status_det] where State='Reachable' ORDER BY [rec_upt_date]";

	public static final String UPDATE_USER_DET_BY_PASSWORD = "UPDATE appointment_remainder.usermanagement_det SET FirstName = :FirstName,LastName = :LastName,EmailId = :EmailId,MobNum = :MobNum,UserRole = :UserRole,PBXExtn = :PBXExtn,SkillSet = :SkillSet, Agent=:Agent, userGroup=:userGroup, status=:status, UserPassword=:UserPassword WHERE UserId = :UserId";
}

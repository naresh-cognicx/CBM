package com.cognicx.AppointmentRemainder.constant;

public class UserManagementQueryConstant {
	public static final String INSERT_USER_DET = "insert into appointment_remainder.usermanagement_det(userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup) values (:userKey,:FirstName,:LastName,:EmailId,:MobNum,:UserId,:UserPassword,:UserRole,:PBXExtn,:SkillSet,:Agent,:userGroup)";
	
	public static final String INSERT_AGENT_DET = "insert into appointment_remainder.[agent_sup_mapping](Agent) values (:Agent)";
	public static final String UPDATE_AGENT_DET = "UPDATE appointment_remainder.[agent_sup_mapping] SET Supervisor=:Supervisor where Agent=:Agent";
	
	
	public static final String GET_USER_ID = "select max(SUBSTRING(userKey, 3, 100)) from appointment_remainder.usermanagement_det";

	public static final String GET_USER_DET = "SELECT userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup from appointment_remainder.usermanagement_det";

	public static final String UPDATE_USER_DET = "UPDATE appointment_remainder.usermanagement_det SET FirstName = :FirstName,LastName = :LastName,EmailId = :EmailId,MobNum = :MobNum,UserId = :UserId,UserPassword = :UserPassword,UserRole = :UserRole,PBXExtn = :PBXExtn,SkillSet = :SkillSet, Agent=:Agent WHERE userKey = :userKey";

	public static final String GET_AVAIL_AGENT = "select Agent from appointment_remainder.agent_sup_mapping where Supervisor is NULL";
	public static final String GET_ROLE_DET = "select RoleId, Role from appointment_remainder.user_role_det";
	
	public static final String GET_AVAIL_AGENT_DETAILS ="SELECT FirstName,LastName,UserId,Agent FROM [appointment_remainder].[usermanagement_det] where Agent IN (select Agent from appointment_remainder.agent_sup_mapping where Supervisor is NULL)";
	
}

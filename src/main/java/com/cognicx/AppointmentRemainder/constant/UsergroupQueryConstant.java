package com.cognicx.AppointmentRemainder.constant;

public class UsergroupQueryConstant {
	public static final String GET_USERGROUP_DET = "SELECT groupId,usergroupName,usergroupDesc,usergroupType FROM appointment_remainder.usergroup_det";
	public static final String UPDATE_USERGROUP_DET = "UPDATE appointment_remainder.usergroup_det SET usergroupName = :usergroupName,usergroupDesc = :usergroupDesc,usergroupType = :usergroupType WHERE groupId = :groupId";
	public static final String INSERT_USERGROUP_DET = "insert into appointment_remainder.usergroup_det(groupId,usergroupName,usergroupDesc,usergroupType) "
			+ "values (:groupId,:usergroupName,:usergroupDesc,:usergroupType)";
	public static final String GET_USERGROUP_ID = "select max(SUBSTRING(groupId, 3, 100)) from appointment_remainder.usergroup_det";

	
}

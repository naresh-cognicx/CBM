package com.cognicx.AppointmentRemainder.constant;

public class DispositionQueryConstant {
	public static final String GET_DISPOSITION_CODE_DET = "select dispCode,dispItem,dispCodeId  FROM appointment_remainder.disposition_code_det where dispId=:dispId";
	public static final String GET_DISPOSITION_DET = "select dispId,dispositionName,description  FROM appointment_remainder.disposition_management ";
	public static final String INSERT_DISPOSITION_DET = "insert into appointment_remainder.disposition_management (dispId,dispositionName,description) values(:dispId,:dispositionName,:description)";
	public static final String INSERT_DISPOSITION_CODE_DET = "insert into appointment_remainder.disposition_code_det (dispId,dispCode,dispItem,dispCodeId) values(:dispId,:dispCode,:dispItem,:dispCodeId)";
	
	public static final String UPDATE_DISPOSITION_DET = "update appointment_remainder.disposition_management SET dispositionName = :dispositionName,description = :description WHERE dispId = :dispId";
	public static final String UPDATE_DISPOSITION_CODE_DET = "update appointment_remainder.disposition_code_det SET  dispCode = :dispCode,dispItem = :dispItem WHERE dispId = :dispId and dispCodeId=:dispCodeId";
	public static final String GET_DISPOSITION_ID = "select max(SUBSTRING(dispId, 3, 100)) from appointment_remainder.disposition_management";
	public static final String VALIDATE_DISPOSITION_ID = "select count(*) from [appointment_remainder].[disposition_code_det] where dispId=:dispId";

	public static final String GET_DISP_CODE_ID = "select max(SUBSTRING(dispCodeId, 3, 100)) from appointment_remainder.disposition_code_det";
	
}

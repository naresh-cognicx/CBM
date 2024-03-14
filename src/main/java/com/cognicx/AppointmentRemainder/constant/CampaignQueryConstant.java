package com.cognicx.AppointmentRemainder.constant;

public class CampaignQueryConstant {

	public static final String INSERT_CAMPAIGN_DET = "INSERT INTO appointment_remainder.campaign_det(campaign_id,name,description,status,max_adv_time,retry_delay,retry_count,concurrent_call,start_date,start_time,end_date,end_time,ftp_location,ftp_credentials,file_name,call_before,dncId) "
			+ "VALUES (:campaignId,:name,:desc,:status,:maxAdvTime,:retryDelay,:retryCount,:concurrentCall,:startDate,:startTime,:endDate,:endTime,:ftpLocation,:ftpCredentials,:fileName,:callBefore,:dncId)";

	public static final String INSERT_CAMPAIGN_WEEK_DET = "insert into appointment_remainder.campaign_week_det (campaign_id,day,status,start_time,end_time) values(:campaignId,:day,:status,:startTime,:endTime)";

	public static final String GET_CAMPAIGN_ID = "select max(SUBSTRING(campaign_id, 3, 100)) from appointment_remainder.campaign_det";

	public static final String GET_CAMPAIGN_DET = "SELECT campaign_id,name,description,status,max_adv_time,retry_delay,retry_count,concurrent_call,start_date,start_time,campaign_det.end_date,end_time,ftp_location,ftp_credentials,file_name,call_before,dncId FROM appointment_remainder.campaign_det where status = 1 ";

	public static final String GET_CAMPAIGN_WEEK_DET = "SELECT campaign_week_id,campaign_id,day,status,start_time,end_time from appointment_remainder.campaign_week_det";

	public static final String UPDATE_CAMPAIGN_DET = "UPDATE appointment_remainder.campaign_det SET name = :name,status = :status,max_adv_time = :maxAdvTime,retry_delay = :retryDelay,retry_count = :retryCount,concurrent_call = :concurrentCall,start_date = :startDate,start_time = :startTime,end_date = :endDate,end_time = :endTime,ftp_location = :ftpLocation,ftp_credentials = :ftpCredentials,rec_updt_dt = getdate(),call_before=:callBefore,file_name=:fileName, dncId=:dncId WHERE campaign_id = :campaignId";

	public static final String UPDATE_CAMPAIGN_WEEK_DET = "UPDATE appointment_remainder.campaign_week_det SET day=:day, status=:status, start_time=:startTime, end_time=:endTime where campaign_week_id=:campaignWeekId";

	public static final String UPDATE_CALL_DET = "UPDATE appointment_remainder.contact_det SET caller_response=:callerResponse, call_status=:callStatus, call_duration=:callDuration, call_retry_count=:retryCount,rec_upt_date=getdate() where contact_id=:contactId";

	//public static final String INSERT_CONTACT_DET = "insert into appointment_remainder.contact_det (campaign_id,campaign_name,doctor_name,patient_name,contact_number,appointment_date,language,call_status,upload_history_id) values(:campaignId,:campaignName,:doctorName,:patientName,:contactNo,:appDate,:language,:callStatus,:historyId)";
	
	public static final String INSERT_CONTACT_DET = "insert into appointment_remainder.contact_det (campaign_id,campaign_name,last_four_digits,customer_mobile_number,total_due,minimum_payment,due_date,language,call_status,upload_history_id) values(:campaignId,:campaignName,:last_four_digits,:customer_mobile_number,:total_due,:minimum_payment,:due_date,:language,:callStatus,:historyId)";

	//public static final String GET_CONTACT_DET = "select campaign_id,campaign_name,doctor_name,patient_name,contact_number,appointment_date,language,contact_id,call_retry_count,rec_upt_date,call_status from appointment_remainder.contact_det where call_status !='ANSWERED' order by appointment_date asc";
	
	public static final String GET_CONTACT_DET = "select campaign_id,campaign_name,last_four_digits,customer_mobile_number,total_due,minimum_payment,due_date,language,contact_id,call_retry_count,rec_upt_date,call_status from appointment_remainder.contact_det where call_status !='ANSWERED' order by appointment_date asc";
	

	public static final String VALIDATE_CAMPAIGN_NAME = "select count(1) from [appointment_remainder].[campaign_det] where name=:name";

	public static final String GET_SUMMARY_REPORT_DET = "select campaign_name,format(due_date,'dd-MMM-yyyy'),count(contact_id) as totalContact,SUM(CASE WHEN call_status!='New' THEN 1 ELSE 0 END) as contactCalled,"
			+ "  SUM(CASE WHEN call_status in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as connectedCall,"
			+ "  SUM(CASE WHEN call_status='NOT ANSWERED'  THEN 1 ELSE 0 END) as answered,"
			+ "  SUM(CASE WHEN call_status='BUSY'  THEN 1 ELSE 0 END) as busy,"
			+ "  SUM(CASE WHEN caller_response='1'  THEN 1 ELSE 0 END) as confirmed,"
			+ "  SUM(CASE WHEN caller_response='2'  THEN 1 ELSE 0 END) as cancelled,"
			+ "  SUM(CASE WHEN caller_response='3'  THEN 1 ELSE 0 END) as rescheduled,"
			+ "  SUM(CASE WHEN caller_response='0'  THEN 1 ELSE 0 END) as noresponse,"
			+ "  SUM(CASE WHEN call_status not in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as others"
			+ "  from appointment_remainder.contact_det"
			+ "  where cast(due_date as date) between :startDate and :endDate and campaign_id =:name"
			+ "  group by campaign_name,format(due_date,'dd-MMM-yyyy')";

	//public static final String GET_CONTACT_DET_REPORT = "SELECT contact_id,campaign_id,campaign_name,doctor_name,patient_name,contact_number,format(appointment_date,'dd-MMM-yyyy hh:mm:ss tt'),caller_response ,call_status ,call_duration ,call_retry_count FROM appointment_remainder .contact_det where";
	
	public static final String GET_CONTACT_DET_REPORT = "SELECT contact_id,campaign_id,campaign_name,last_four_digits,customer_mobile_number,total_due,minimum_payment,format(due_date,'dd-MMM-yyyy hh:mm:ss tt'),caller_response ,call_status ,call_duration ,call_retry_count FROM appointment_remainder .contact_det where";

	public static final String GET_CALL_RETRY_DET = "select contact_id,call_status,format(rec_add_dt,'dd-MMM-yyyy hh:mm:ss tt') from appointment_remainder.call_retry_det where contact_id in (:contactIdList) order by contact_id asc";

	public static final String INSERT_CALL_RETRY_DET = "insert into appointment_remainder.call_retry_det (contact_id,call_status,retry_count) values (:contactId,:callStatus,:retryCount)";

	public static final String GET_UPLOAD_HISTORY_DETIALS = "select upload_history_id,campaign_id,campaign_name,uploaded_on,file_name from appointment_remainder.upload_history_det where status=1 and cast(uploaded_on as date) between :startDate and :endDate";

	public static final String DELETE_CONTACT_BY_HISTORY = "delete from appointment_remainder.contact_det where upload_history_id=:historyId";
	
	public static final String GET_CUSTOMER_DATA = " select customer_data_id,last_four_digits,customer_mobile_number,total_due,minimum_payment,due_date,status from appointment_remainder.customer_data where status = 'Active' ";
	
	public static final String GET_CALL_RETRY_COUNT = " select call_retry_count from appointment_remainder.contact_det where contact_id = :contact_id ";
	
	public static final String GET_CONTACT_DET_BY_DATE_RANGE = "select campaign_id,campaign_name,last_four_digits,customer_mobile_number,total_due,minimum_payment,due_date,language,contact_id,call_retry_count,rec_upt_date,call_status from appointment_remainder.contact_det where cast(due_date as date) between :startDate and :endDate ";
	
	public static final String GET_CALL_RETRY_DETAILS = " select call_retry_id,contact_id,call_status,rec_add_dt,retry_count from appointment_remainder.call_retry_det where contact_id = :contact_id ";
	
	public static final String GET_LEAD_WISE_SUMMARY_REPORT_DET = "select count(contact_id) as totalContact,SUM(CASE WHEN call_status!='New' THEN 1 ELSE 0 END) as contactCalled,"
			+ "  SUM(CASE WHEN call_status in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as connectedCall,"
			+ "  SUM(CASE WHEN call_status='ANSWERED'  THEN 1 ELSE 0 END) as answered,"
			+ "  SUM(CASE WHEN call_status='BUSY'  THEN 1 ELSE 0 END) as busy,"
			+ "  SUM(CASE WHEN call_status='NOANSWER'  THEN 1 ELSE 0 END) as notanswered,"
			+ "  SUM(CASE WHEN caller_response='1'  THEN 1 ELSE 0 END) as confirmed,"
			+ "  SUM(CASE WHEN caller_response='2'  THEN 1 ELSE 0 END) as cancelled,"
			+ "  SUM(CASE WHEN caller_response='3'  THEN 1 ELSE 0 END) as rescheduled,"
			+ "  SUM(CASE WHEN caller_response='0'  THEN 1 ELSE 0 END) as noresponse,"
			+ "  SUM(CASE WHEN call_status not in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as others"
			+ "  from appointment_remainder.contact_det"
			+ "  where cast(due_date as date) between :startDate and :endDate ";
	
	public static final String GET_CALL_VOLUME_REPORT_DET = "select format(due_date,'dd-MMM-yyyy'),count(contact_id) as totalContact,SUM(CASE WHEN call_status!='New' THEN 1 ELSE 0 END) as contactCalled,"
			+ "  SUM(CASE WHEN call_status in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as connectedCall,"
			+ "  SUM(CASE WHEN call_status='ANSWERED'  THEN 1 ELSE 0 END) as answered,"
			+ "  SUM(CASE WHEN call_status='BUSY'  THEN 1 ELSE 0 END) as busy,"
			+ "  SUM(CASE WHEN call_status='NOANSWER'  THEN 1 ELSE 0 END) as notanswered,"
			+ "  SUM(CASE WHEN caller_response='1'  THEN 1 ELSE 0 END) as confirmed,"
			+ "  SUM(CASE WHEN caller_response='2'  THEN 1 ELSE 0 END) as cancelled,"
			+ "  SUM(CASE WHEN caller_response='3'  THEN 1 ELSE 0 END) as rescheduled,"
			+ "  SUM(CASE WHEN caller_response='0'  THEN 1 ELSE 0 END) as noresponse,"
			+ "  SUM(CASE WHEN call_status not in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as others"
			+ "  from appointment_remainder.contact_det"
			+ "  where cast(due_date as date) between :startDate and :endDate "
			+ "  group by format(due_date,'dd-MMM-yyyy')";
	
	
	public static final String INSERT_DUMMY_CONTACT_DET = "insert into appointment_remainder.dbo.dummy_det (campaign_id,mobile_number,unix_time,due_date,contact_id) values(:campaign_id,:mobile_number,:unix_time,:due_date,:contact_id)";
	
	//Added on 05/02/2024
	public static final String GET_CAMPAIGN_STATUS = "select campaign_status from [appointment_remainder].[campaign_status] where campaign_id=:campaign_id";

	//Added on 14/02/2024
	public static final String INSERT_CAMPAIGN_STATUS = "insert into appointment_remainder.campaign_status (campaign_id,campaign_status) values (:campaignId,:campaign_status)";
	public static final String UPDATE_CAMPAIGN_STATUS = "update appointment_remainder.campaign_status SET campaign_status=:campaign_status where campaign_id=:campaignId";
	
	public static final String GET_CAMPAIGN_DET_RT = "select campaign_id,name,status,start_date,end_date,start_time,end_time,concurrent_call,dncId FROM appointment_remainder.campaign_det";
	
	public static final String GET_CAMPAIGN_BASED_COUNT = "select COUNT(*) from appointment_remainder.contact_det where campaign_id=:campaignId";

	public static final String GET_CAMPAIGN_BASED_CONT_STATUS = "select COUNT(*) from appointment_remainder.contact_det where campaign_id=:campaignId AND call_status=:disposition";
	
	public static final String INSERT_ACTIVE_CONTACT_DET = "insert into appointment_remainder.active_contact_det (calluid,productid,connectedlinenum,status) values (:calluid,:productid,:connectedlinenum,:status)";

	public static final String UPDATE_ACTIVE_CONTACT_DET = "update appointment_remainder.active_contact_det SET status=:status,errorcode=:errorcode where calluid=:calluid and productid=:productid and connectedlinenum=:connectedlinenum";
	
	public static final String GET_ACTIVE_CONTACT_DET = "select COUNT(*) from appointment_remainder.active_contact_det where productid=:campaignId AND status=:status";

	public static final String GET_ACTIVE_CONTACT_ERR_DET = "select COUNT(*) from appointment_remainder.active_contact_det where productid=:campaignId AND errorcode in (";

	public static final String INSERT_SURVEY_CONTACT_DET = "insert into appointment_remainder.survey_contact_det (phone,actionId,Survey_Lang,MainSkillset,subSkillset) values (:phone,:actionId,:Survey_Lang,:MainSkillset,:subSkillset)";
	public static final String INSERT_dNS_DET="INSERT INTO appointment_remainder.DNC_Details(DNCID,DNC_Name,description) VALUES (:DNCID,:DNC_Name,:description)";
	public static final String GET_dns_DET = "Select DNCID,DNC_Name,description from appointment_remainder.DNC_Details";
	public static final String UPDATE_DNS_DET = "UPDATE appointment_remainder.DNC_Details SET DNC_Name=:DNC_Name,description = :description where DNCID=:DNCID";
	public static final String INSERT_CONTACT_DET1 = "insert into appointment_remainder.DNC_Contact (contactNumber) values(:contactNumber)";
	
	
	public static final String GET_DNC_CONTACT = "select count(*) from appointment_remainder.DNC_Contact where DNCID=:DNCID";
	public static final String GET_DNC_CONTACT_DET = "select contactNumber from appointment_remainder.DNC_Contact where DNCID=:DNCID";

	public static final String COUNT_ACTIVE_CONTACT_DET = "SELECT COUNT(*) FROM appointment_remainder.active_contact_det WHERE productid=:productId AND status=:status";


}


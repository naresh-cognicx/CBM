package com.cognicx.AppointmentRemainder.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.cognicx.AppointmentRemainder.Request.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
//import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponseReport;
import com.cognicx.AppointmentRemainder.service.CampaignService;
import com.cognicx.AppointmentRemainder.service.impl.CampaignServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
@CrossOrigin
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    CampaignService campaignService;

    @Value("${app.isFTP}")
    private String isFTP;

    @Value("${app.fileDirectory}")
    private String fileDirectory;

    /*
     * @Value("${call.apiurl}") private String callApi;
     */

    @Value("${call.apiurl.autocalls}")
    private String callApiAutoCall;

    @Value("${failure.filediectory}")
    private String failureDirectory;

    @Value("${predue.dialplan}")
    private String preDueDialPlan;

    @Value("${postdue.dialplan}")
    private String postDueDialPlan;

    @Value("${autodialcamp}")
    private String autoDialCamp;

    //added on 21022024
    @Value("${productID}")
    private String productID;

    private static List<String> listOfActionIdStore = new CopyOnWriteArrayList<>();

    private static Logger logger = LoggerFactory.getLogger(CampaignController.class);

    // @Scheduled(cron = "0 0/2 * * * *")
    @PostMapping("/uploadSftpContact")
    public void setupJsch() throws JSchException, SftpException, IOException {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;
        boolean isFileFound = true;
        try {
            String fileTimestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
            if (campaignDetList != null && !campaignDetList.isEmpty()) {
                for (CampaignDetRequest campaignDetRequest : campaignDetList) {
                    isFileFound = true;
                    String fileName = campaignDetRequest.getFileName();
                    logger.info("****Getting SFTP session****");
                    session = jsch.getSession(campaignDetRequest.getFtpUsername(), campaignDetRequest.getFtpLocation(),
                            22);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(campaignDetRequest.getFtpPassword());
                    InputStream stream = null;
                    if (isFTP != null && "true".equalsIgnoreCase(isFTP)) {
                        session.connect();
                        Channel channel = session.openChannel("sftp");
                        channel.connect();
                        sftpChannel = (ChannelSftp) channel;
                        logger.info("****Got SFTP channel ****");
                        // sftpChannel.cd("/www/eappzz.com/reminder1");
                        logger.info(
                                "****Getting '" + campaignDetRequest.getFileName() + "' file from SFTP channel ****");
                        try {
                            stream = sftpChannel.get(campaignDetRequest.getFileName());
                        } catch (SftpException e) {
                            logger.error("SftpException occurred in Retriving" + campaignDetRequest.getFileName()
                                    + "file from SFTP");
                            isFileFound = false;
                        } catch (Exception e) {
                            logger.error("Exception occurred in Retriving" + campaignDetRequest.getFileName()
                                    + "file from SFTP");
                            isFileFound = false;
                        }
                    } else {
                        stream = null;
                    }
                    if (isFileFound) {
                        BigInteger historyId = getUploadHistoryid(campaignDetRequest, fileName);
                        List<ContactDetDto> contactDetList = csvToData(stream, historyId, isFTP, fileDirectory,
                                fileTimestamp, failureDirectory, campaignDetList, new ArrayList<>(), "", "");
                        logger.info("****Converted CSV DATA to Object****");
                        if (stream != null)
                            stream.close();
                        logger.info("****Inserting contact details to DB Table****");
                        for (ContactDetDto contactDetDto : contactDetList) {
                            campaignService.createContact(contactDetDto);
                        }
                        String[] file = fileName.split("\\.");
                        if (sftpChannel != null) {
                            sftpChannel.rename(fileName, file[0] + "_" + fileTimestamp + "." + file[1]);
                            sftpChannel.exit();
                        }
                    }
                    session.disconnect();
                }
            }
        } catch (IOException io) {
            logger.error("IO Exception occurred file upload from SFTP server due to " + io.getMessage());
        } catch (JSchException e) {
            logger.error("JSchException occurred file upload from SFTP server due to " + e.getMessage());
            e.printStackTrace();
        } catch (SftpException e) {
            logger.error("SftpException occurred file upload from SFTP server due to " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception occurred during file upload from SFTP server due to " + e.getMessage());
        } finally {
            if (sftpChannel != null)
                sftpChannel.exit();
            session.disconnect();
        }
    }

    private BigInteger getUploadHistoryid(CampaignDetRequest campaignDetRequest, String fileName) {
        UploadHistoryDto uploadHistoryDto = new UploadHistoryDto();
        uploadHistoryDto.setCampaignId(campaignDetRequest.getCampaignId());
        uploadHistoryDto.setCampaignName(campaignDetRequest.getCampaignName());
        uploadHistoryDto.setFilename(fileName);
        BigInteger historyId = campaignService.insertUploadHistory(uploadHistoryDto);
        return historyId;
    }

    // @Scheduled(cron = "0 0/2 * * * *")
    @PostMapping("/uploadContact")
    public void uploadContact() {
        FTPClient client = new FTPClient();
        InputStream in;
        try {
            List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
            if (campaignDetList != null && !campaignDetList.isEmpty()) {
                for (CampaignDetRequest campaignDetRequest : campaignDetList) {
                    client.connect(campaignDetRequest.getFtpLocation(), 21);
                    boolean isSuccess = client.login(campaignDetRequest.getFtpUsername(),
                            campaignDetRequest.getFtpPassword());
//					List<ContactDetDto> contactDetList = csvToData(null);
//					for (ContactDetDto contactDetDto : contactDetList) {
//						campaignService.createContact(contactDetDto);
//					}
                    if (isSuccess) {
                        List<String> fileName = new ArrayList<>();
                        // client.changeWorkingDirectory("/eappzz.com/reminder1");
                        FTPFile[] files = client.listFiles();
                        for (FTPFile file : files) {
                            if (file.isFile()) {
                                fileName.add(file.getName());
                                logger.info("File Names file.getName()");
                            }
                        }
                        if (fileName.contains(campaignDetRequest.getFileName())) {
                            logger.info("Inside If condition");
                            in = client.retrieveFileStream(campaignDetRequest.getFileName());
                            // List<ContactDetDto> contactDetList = csvToData(in, null);
                            boolean store = client.storeFile("tez.csv", in);
                            in.close();
                            String newFileName = "campaign_new.csv";
                            boolean isRenamed = client.rename(campaignDetRequest.getFileName(), "test.csv");
                            logger.info("Renamed Status:: " + isRenamed);
                            client.disconnect();
//							for (ContactDetDto contactDetDto : contactDetList) {
//								campaignService.createContact(contactDetDto);
//							}
                        } else {
                            logger.info("In ftp Fileupload:: expected file '" + campaignDetRequest.getFileName()
                                    + "' is not there");
                            client.disconnect();
                        }

                    }
                }
            }
//			client.connect("eappzz.com", 21);
//			boolean isSuccess = client.login("test1@eappzz.com", "2u42*(1t5#to");
//			client.changeWorkingDirectory("/eappzz.com/reminder1");
//
//			String filename = "campaign.csv";
//			InputStream in = client.retrieveFileStream(filename);

            // List<ContactDetDto> contactDetList = csvToData(in);
            // client.disconnect();
            // in.close();
            // for (ContactDetDto contactDetDto : contactDetList) {
            // campaignService.createContact(contactDetDto);
            // }
            // retrieveFile("/" + filename, in);
        } catch (Exception e) {
            logger.error("Error occured in FTP File upload:: " + e);
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // return null;
    }

    private static List<ContactDetDto> csvToData(InputStream is, BigInteger historyId, String isFTP,
                                                 String fileDirectory, String fileTimestamp, String failureDirectory,
                                                 List<CampaignDetRequest> campaignDetList, List<ContactDetDto> failureList, String campaignId, String campaignName) {
        List<ContactDetDto> contactList = null;
        // List<ContactDetDto> failureList = null;
        CSVPrinter csvPrinter = null;
        CSVParser csvParser = null;
        BufferedReader fileReader = null;
        try {
            StringBuilder reason = null;
            if (isFTP != null && "true".equalsIgnoreCase(isFTP)) {
                fileReader = new BufferedReader(new InputStreamReader(is));
            } else {
                fileReader = new BufferedReader(new FileReader(fileDirectory));
            }
            csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            contactList = new ArrayList<>();
            // failureList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                reason = new StringBuilder();
                ContactDetDto contactDet = new ContactDetDto();
                contactDet.setCampaignId(campaignId);
                contactDet.setCampaignName(campaignName);
                //Code commented by SK Praveen Kumar for Auto call changes om 30/08/2023
//				contactDet.setDoctorName(csvRecord.get("doctor name"));
//				contactDet.setPatientName(csvRecord.get("patient name"));
//				contactDet.setContactNo(csvRecord.get("contact number"));
//				contactDet.setAppointmentDate(csvRecord.get("appointment date"));
//				contactDet.setLanguage(csvRecord.get("language"));

                contactDet.setLastFourDigits(csvRecord.get("Last 4 Digits"));
                contactDet.setCustomerMobileNumber(csvRecord.get("CUST_MOBILE_NUMBER"));
                contactDet.setTotalDue(csvRecord.get("Total Due"));
                contactDet.setMinPayment(csvRecord.get("Minimum Payment"));
                contactDet.setDueDate(csvRecord.get("Due Date"));
                contactDet.setHistoryId(historyId);
                if (validateFileData(csvRecord, reason, campaignDetList, contactDet)) {
                    contactList.add(contactDet);
                } else {
                    contactDet.setFailureReason(reason.toString());
                    failureList.add(contactDet);
                }
            }
            csvParser.close();
            if (!failureList.isEmpty()) {
                // csvPrinter = failureCsvData(fileTimestamp, failureList, failureDirectory);
            }
        } catch (IOException e) {
            logger.error("fail to parse CSV file: " + e.getMessage());
        } catch (Exception e) {
            logger.error("fail to parse CSV file: " + e.getMessage());
        } finally {
            try {
                csvParser.close();
                if (csvPrinter != null)
                    csvPrinter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return contactList;
    }

    private static CSVPrinter failureCsvData(String fileTimestamp, List<ContactDetDto> failureList,
                                             String failureDirectory) throws IOException {
        CSVPrinter csvPrinter;
        List<String> headerlist = new ArrayList<>(Arrays.asList("campaign id", "campaign name", "Last 4 Digits",
                "CUST_MOBILE_NUMBER", "Total Due", "Minimum Payment", "Due Date", "reason"));
        final CSVFormat format = CSVFormat.DEFAULT.withHeader(headerlist.toArray(new String[0]));
        Writer writer = Files.newBufferedWriter(Paths.get(failureDirectory + fileTimestamp + ".csv"));
        csvPrinter = new CSVPrinter(writer, format);
        for (ContactDetDto contactDet : failureList) {
            csvPrinter
                    .printRecord(new ArrayList<>(Arrays.asList(contactDet.getCampaignId(), contactDet.getCampaignName(),
                            contactDet.getLastFourDigits(), contactDet.getCustomerMobileNumber(), contactDet.getTotalDue(),
                            contactDet.getMinPayment(), contactDet.getDueDate(), contactDet.getFailureReason())));
        }
        csvPrinter.flush();
        return csvPrinter;
    }

    /*
     * private static boolean validateFileData(CSVRecord csvRecord, StringBuilder
     * reason, List<CampaignDetRequest> campaignDetList, ContactDetDto contactDet) {
     * boolean isValid = true; if (csvRecord.get("campaign id") == null ||
     * csvRecord.get("campaign id").isEmpty()) {
     * reason.append("Campaign ID is missing;"); isValid = false; } else {
     * CampaignDetRequest commonDetail = campaignDetList.stream() .filter(x ->
     * csvRecord.get("campaign id").equalsIgnoreCase(x.getCampaignId())).findAny()
     * .orElse(null); if (commonDetail == null) {
     * reason.append("Campaign Id is Incorrect;"); isValid = false; } } if
     * (csvRecord.get("campaign name") == null ||
     * csvRecord.get("campaign name").isEmpty()) {
     * reason.append("Campaign name is missing;"); isValid = false; } if
     * (csvRecord.get("doctor name") == null ||
     * csvRecord.get("doctor name").isEmpty()) {
     * reason.append("Doctor name is missing;"); isValid = false; } if
     * (csvRecord.get("Patient name") == null ||
     * csvRecord.get("Patient name").isEmpty()) {
     * reason.append("Patient name is missing;"); isValid = false; } if
     * (csvRecord.get("contact number") == null ||
     * csvRecord.get("contact number").isEmpty()) {
     * reason.append("Contact name is missing;"); isValid = false; } if
     * (csvRecord.get("language") == null || csvRecord.get("language").isEmpty()) {
     * reason.append("language is missing;"); isValid = false; } if
     * (csvRecord.get("appointment date") == null &&
     * csvRecord.get("appointment date").isEmpty()) {
     * reason.append("Appointment date is missing;"); isValid = false; } else { try
     * { new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(csvRecord.
     * get("appointment date")); } catch (Exception e) {
     * reason.append("Appointment date format is incorrect;"); isValid = false; } }
     * return isValid; }
     */

    private static boolean validateFileData(CSVRecord csvRecord, StringBuilder reason,
                                            List<CampaignDetRequest> campaignDetList, ContactDetDto contactDet) {
        boolean isValid = true;

        if (csvRecord.get("Last 4 Digits") == null || csvRecord.get("Last 4 Digits").isEmpty()) {
            reason.append("Last 4 Digits is missing;");
            isValid = false;
        }
        if (csvRecord.get("CUST_MOBILE_NUMBER") == null || csvRecord.get("CUST_MOBILE_NUMBER").isEmpty()) {
            reason.append("Customer Mobile Number is missing;");
            isValid = false;
        }
        if (csvRecord.get("Total Due") == null || csvRecord.get("Total Due").isEmpty()) {
            reason.append("Total Due is missing;");
            isValid = false;
        }
        if (csvRecord.get("Minimum Payment") == null || csvRecord.get("Minimum Payment").isEmpty()) {
            reason.append("Minimum Payment is missing;");
            isValid = false;
        }
        if (csvRecord.get("Due Date") == null && csvRecord.get("Due Date").isEmpty()) {
            reason.append("Due Date is missing;");
            isValid = false;
        } else {
            try {
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(csvRecord.get("Due Date"));
            } catch (Exception e) {
                reason.append("Appointment date format is incorrect;");
                isValid = false;
            }
        }
        if (!isValid)
            logger.info("validateFileData : " + reason);
        return isValid;
    }

    @Scheduled(cron = "0 0/2 * * * *")
    @PostMapping("/httpurl")
    public void executeFailure() {
        try {
            int concurrent;
            int val = 0;
            long timeDifference;
            long timeDifference1;
            long retryDifference;
            boolean isMaxAdvTime = true;
            Date currentDate = new Date();
            Date weekStartDate = null;
            Date weekEndDate = null;
            DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat time = new SimpleDateFormat("hh:mm a");
            DateFormat WeekDaytimeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat weekDayFormat = new SimpleDateFormat("EEEE");
            String weekDay = String.valueOf(weekDayFormat.format(currentDate));
            logger.info("**** Scheduler Started ****");
            List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
            Map<String, List<ContactDetDto>> contactDetMap = campaignService.getContactDet();
            logger.info("**** Campaign and Contact details fetched ****");
            if (campaignDetList != null && !campaignDetList.isEmpty()) {
                logger.info("**** Campaign details not empty ****");
                for (CampaignDetRequest campaignDetRequest : campaignDetList) {
                    //currentDate = new Date();

                    logger.info("New :: Contact Details Keys :" + contactDetMap.keySet().toString());
                    logger.info("New :: Campaign ID :" + campaignDetRequest.getCampaignId());

                    //Added on 05/02/2024
                    CampaignStatus campaignStatus = new CampaignStatus();
                    campaignStatus.setCampaignId(campaignDetRequest.getCampaignId());
                    boolean campStatus = campaignService.getCampaignStatus(campaignStatus);
                    logger.info("New :: Campaign status :" + campStatus);
                    if (campStatus) {
                        logger.info("Campaign status is enabled . Hence Campaaign scheduler is called");
                        if (contactDetMap != null && contactDetMap.containsKey(campaignDetRequest.getCampaignId())) {
                            logger.info("**** Contact details contain campaign key ****");
                            if (campaignDetRequest.getConcurrentCall() != null && !campaignDetRequest.getConcurrentCall().isEmpty()) {
                                val = Integer.parseInt(campaignDetRequest.getConcurrentCall());
                            } else {
                                val = 5;
                            }
                            String campaignDateStr = campaignDetRequest.getStartDate() + " "
                                    + campaignDetRequest.getStartTime();
                            Date campaignStartdate = dateTimeformat.parse(campaignDateStr);
                            Date campaignEndDate = dateTimeformat
                                    .parse(campaignDetRequest.getEndDate() + " " + campaignDetRequest.getEndTime());
                            for (CampaignWeekDetRequest campaignWeekDetRequest : campaignDetRequest.getWeekDaysTime()) {
                                if (weekDay.equalsIgnoreCase(campaignWeekDetRequest.getDay())) {
                                    weekStartDate = WeekDaytimeFormat.parse(campaignWeekDetRequest.getStartTime());
                                    weekEndDate = WeekDaytimeFormat.parse(campaignWeekDetRequest.getEndTime());
                                }
                            }
                            List<ContactDetDto> contactDetList = contactDetMap.get(campaignDetRequest.getCampaignId());
                            if (contactDetList != null && !contactDetList.isEmpty()) {
                                logger.info("**** Contact details condition  ****");
                                int i = 1, j = 1;
                                for (ContactDetDto contactDetDto : contactDetList) {
                                    logger.info("**** inside contact details loop  ****");
                                    logger.info("**** Contact ID : ****" + contactDetDto.getContactId());
                                    int countToCall = campaignService.getCountToCall(productID);
                                    int countOfDialNo = contactDetList.size();
                                    val = Math.min(countOfDialNo, val);
                                    logger.info("Count to dial no : " + countOfDialNo);
                                    if (countToCall != 0) {
                                        concurrent = Math.min(countToCall, val); //
                                    } else {
                                        concurrent = val;
                                    }
                                    isMaxAdvTime = true;
                                    Date appdate = dateTimeformat.parse(contactDetDto.getDueDate());
                                    Date appdateCallBefore = dateformat.parse(contactDetDto.getDueDate());
                                    logger.info("CampaignId: " + campaignDetRequest.getCampaignId() + "   **** outside date condition  **** " + "  appdate:" + appdate + " campaignStartdate :" + campaignStartdate + "  campaignEndDate: " + campaignEndDate);

                                    if (appdate.after(campaignStartdate) && appdate.before(campaignEndDate)) {
                                        logger.info("CampaignId: " + campaignDetRequest.getCampaignId() + "   **** inside contact details loop  **** " + "customer mobile number:" + contactDetDto.getCustomerMobileNumber() + "  appdate:" + appdate + " campaignStartdate :" + campaignStartdate + "  campaignEndDate: " + campaignEndDate);
                                        timeDifference = appdate.getTime() - currentDate.getTime();
                                        timeDifference1 = appdateCallBefore.getDate() - currentDate.getDate();


                                        LocalDate start = LocalDate.of(currentDate.getYear(), (currentDate.getMonth() + 1), currentDate.getDate());
                                        LocalDate end = LocalDate.of(appdateCallBefore.getYear(), (appdateCallBefore.getMonth() + 1), appdateCallBefore.getDate());
                                        long dayDifference = start.until(end, ChronoUnit.DAYS);

                                        logger.info("**** currentDate " + " Year :" + currentDate.getYear() + " Month :" + currentDate.getMonth() + "Date :" + currentDate.getDate());
                                        logger.info("**** DueDate " + " Year :" + appdateCallBefore.getYear() + " Month :" + appdateCallBefore.getMonth() + "Date :" + appdateCallBefore.getDate());


                                        logger.info("**** inside contact details loop  ****timeDifference: " + timeDifference + "  Time difference1:  " + timeDifference1 + "  dayDifference:" + dayDifference + "  currentDate: " + currentDate +
                                                "**** inside contact details loop  ****  CampaignId:  " + campaignDetRequest.getCampaignId() + "  CallBefore:" + campaignDetRequest.getCallBefore() + "  CampaignName: " + campaignDetRequest.getCampaignName());
                                        logger.info("New ::  call Before =" + campaignDetRequest.getCallBefore());

                                        if (dayDifference == Integer.parseInt(campaignDetRequest.getCallBefore())) {
                                            if ("0".equalsIgnoreCase(campaignDetRequest.getCallBefore())) {
                                                long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
                                                String[] hourMin = campaignDetRequest.getMaxAdvNotice().split(":");
                                                long minutes = (Integer.parseInt(hourMin[0]) * 60)
                                                        + Integer.parseInt(hourMin[1]);
                                                logger.info("CampaignId: " + campaignDetRequest.getCampaignId() + "   **** minutesDifference :" + minutesDifference + " contact details loop  **** minutes:" + minutes);
                                                if (minutesDifference < minutes) {
                                                    isMaxAdvTime = false;
                                                    listOfActionIdStore.clear();
                                                    logger.info("List of action id is cleared...");
                                                }
                                            }
                                            logger.info("CampaignId: " + campaignDetRequest.getCampaignId() + "   **** isMaxAdvTime :" + isMaxAdvTime + " contact details loop  **** appdate:" + appdate + " campaignStartdate :" + campaignStartdate + "  campaignEndDate: " + campaignEndDate);
                                            if (isMaxAdvTime) {
                                                currentDate = new Date(); //Code added by SK Praveen Kumar for bug fix

                                                logger.info("New :: Week Start Date :" + weekStartDate);
                                                logger.info("New :: Week End Date :" + weekEndDate);
                                                logger.info("New :: Currend Date :" + currentDate);
                                                if (WeekDaytimeFormat.parse(WeekDaytimeFormat.format(currentDate))
                                                        .after(weekStartDate)
                                                        && WeekDaytimeFormat.parse(WeekDaytimeFormat.format(currentDate))
                                                        .before(weekEndDate)) {
                                                    logger.info("CampaignId: " + campaignDetRequest.getCampaignId() + "    **** WeekDaytimeFormat weekEndDate:" + weekEndDate + " contact details loop  **** appdate:" + appdate + " campaignStartdate :" + campaignStartdate + "  campaignEndDate: " + campaignEndDate);
                                                    if (contactDetDto.getCallRetryCount() != null && (Integer
                                                            .parseInt(contactDetDto.getCallRetryCount()) <= Integer
                                                            .parseInt(campaignDetRequest.getRetryCount()))) {
                                                        Date updateddate = dateTimeformat
                                                                .parse(contactDetDto.getUpdatedDate());
                                                        retryDifference = TimeUnit.MILLISECONDS
                                                                .toMinutes(currentDate.getTime() - updateddate.getTime());
                                                        if ("New".equalsIgnoreCase(contactDetDto.getCallStatus())
                                                                || retryDifference > Integer
                                                                .parseInt(campaignDetRequest.getRetryDelay())) {
                                                            logger.info(
                                                                    "**** All Conditions are satisfied going to make call For the contact ID : " + contactDetDto.getContactId() + "****");
                                                            Date dueDate = dateTimeformat.parse(contactDetDto.getDueDate());
                                                            Long dueUnixTime = dueDate.getTime() / 1000;

//                                                            logger.info("**** Inside Thread API Thread API request****");
                                                            Thread t = new Thread(() -> processCallApi(contactDetDto, campaignDetRequest, dueUnixTime));
                                                            t.start();

//														updateCallDet(i, contactDetDto.getContactId(),
//																contactDetDto.getCallRetryCount());

//                                                            for (int count=0;count<concurrent;count++){
//                                                            Thread t = new Thread(obj1);
//                                                            logger.info("New :: Thread Started :");
//                                                            t.start();
//                                                            if (contactDetDto.getCustomerMobileNumber().isEmpty() || contactDetDto.getCustomerMobileNumber() != null) {
//                                                                t.sleep(1000);
//                                                            } else {
//                                                                t.join();
//                                                            }
//                                                        }

//                                                            logger.info("New :: Concurrent Value :" + concurrent);
//                                                            if (j > concurrent) {
//                                                                logger.info("New :: Thread is going to Sleep");
//                                                                Thread.sleep(10000 * concurrent);
//                                                                logger.info("New :: Thread Resumed");
//                                                                j = 0;
//                                                            }
//                                                            i++;
//                                                            j++;
                                                        } else {
                                                            logger.info("New :: contact details status is either not new or retry difference is greater than campaing retry difference for the contact ID : " + contactDetDto.getContactId());
                                                            logger.info("New :: contact details status :" + contactDetDto.getCallStatus() + " :: Campaign Retry  :" + campaignDetRequest.getRetryCount() + " Retry Difference :" + retryDifference);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        logger.info("New :: Campaign Status is not true. Hence campaign API scheduler is not invoked");
                    }
                }
            }
        }
//		catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        catch (Exception e) {
            logger.info("Error Occured in call Making due to : " + e.getMessage());
            e.printStackTrace();
        }
        // return null;
    }

    private void processCallApi(ContactDetDto contactDetDto, CampaignDetRequest campaignDetRequest, Long dueUnixTime) {
        logger.info("**** Inside Thread API Thread API request****");
        String dialplan = null;
        logger.info("New :: AutoDial campaign ID set to property is :" + autoDialCamp);
        if (autoDialCamp != null && autoDialCamp.equalsIgnoreCase(campaignDetRequest.getCampaignId())) {
            dialplan = postDueDialPlan;
        } else {
            dialplan = preDueDialPlan;
        }
        Unirest.setTimeouts(0, 0);
        try {
            ContactDetDto dummycontact = new ContactDetDto();
            dummycontact.setCampaignId(contactDetDto.getCampaignId());
            dummycontact.setCampaignName(contactDetDto.getCampaignName());
            dummycontact.setDueDate(Long.toString(dueUnixTime));
            dummycontact.setAppointmentDate(contactDetDto.getDueDate());
            dummycontact.setCallStatus(contactDetDto.getContactId());
            campaignService.createDummyContact(dummycontact);

            logger.info("*************Dummy Contact*********");
            logger.info("*************Contact ID : *********" + contactDetDto.getContactId());
            logger.info("campaignId" + dummycontact.getCampaignId());
            logger.info("getCustomerMobileNumber" + dummycontact.getCampaignName());
            logger.info("language" + dummycontact.getLanguage());
            logger.info("UnixTime" + dummycontact.getContactNo());
            logger.info("DueDate" + dummycontact.getAppointmentDate());
            logger.info("ContactId" + dummycontact.getCallStatus());

            String request = "{\r\n    \"outcallerid\": \"044288407\",\r\n    \"siptrunk\": \"Avaya\",\r\n  "
                    + "  \"phone\": \"" + contactDetDto.getCustomerMobileNumber() + "\",\r\n   "
                    + "\"language\": \"" + contactDetDto.getLanguage() + "\",\r\n "
                    + "  \"productid\": \"" + productID + "\",\r\n   "
                    + " \"amount\": \"" + contactDetDto.getMinPayment() + "\",\r\n    "
                    + " \"last4digit\": \"" + contactDetDto.getLastFourDigits() + "\",\r\n "
                    + "   \"duedate\": \"" + contactDetDto.getDueDate() + "\",\r\n   "
                    + "  \"unixtime\": \"" + dueUnixTime + "\",\r\n    \"timezone\": \"GST\",\r\n    \"dialplan\": \"" + dialplan + "\",\r\n   "
                    + " \"actionid\": \"" + contactDetDto.getContactId() + "\"\r\n}";

            logger.info(request);

            HttpResponse<String> response = Unirest.post(callApiAutoCall)
                    .header("Content-Type", "application/json")
                    .body("{\r\n    \"outcallerid\": \"044288407\",\r\n    \"siptrunk\": \"Avaya\",\r\n  "
                            + "  \"phone\": \"" + contactDetDto.getCustomerMobileNumber() + "\",\r\n   "
                            + "\"language\":\"" + contactDetDto.getLanguage() + "\",\r\n "
                            + "  \"productid\": \"" + productID + "\",\r\n   "
                            + " \"amount\": \"" + contactDetDto.getMinPayment() + "\",\r\n    "
                            + " \"last4digit\": \"" + contactDetDto.getLastFourDigits() + "\",\r\n "
                            + "   \"duedate\": \"" + contactDetDto.getDueDate() + "\",\r\n   "
                            + "  \"unixtime\": \"" + dueUnixTime + "\",\r\n    \"timezone\": \"GST\",\r\n    \"dialplan\": \"" + dialplan + "\",\r\n   "
                            + " \"actionid\": \"" + contactDetDto.getContactId() + "\"\r\n}")
                    .asString();
            logger.info(
                    "**** Inside Thread API Thread API response****");
/*              Example response from the api
                {
                    "ActionID": "169746",
                        "Message": "Originate successfully queued",
                        "Response": "Success"
                }
                */

            String responses = response.toString();
            String contactId = contactDetDto.getContactId();
            if (responses.contains(contactId) && responses.contains("Success")) {
                listOfActionIdStore.add(contactId);
            } else {
                System.out.println("Action not found or unsuccessful.");
            }
            String customerMobile = contactDetDto.getCustomerMobileNumber();
            if (listOfActionIdStore.contains(contactId)) {
                logger.info(response.getBody());
                logger.info("Response for this : " + contactId + " " + customerMobile);
//                    campaignService.getMobileDialed(contactId,productID,customerMobile,"Success");
                System.out.println("Call Done");
            } else {
                logger.info(response.getBody());
                logger.info("Response for this :  " + contactId + " " + customerMobile);
//                    campaignService.getMobileDialed(contactId,productID,customerMobile,"Error");
                System.out.println("Call error");
            }
//                                                                    if (responses.contains("Success")){
//                                                                       campaignService.markasmobileand
//                                                                    }
        } catch (UnirestException e1) {
            logger.info(
                    "**** Inside Exception clause API Thread API ****");
            logger.error(e1.getMessage());
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        logger.info("Product Id : " + productID);

        System.out.println("Request Success");
        CloseableHttpClient httpclient = HttpClients
                .createDefault();
        HttpPost httppost = new HttpPost(callApiAutoCall);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("outcallerid", "044288407"));
        nvps.add(new BasicNameValuePair("siptrunk", "Avaya"));
        nvps.add(new BasicNameValuePair("phone", contactDetDto.getCustomerMobileNumber()));
        nvps.add(new BasicNameValuePair("amount", contactDetDto.getMinPayment()));
        nvps.add(new BasicNameValuePair("last4digit", contactDetDto.getLastFourDigits()));
        nvps.add(new BasicNameValuePair("language", contactDetDto.getLanguage()));
        nvps.add(new BasicNameValuePair("productID", productID));
        nvps.add(new BasicNameValuePair("unixtime", "1695454737"));
        nvps.add(new BasicNameValuePair("timezone", "GST"));
        nvps.add(new BasicNameValuePair("dialplan", dialplan));
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        nvps.add(new BasicNameValuePair("actionid", contactDetDto.getContactId()));
        for (NameValuePair name : nvps) {
            logger.info("**** Call Request Parameters executeFailureAutoCalls****");
            logger.info("Name value pair :" + name.getValue());
        }


        try {
            httppost.setEntity(
                    new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*
         * HttpResponse httpresponse; try { httpresponse =
         * httpclient.execute(httppost); logger.
         * info("**** Call made successfully for below details executeFailureAutoCalls****"
         * ); logger.info("custphone===== " +
         * contactDetDto.getCampaignName());
         * logger.info("docname===== " +
         * contactDetDto.getDueDate()); logger.info("docname===== "
         * + contactDetDto.getLastFourDigits());
         * logger.info("docname===== " +
         * contactDetDto.getMinPayment());
         * logger.info("docname===== " +
         * contactDetDto.getCustomerMobileNumber());
         * logger.info("docname===== " +
         * contactDetDto.getCallStatus());
         * logger.info("docname===== " +
         * contactDetDto.getTotalDue()); System.out.println(
         * "Time for " + System.currentTimeMillis() + " : " +
         * contactDetDto.getCustomerMobileNumber()); Scanner sc =
         * new Scanner( httpresponse.getEntity().getContent());
         * System.out.println(httpresponse.getEntity().getContent()
         * .toString()); while (sc.hasNext()) {
         * logger.info("***Call response***");
         * logger.info(sc.nextLine()); } } catch (IOException e) {
         * e.printStackTrace(); }
         *
         * try { httpclient.close(); } catch (IOException e) {
         * e.printStackTrace(); }
         */
    }

    private void process(ContactDetDto contactDetDto, CampaignDetRequest campaignDetRequest, Long dueUnixTime, int concurrent) {
        try {
            Thread[] threads = new Thread[concurrent];
            for (int count = 0; count < concurrent; count++) {
                threads[count] = new Thread(() -> {
                    try {
                        logger.info("**** Inside Thread API Thread API request****");
                        Runnable obj1 = () -> {
                            logger.info(
                                    "**** Inside Thread API Thread API request****");
                            String dialplan = null;
                            logger.info("New :: AutoDial campaign ID set to property is :" + autoDialCamp);
                            if (autoDialCamp != null && autoDialCamp.equalsIgnoreCase(campaignDetRequest.getCampaignId())) {
                                dialplan = postDueDialPlan;
                            } else {
                                dialplan = preDueDialPlan;
                            }
                            Unirest.setTimeouts(0, 0);
                            try {
                                ContactDetDto dummycontact = new ContactDetDto();
                                dummycontact.setCampaignId(contactDetDto.getCampaignId());
                                dummycontact.setCampaignName(contactDetDto.getCampaignName());
                                dummycontact.setDueDate(Long.toString(dueUnixTime));
                                dummycontact.setAppointmentDate(contactDetDto.getDueDate());
                                dummycontact.setCallStatus(contactDetDto.getContactId());
                                campaignService.createDummyContact(dummycontact);

                                logger.info("*************Dummy Contact*********");
                                logger.info("*************Contact ID : *********" + contactDetDto.getContactId());
                                logger.info("campaignId" + dummycontact.getCampaignId());
                                logger.info("getCustomerMobileNumber" + dummycontact.getCampaignName());
                                logger.info("language" + dummycontact.getLanguage());
                                logger.info("UnixTime" + dummycontact.getContactNo());
                                logger.info("DueDate" + dummycontact.getAppointmentDate());

                                String request ="{\r\n    \"outcallerid\": \"044288407\",\r\n    \"siptrunk\": \"Avaya\",\r\n  "
                                        + "  \"phone\": \"" + contactDetDto.getCustomerMobileNumber() + "\",\r\n   "
                                        + "\"language\":\"" + contactDetDto.getLanguage() + "\",\r\n "
                                        + "  \"productid\": \"" + productID + "\",\r\n   "
                                        + " \"amount\": \"" + contactDetDto.getMinPayment() + "\",\r\n    "
                                        + " \"last4digit\": \"" + contactDetDto.getLastFourDigits() + "\",\r\n "
                                        + "   \"duedate\": \"" + contactDetDto.getDueDate() + "\",\r\n   "
                                        + "  \"unixtime\": \"" + dueUnixTime + "\",\r\n    \"timezone\": \"GST\",\r\n    \"dialplan\": \"" + dialplan + "\",\r\n   "
                                        + " \"actionid\": \"" + contactDetDto.getContactId() + "\"\r\n}";

                                logger.info(request);
                                logger.info("Product Id : " + productID);
                                HttpResponse<String> response = Unirest.post(callApiAutoCall)
                                        .header("Content-Type", "application/json")
                                        .body("{\r\n    \"outcallerid\": \"044288407\",\r\n    \"siptrunk\": \"Avaya\",\r\n  "
                                                + "  \"phone\": \"" + contactDetDto.getCustomerMobileNumber() + "\",\r\n   "
                                                + "\"language\":\"" + contactDetDto.getLanguage() + "\",\r\n "
                                                + "  \"productid\": \"" + productID + "\",\r\n   "
                                                + " \"amount\": \"" + contactDetDto.getMinPayment() + "\",\r\n    "
                                                + " \"last4digit\": \"" + contactDetDto.getLastFourDigits() + "\",\r\n "
                                                + "   \"duedate\": \"" + contactDetDto.getDueDate() + "\",\r\n   "
                                                + "  \"unixtime\": \"" + dueUnixTime + "\",\r\n    \"timezone\": \"GST\",\r\n    \"dialplan\": \"" + dialplan + "\",\r\n   "
                                                + " \"actionid\": \"" + contactDetDto.getContactId() + "\"\r\n}")
                                        .asString();
                                logger.info(
                                        "**** Inside Thread API Thread API response****");
                                logger.info(response.getBody());
                            } catch (UnirestException e1) {
                                logger.info(
                                        "**** Inside Exception clause API Thread API ****");
                                logger.error(e1.getMessage());
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            CloseableHttpClient httpclient = HttpClients
                                    .createDefault();
                            HttpPost httppost = new HttpPost(callApiAutoCall);
                            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                            nvps.add(new BasicNameValuePair("outcallerid", "044288407"));
                            nvps.add(new BasicNameValuePair("siptrunk", "Avaya"));
                            nvps.add(new BasicNameValuePair("phone", contactDetDto.getCustomerMobileNumber()));
                            nvps.add(new BasicNameValuePair("amount", contactDetDto.getMinPayment()));
                            nvps.add(new BasicNameValuePair("last4digit", contactDetDto.getLastFourDigits()));
                            nvps.add(new BasicNameValuePair("language", contactDetDto.getLanguage()));
                            nvps.add(new BasicNameValuePair("productID", productID));
                            nvps.add(new BasicNameValuePair("unixtime", "1695454737"));
                            nvps.add(new BasicNameValuePair("timezone", "GST"));
                            nvps.add(new BasicNameValuePair("dialplan", dialplan));
                            long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                            nvps.add(new BasicNameValuePair("actionid", contactDetDto.getContactId()));
                            for (NameValuePair name : nvps) {
                                logger.info("**** Call Request Parameters executeFailureAutoCalls****");
                                logger.info("Name value pair :" + name.getValue());
                            }

                            try {
                                httppost.setEntity(
                                        new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                            } catch (
                                    UnsupportedEncodingException e) {
                                logger.error("Error : " + e.getMessage());
                                e.printStackTrace();
                            }
                        };
                        synchronized (obj1) {
                            obj1.run();
                        }
                    } catch (Exception e) {
                        logger.warn("Exception in thread: " + e.getMessage());
                    }
                });
                logger.info("New :: Thread Started :");
                threads[count].start();
            }
            for (Thread thread : threads) {
                logger.info("Call completed count: " + thread);
                thread.join();
            }
        } catch (InterruptedException e) {
            logger.warn("Thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    @PostMapping("/createCampaign")
    public ResponseEntity<GenericResponse> createCampaign(@RequestBody CampaignDetRequest campaignDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.createCampaign(campaignDetRequest);
    }

    @GetMapping("/getCampaignDetail")
    public ResponseEntity<GenericResponse> getCampaignDetail()
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("Invoking Get Campaign Detail");
        return campaignService.getCampaignDetail();
    }

    @PostMapping("/updateCampaign")
    public ResponseEntity<GenericResponse> updateCampaign(@RequestBody CampaignDetRequest campaignDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("Updating Campaign Detail");
        return campaignService.updateCampaign(campaignDetRequest);
    }

    @PostMapping("/updateCallDetail")
    public ResponseEntity<GenericResponse> updateCallDetail(@RequestBody UpdateAutoCallRequest updateAutoCallRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("**********UPDATE CALL DETAILS INPUT**********");
        logger.info("getActionid: " + updateAutoCallRequest.getActionid());
        logger.info("getPhone: " + updateAutoCallRequest.getPhone());
        logger.info("getCallstart: " + updateAutoCallRequest.getCallstart());
        logger.info("getCallanswer: " + updateAutoCallRequest.getCallanswer());
        logger.info("getCallend: " + updateAutoCallRequest.getCallend());
        logger.info("getCalltalktime: " + updateAutoCallRequest.getCalltalktime());
        logger.info("getCallduration: " + updateAutoCallRequest.getCallduration());
        logger.info("getDisposition: " + updateAutoCallRequest.getDisposition());
        logger.info("getDialstatus: " + updateAutoCallRequest.getDialstatus());
        logger.info("getHangupcode: " + updateAutoCallRequest.getHangupcode());
        logger.info("getHangupreason: " + updateAutoCallRequest.getHangupreason());
        logger.info("getHanguptext: " + updateAutoCallRequest.getHanguptext());


        UpdateCallDetRequest updateCallDetRequest = new UpdateCallDetRequest();
        updateCallDetRequest.setCallDuration(updateAutoCallRequest.getCallduration());
        updateCallDetRequest.setContactId(updateAutoCallRequest.getActionid());
        updateCallDetRequest.setCallerResponse("0");
        //Added on 28022024
        if (updateAutoCallRequest.getDisposition() != null)
            updateCallDetRequest.setCallStatus(updateAutoCallRequest.getDisposition());

        else if (updateAutoCallRequest.getDialstatus() != null)
            updateCallDetRequest.setCallStatus(updateAutoCallRequest.getDialstatus());
            //

            //	if("ANSWER".equalsIgnoreCase(updateAutoCallRequest.getDialstatus()))
            //	updateCallDetRequest.setCallStatus("ANSWERED");
        else
            updateCallDetRequest.setCallStatus(updateAutoCallRequest.getDialstatus());
        updateCallDetRequest.setHangupcode(updateAutoCallRequest.getHangupcode());


        return campaignService.updateCallDetail(updateCallDetRequest);
    }

    @PostMapping("/validateCampaignName")
    public ResponseEntity<GenericResponse> validateCampaignName(@RequestBody CampaignDetRequest campaignDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.validateCampaignName(campaignDetRequest);
    }

    @PostMapping("/summaryReport")
    public ResponseEntity<GenericResponseReport> summaryReport(@RequestBody ReportRequest reportRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.summaryReport(reportRequest);
    }

    @PostMapping("/detailReport")
    public ResponseEntity<GenericResponseReport> detailReport(@RequestBody ReportRequest reportRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.detailReport(reportRequest);
    }

    @PostMapping("/downloadSummaryReport")
    public ResponseEntity<InputStreamResource> downloadSummaryReport(@RequestBody ReportRequest reportRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.downloadSummaryReport(reportRequest);
    }

    @PostMapping("/downloadDetailReport")
    public ResponseEntity<InputStreamResource> downloadDetailReport(@RequestBody ReportRequest reportRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.downloadDetailReport(reportRequest);
    }

    @PostMapping("/getUploadhistory")
    public ResponseEntity<GenericResponse> getUploadHistory(@RequestBody ReportRequest reportRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.getUploadHistory(reportRequest);
    }

    @PostMapping("/deleteContactByHistory")
    public ResponseEntity<GenericResponse> deleteContactByHistory(
            @RequestBody UpdateCallDetRequest updateCallDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.deleteContactByHistory(updateCallDetRequest);
    }

    @PostMapping("/uploadContactDetail")
    public ResponseEntity<GenericResponse> uploadContactDetail(@RequestParam("file") MultipartFile file,
                                                               @RequestParam(name = "campaignId", required = false) String campaignId,
                                                               @RequestParam(name = "campaignName", required = false) String campaignName)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        String message = null;
        CSVPrinter csvPrinter = null;
        boolean isUploaded = true;
        List<ContactDetDto> failureList = null;
        if ("text/csv".equalsIgnoreCase(file.getContentType()) || file.getOriginalFilename().endsWith(".csv")) {
            try {
                failureList = new ArrayList<>();
                CampaignDetRequest campaignDetRequest = new CampaignDetRequest();
                campaignDetRequest.setCampaignId(campaignId);
                campaignDetRequest.setCampaignName(campaignName);
                List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
                BigInteger historyId = getUploadHistoryid(campaignDetRequest, file.getOriginalFilename());
                List<ContactDetDto> contactDetList = csvToData(file.getInputStream(), historyId, isFTP, fileDirectory,
                        new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()), failureDirectory, campaignDetList,
                        failureList, campaignId, campaignName);
                ContactDetDto commonDetail = contactDetList.stream()
                        .filter(x -> campaignId.equalsIgnoreCase(x.getCampaignId())).findAny().orElse(null);
                logger.info("****Converted CSV DATA to Object****");
                if (contactDetList.isEmpty()) {
                    message = "Upload failed! Invalid data or Contact details already exist for same Appointment date and time ";
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                            .body(new GenericResponse(message, "Failed"));
                }
                if (commonDetail != null) {
                    logger.info("****Inserting contact details to DB Table****");
                    for (ContactDetDto contactDetDto : contactDetList) {
                        isUploaded = campaignService.createContact(contactDetDto);
                        if (!isUploaded) {
                            contactDetDto.setFailureReason(
                                    "Contact details already exist for same Appointment Date and Time");
                            failureList.add(contactDetDto);
                        }
                    }
                    message = "Uploaded the file successfully: " + file.getOriginalFilename();
                    if (!failureList.isEmpty()) {
                        csvPrinter = failureCsvData(new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date()),
                                failureList, failureDirectory);
                        message = "One or more Contacts not uploaded due to some invalid data!";
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(message, "Success"));
                } else if (commonDetail == null && !contactDetList.isEmpty()) {
                    message = "Upload failed. Identified incorrect Campaign id, expected Id is " + campaignId;
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                            .body(new GenericResponse(message, "Failed"));
                }
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                logger.error("Error occured in uploadContactDetail:: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body(new GenericResponse(message, "Failed"));
            } finally {
                if (csvPrinter != null)
                    csvPrinter.close();
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(message, "Failed"));
    }

    private void updateCallDet(int i, String contactId, String C) {
        UpdateCallDetRequest UpdateCallDetRequest = new UpdateCallDetRequest();
        UpdateCallDetRequest.setContactId(contactId);
        UpdateCallDetRequest.setRetryCount(Integer.parseInt(contactId));
        if (i == 0) {
            UpdateCallDetRequest.setCallStatus("Failed");
        } else if (i % 3 == 0) {
            UpdateCallDetRequest.setCallStatus("ANSWERED");
            UpdateCallDetRequest.setCallerResponse("2");
            UpdateCallDetRequest.setCallDuration("20");
        } else if (i % 5 == 0) {
            UpdateCallDetRequest.setCallStatus("Failed");
        } else if (i % 7 == 0) {
            UpdateCallDetRequest.setCallStatus("ANSWERED");
            UpdateCallDetRequest.setCallerResponse("3");
            UpdateCallDetRequest.setCallDuration("20");
        } else if (i % 2 == 0) {
            UpdateCallDetRequest.setCallStatus("ANSWERED");
            UpdateCallDetRequest.setCallerResponse("1");
            UpdateCallDetRequest.setCallDuration("20");
        } else {
            UpdateCallDetRequest.setCallStatus("ANSWERED");
            UpdateCallDetRequest.setCallDuration("5");
        }
        campaignService.updateCallDetail(UpdateCallDetRequest);
    }

    private static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            logger.error("IOException: %s%n", e);
        }
    }


    /*
     * @PostMapping("/httpurlautocalls") public void executeFailureAutoCalls() {
     *
     * logger.info("***Call executeFailureAutoCalls started***");
     *
     * try { List<CustomerDataDto> customerList = campaignService.getCustomerData();
     *
     * for (CustomerDataDto customerDataDto : customerList) {
     *
     * logger.
     * info("**** All Conditions are satisfied going to make call For executeFailureAutoCalls **** "
     * ); Runnable obj1 = () -> { System.out.println("Request Success");
     * CloseableHttpClient httpclient = HttpClients.createDefault(); HttpPost
     * httppost = new HttpPost(callApiAutoCall); List<NameValuePair> nvps = new
     * ArrayList<NameValuePair>(); nvps.add(new BasicNameValuePair("outcallerid",
     * "044288407")); nvps.add(new BasicNameValuePair("siptrunk", "Avaya"));
     * nvps.add(new BasicNameValuePair("phone", customerDataDto.getMobileNumber()));
     * nvps.add(new BasicNameValuePair("amount", customerDataDto.getTotalDue()));
     * nvps.add(new BasicNameValuePair("last4digit",
     * customerDataDto.getLastFourDigits())); nvps.add(new
     * BasicNameValuePair("duedate", customerDataDto.getDueDate())); nvps.add(new
     * BasicNameValuePair("unixtime", "1695454737")); nvps.add(new
     * BasicNameValuePair("timezone", "IST")); nvps.add(new
     * BasicNameValuePair("dialplan", "autodial")); nvps.add(new
     * BasicNameValuePair("actionid", "1234567890")); try { httppost.setEntity(new
     * UrlEncodedFormEntity(nvps, HTTP.UTF_8)); } catch
     * (UnsupportedEncodingException e) { e.printStackTrace(); } HttpResponse
     * httpresponse; try { httpresponse = httpclient.execute(httppost); logger.
     * info("**** Call made successfully for below details executeFailureAutoCalls****"
     * ); logger.info("custphone===== " + customerDataDto.getCampaignName());
     * logger.info("custname===== " + customerDataDto.getCutomerDataId());
     * logger.info("docname===== " + customerDataDto.getDueDate());
     * logger.info("docname===== " + customerDataDto.getLastFourDigits());
     * logger.info("docname===== " + customerDataDto.getMinimumPayment());
     * logger.info("docname===== " + customerDataDto.getMobileNumber());
     * logger.info("docname===== " + customerDataDto.getMobileNumber());
     * logger.info("docname===== " + customerDataDto.getStatus());
     * logger.info("docname===== " + customerDataDto.getTotalDue());
     *
     * Scanner sc = new Scanner(httpresponse.getEntity().getContent());
     * System.out.println(httpresponse.getEntity().getContent().toString()); while
     * (sc.hasNext()) { logger.info("***Call response executeFailureAutoCalls***");
     * logger.info(sc.nextLine()); } } catch (IOException e) { e.printStackTrace();
     * }
     *
     * try { httpclient.close(); } catch (IOException e) { e.printStackTrace(); } };
     *
     * Thread t = new Thread(obj1); t.start(); } }
     *
     * catch (Exception e) { logger.info("Error Occured in call Making due to : " +
     * e.getMessage()); e.printStackTrace(); } // return null; }
     */
    @PostMapping("/getRetryReport")
    public ResponseEntity<GenericResponse> getRetryReport(@RequestBody ReportRequest reportRequest)
            throws Exception {
        return campaignService.getRetryReport(reportRequest);
    }

    @PostMapping("/getLeadWiseSummary")
    public ResponseEntity<GenericResponseReport> getLeadWiseSummary(@RequestBody ReportRequest reportRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.getLeadWiseSummary(reportRequest);
    }

    @PostMapping("/getCallVolumeReport")
    public ResponseEntity<GenericResponseReport> getCallVolumeReport(@RequestBody ReportRequest reportRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.getCallVolumeReport(reportRequest);
    }


    @GetMapping("/getRealTimeDashboard")
    public ResponseEntity<GenericResponse> getRealTimeData()
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.getRealTimeDashboard();
    }

    @GetMapping("/getCount")
    public int getCountToCall()
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return campaignService.getCountToCall("EN_NeuronMember");
    }

    @PostMapping("/createTenant")
    public ResponseEntity<GenericResponse> createTenant(@RequestBody TenantDetRequest tenantDetRequest){
        return campaignService.createTenant(tenantDetRequest);
    }
}

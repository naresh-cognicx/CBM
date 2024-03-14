package com.cognicx.AppointmentRemainder.util;

import com.cognicx.AppointmentRemainder.service.TenantService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class LogsFileTransferScheduler {

    @Value("${SOURCE_DIRECTORY}")
    private String SOURCE_DIRECTORY;

    @Value("${DESTINATION_DIRECTORY}")
    private String DESTINATION_DIRECTORY;
    private static Logger logger = LoggerFactory.getLogger(LogsFileTransferScheduler.class);

    @Scheduled(cron = "0 10 0 * * *")// schedule a task to run daily at 12:10 AM
    public void transferFiles() {

        logger.info("Scheduled task started at " + LocalDateTime.now());

        File sourceDir = new File(SOURCE_DIRECTORY);
        File destinationDir = new File(DESTINATION_DIRECTORY);

        if (sourceDir.exists() && sourceDir.isDirectory()) {
            File[] gzFiles = sourceDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".gz") && name.toLowerCase().startsWith("reminder"));

            if (gzFiles != null && gzFiles.length > 0) {

                Arrays.stream(gzFiles).forEach(gzFile -> {
                    try {
                        FileUtils.copyFileToDirectory(gzFile, destinationDir);
                        if (gzFile.delete()) {
                            logger.info("Deleted source file: " + gzFile.getName());
                        } else {
                            logger.error("Failed to delete source file: " + gzFile.getName());
                        }
                    } catch (IOException e) {
                        logger.error("Error copying file " + gzFile.getName() + ": " + e.getMessage());
                    }
                });

                logger.info(".gz file transfer completed successfully.");
            } else {
                logger.error("No .gz files found in the source directory.");
            }
        } else {
            logger.error("Source directory does not exist or is not a directory.");
        }
        logger.info("Scheduled task completed at " + LocalDateTime.now());
    }
}

package com.cognicx.AppointmentRemainder.util;

import org.apache.commons.io.FileUtils;
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
    private  String SOURCE_DIRECTORY;
    @Value("${DESTINATION_DIRECTORY}")
    private  String DESTINATION_DIRECTORY;

//    @Scheduled(cron = "0/5 * * * * *") // Run daily at 11 pm
    public void transferFiles() {
        System.out.println("Scheduled task started at " + LocalDateTime.now());

        File sourceDir = new File(SOURCE_DIRECTORY);
        File destinationDir = new File(DESTINATION_DIRECTORY);

        if (sourceDir.exists() && sourceDir.isDirectory()) {
            File[] gzFiles = sourceDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".gz") && name.toLowerCase().startsWith("reminder"));

            if (gzFiles != null && gzFiles.length > 0) {

                Arrays.stream(gzFiles).forEach(gzFile -> {
                    try {
                        FileUtils.copyFileToDirectory(gzFile, destinationDir);
//                        if (gzFile.delete()) {
//                            System.out.println("Deleted source file: " + gzFile.getName());
//                        } else {
//                            System.err.println("Failed to delete source file: " + gzFile.getName());
//                        }
                    } catch (IOException e) {
                        System.err.println("Error copying file " + gzFile.getName() + ": " + e.getMessage());
                    }
                });

                System.out.println(".gz file transfer completed successfully.");
            } else {
                System.out.println("No .gz files found in the source directory.");
            }
        } else {
            System.err.println("Source directory does not exist or is not a directory.");
        }

        System.out.println("Scheduled task completed at " + LocalDateTime.now());
    }
}

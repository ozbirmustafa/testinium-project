package com.beymen.utils;

import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    private static final Logger logger = LoggerHelper.getLogger(FileUtils.class);

    public static void writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error writing to file {}: {}", fileName, e.getMessage());
        }
    }

    /**
     * Bu metot, pathi belirtilen text dosyasını siler.
     *
     * @param fileName Silinecek dosyanın adı veya yolu.
     */
    public static void deleteFile(String fileName) {
        Path filePath = Paths.get(fileName);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("File deleted successfully: {}", fileName);
            } else {
                logger.info("File not found, no deletion needed: {}", fileName);
            }
        } catch (IOException e) {
            logger.error("Error deleting file {}: {}", fileName, e.getMessage());
        }
    }
}
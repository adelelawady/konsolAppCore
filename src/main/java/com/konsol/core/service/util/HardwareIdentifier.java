package com.konsol.core.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HardwareIdentifier {

    /**
     * Generate a unique hardware ID based on system properties
     */
    public String generateHardwareId() {
        try {
            List<String> hardwareInfo = new ArrayList<>();

            // CPU Info
            hardwareInfo.add(System.getenv("PROCESSOR_IDENTIFIER"));
            hardwareInfo.add(System.getenv("PROCESSOR_ARCHITECTURE"));
            hardwareInfo.add(System.getenv("PROCESSOR_ARCHITEW6432"));
            hardwareInfo.add(System.getenv("NUMBER_OF_PROCESSORS"));

            // Motherboard Serial Number
            String motherboardSN = executeCommand("wmic baseboard get serialnumber");
            hardwareInfo.add(motherboardSN);

            // BIOS Serial Number
            String biosSN = executeCommand("wmic bios get serialnumber");
            hardwareInfo.add(biosSN);

            // Disk Drive Serial Number
            String diskSN = executeCommand("wmic diskdrive get serialnumber");
            hardwareInfo.add(diskSN);

            // Create a unique hash from the hardware info
            return createHash(String.join("", hardwareInfo));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate hardware ID", e);
        }
    }

    private String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            java.util.Scanner s = new java.util.Scanner(process.getInputStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next().trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private String createHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

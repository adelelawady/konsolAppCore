package com.konsol.core.service.fingerprint;

import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HikVisionFingerprintServiceImpl implements FingerprintService {
    
    private final Logger log = LoggerFactory.getLogger(HikVisionFingerprintServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private boolean isConnected = false;
    private final FingerprintConfig config;
    private static final String ARTEMIS_PATH = "/artemis";
    
    @Autowired
    public HikVisionFingerprintServiceImpl(FingerprintConfig config) {
        this.config = config;
    }
    
    @Override
    public boolean initializeDevice(String ipAddress, int port, String username, String password) {
        try {
            // Initialize HikVision SDK configuration
            ArtemisConfig.host = ipAddress + ":" + port;
            ArtemisConfig.appKey = username;
            ArtemisConfig.appSecret = password;
            
            // Test connection by getting device info
            String getDeviceInfoApi = ARTEMIS_PATH + "/api/resource/v1/fingerprint/deviceInfo";
            Map<String, String> path = new HashMap<>();
            path.put("https://", getDeviceInfoApi);
            
            String result = ArtemisHttpUtil.doGetArtemis(path, null, "application/json", null, null);
            
            if (result != null && !result.isEmpty()) {
                isConnected = true;
                log.info("Successfully connected to fingerprint device at {}", ipAddress);
                return true;
            } else {
                log.error("Failed to connect to device - no response");
                return false;
            }
            
        } catch (Exception e) {
            log.error("Failed to initialize fingerprint device: {}", e.getMessage());
            isConnected = false;
            return false;
        }
    }

    @Override
    public byte[] captureFingerprintTemplate(String userId) {
        if (!isConnected) {
            log.error("Device not connected");
            return null;
        }
        
        try {
            String captureApi = ARTEMIS_PATH + "/api/acsFinger/v1/fingerprint/capture";
            Map<String, String> path = new HashMap<>();
            path.put("https://", captureApi);
            
            String jsonBody = objectMapper.writeValueAsString(Map.of("userId", userId));
            
            String result = ArtemisHttpUtil.doPostStringArtemis(path, jsonBody, null, "application/json", null);
            
            if (result != null && !result.isEmpty()) {
                Map<String, Object> response = objectMapper.readValue(result, Map.class);
                if (response.containsKey("data") && response.get("data") != null) {
                    String template = response.get("data").toString();
                    return template.getBytes();
                }
            }
            
            return null;
            
        } catch (Exception e) {
            log.error("Failed to capture fingerprint: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean enrollFingerprint(String userId, byte[] fingerprintTemplate) {
        if (!isConnected) {
            log.error("Device not connected");
            return false;
        }
        
        try {
            String enrollApi = ARTEMIS_PATH + "/api/acsFinger/v1/fingerprint/enroll";
            Map<String, String> path = new HashMap<>();
            path.put("https://", enrollApi);
            
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("fingerprintTemplate", new String(fingerprintTemplate));
            
            String jsonBody = objectMapper.writeValueAsString(params);
            String result = ArtemisHttpUtil.doPostStringArtemis(path, jsonBody, null, "application/json", null);
            
            if (result != null && !result.isEmpty()) {
                Map<String, Object> response = objectMapper.readValue(result, Map.class);
                return response.containsKey("code") && response.get("code").equals("0");
            }
            return false;
            
        } catch (Exception e) {
            log.error("Failed to enroll fingerprint: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyFingerprint(byte[] fingerprintTemplate, String userId) {
        if (!isConnected) {
            log.error("Device not connected");
            return false;
        }
        
        try {
            String verifyApi = ARTEMIS_PATH + "/api/acsFinger/v1/fingerprint/verify";
            Map<String, String> path = new HashMap<>();
            path.put("https://", verifyApi);
            
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("fingerprintTemplate", new String(fingerprintTemplate));
            
            String jsonBody = objectMapper.writeValueAsString(params);
            String result = ArtemisHttpUtil.doPostStringArtemis(path, jsonBody, null, "application/json", null);
            
            if (result != null && !result.isEmpty()) {
                Map<String, Object> response = objectMapper.readValue(result, Map.class);
                return response.containsKey("data") && Boolean.TRUE.equals(response.get("data"));
            }
            return false;
            
        } catch (Exception e) {
            log.error("Failed to verify fingerprint: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteFingerprint(String userId) {
        if (!isConnected) {
            log.error("Device not connected");
            return false;
        }
        
        try {
            String deleteApi = ARTEMIS_PATH + "/api/acsFinger/v1/fingerprint/delete";
            Map<String, String> path = new HashMap<>();
            path.put("https://", deleteApi);
            
            String jsonBody = objectMapper.writeValueAsString(Map.of("userId", userId));
            String result = ArtemisHttpUtil.doPostStringArtemis(path, jsonBody, null, "application/json", null);
            
            if (result != null && !result.isEmpty()) {
                Map<String, Object> response = objectMapper.readValue(result, Map.class);
                return response.containsKey("code") && response.get("code").equals("0");
            }
            return false;
            
        } catch (Exception e) {
            log.error("Failed to delete fingerprint: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> getEnrolledFingerprints() {
        if (!isConnected) {
            log.error("Device not connected");
            return new ArrayList<>();
        }
        
        try {
            String listApi = ARTEMIS_PATH + "/api/acsFinger/v1/fingerprint/list";
            Map<String, String> path = new HashMap<>();
            path.put("https://", listApi);
            
            String result = ArtemisHttpUtil.doGetArtemis(path, null, "application/json", null, null);
            
            List<String> enrolledUsers = new ArrayList<>();
            if (result != null && !result.isEmpty()) {
                Map<String, Object> response = objectMapper.readValue(result, Map.class);
                if (response.containsKey("data") && response.get("data") instanceof List) {
                    List<Map<String, Object>> users = (List<Map<String, Object>>) response.get("data");
                    for (Map<String, Object> user : users) {
                        if (user.containsKey("userId")) {
                            enrolledUsers.add(user.get("userId").toString());
                        }
                    }
                }
            }
            
            return enrolledUsers;
            
        } catch (Exception e) {
            log.error("Failed to get enrolled fingerprints: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isDeviceConnected() {
        return isConnected;
    }

    @Override
    public void disconnect() {
        if (isConnected) {
            try {
                // Clean up resources
                ArtemisConfig.host = null;
                ArtemisConfig.appKey = null;
                ArtemisConfig.appSecret = null;
                
                isConnected = false;
                log.info("Successfully disconnected from fingerprint device");
            } catch (Exception e) {
                log.error("Failed to disconnect from device: {}", e.getMessage());
            }
        }
    }
} 
package com.mms.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Notification entity representing a message sent to a patient.
 */
public class Notification {
    private String notificationId;
    private String patientId;
    private String message;
    private LocalDateTime timestamp;
    private boolean isPromotional;

    public Notification(String notificationId, String patientId, String message, boolean isPromotional) {
        this.notificationId = notificationId;
        this.patientId = patientId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isPromotional = isPromotional;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isPromotional() {
        return isPromotional;
    }

    public void setPromotional(boolean promotional) {
        isPromotional = promotional;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Notification{" +
                "id='" + notificationId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp.format(formatter) +
                ", isPromotional=" + isPromotional +
                '}';
    }
}

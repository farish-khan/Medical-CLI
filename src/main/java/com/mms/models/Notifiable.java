package com.mms.models;

/**
 * Interface for objects that can receive notifications.
 */
public interface Notifiable {
    void receiveNotification(Notification notification);
}

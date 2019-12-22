package com.example.bloodaid.models;

public class NotificationParentModelClass {
    int notificationId;
    String message;
    String need_date;
    String created_at;

    public NotificationParentModelClass(){}

    public NotificationParentModelClass(int notificationId, String message, String need_date, String created_at) {
        this.notificationId = notificationId;
        this.message = message;
        this.need_date = need_date;
        this.created_at = created_at;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNeed_date() {
        return need_date;
    }

    public void setNeed_date(String need_date) {
        this.need_date = need_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

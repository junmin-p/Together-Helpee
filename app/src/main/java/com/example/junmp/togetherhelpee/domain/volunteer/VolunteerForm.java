package com.example.junmp.togetherhelpee.domain.volunteer;

import java.util.Date;

public class VolunteerForm {
    private String userId;

    private Date startAt;
    private Date endAt;
    private double latitude;
    private double longitude;
    private String message;

    public Date getStartAt() {
        return startAt;
    }

    public String getUserId() {
        return userId;
    }

    public Date getEndAt() {

        return endAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }

    public VolunteerForm(String userId , double latitude, double longitude) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStartAt(Date startAt) {

        this.startAt = startAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public boolean validate() {
        return true;
    }

    public String getErrorMessage() {
        return "";
    }
}

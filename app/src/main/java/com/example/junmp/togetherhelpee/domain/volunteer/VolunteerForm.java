package com.example.junmp.togetherhelpee.domain.volunteer;

import com.example.junmp.togetherhelpee.domain.user.User;

import java.util.Date;

public class VolunteerForm {
    private User user;
    private Date startAt;
    private Date endAt;
    private float latitude;
    private float longitude;
    private String helpContents;

    public VolunteerForm(User user, Date startAt, Date endAt, float latitude, float longitude, String helpContents) {
        this.user = user;
        this.startAt = startAt;
        this.endAt = endAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.helpContents = helpContents;
    }
}

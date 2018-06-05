package com.example.junmp.togetherhelpee.domain.volunteer;

import java.util.Date;

public class Volunteer {
    private int volunteerId;
    private Date createdAt;
    private String content;
    private int matchingStatus;

    public int getVolunteerId() {
        return volunteerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public int getMatchingStatus() {
        return matchingStatus;
    }

    public boolean isStandBy() {
        return false;
    }
}

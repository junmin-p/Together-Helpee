package com.example.junmp.togetherhelpee.domain.volunteer.feedback;

public class FeedbackForm {
    private String message;
    private int starCount;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public String getMessage() {

        return message;
    }

    public int getStarCount() {
        return starCount;
    }
}

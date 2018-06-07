package com.example.junmp.togetherhelpee.activity.volunteer.request.form;

import com.example.junmp.togetherhelpee.domain.volunteer.Volunteer;
import com.example.junmp.togetherhelpee.domain.volunteer.helper.Helper;

public class DisplayVolunteer {
    private Volunteer volunteer;
    private Helper helper;

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Helper getHelper() {
        return helper;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
    }
}

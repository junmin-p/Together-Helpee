package com.example.junmp.togetherhelpee.domain.volunteer;

import com.example.junmp.togetherhelpee.common.util.network.RetrofitBuilder;
import com.example.junmp.togetherhelpee.domain.volunteer.helper.Helper;

import java.io.IOException;

public class VolunteerService {

    private VolunteerRepository volunteerRepository = RetrofitBuilder.builder().create(VolunteerRepository.class);


    public Volunteer getActiveOne(String userId) {
        try {
            return volunteerRepository.getActiveOne(userId).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Helper findRecommendHelper(int id) {
        try {
            return volunteerRepository.findRecommendHelper(id).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Volunteer save(VolunteerForm form) {
        try {
            return volunteerRepository.save(form).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

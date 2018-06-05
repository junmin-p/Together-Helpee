package com.example.junmp.togetherhelpee.domain.volunteer;

import com.example.junmp.togetherhelpee.common.util.network.RetrofitBuilder;

import java.io.IOException;

public class VolunteerService {

    private VolunteerRepository volunteerRepository = RetrofitBuilder.builder().create(VolunteerRepository.class);

    /**
     * 서버에 접속해서 가져올것
     * @param userId
     * @return
     */
    public Volunteer getActiveOne(String userId) {
        try {
            return volunteerRepository.getActiveOne(userId).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Volunteer getRecommendHelper(VolunteerForm form) {
        return new Volunteer();
    }
}

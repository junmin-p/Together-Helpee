package com.example.junmp.togetherhelpee.domain.volunteer.feedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedbackRepository {

    @POST("volunteer/:volunteerId/feedback")
    Call<Void> save(@Body FeedbackForm form);
}

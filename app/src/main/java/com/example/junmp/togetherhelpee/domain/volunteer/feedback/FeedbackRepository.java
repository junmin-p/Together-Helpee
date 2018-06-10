package com.example.junmp.togetherhelpee.domain.volunteer.feedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedbackRepository {

    @POST("volunteer/{volunteerId}/feedback")
    Call<Void> save(@Path("volunteerId") String volunteerId, @Body FeedbackForm form);
}

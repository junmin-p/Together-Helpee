package com.example.junmp.togetherhelpee.domain.volunteer;

import com.example.junmp.togetherhelpee.domain.volunteer.helper.Helper;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;

public interface VolunteerRepository {

    @GET("volunteer/{userId}/active-one")
    Call<Volunteer> getActiveOne(@Path("userId") String userId);

    @GET("volunteer/{volunteerId}/recommend/helper")
    Call<Helper> findRecommendHelper(@Path("volunteerId") int volunteerId);

    @POST("volunteer")
    Call<Volunteer> save(@Body VolunteerForm form);

    @PUT("volunteer/{volunteerId}/accept")
    Call<Void> accept(@Path("volunteerId") int volunteerId);
}

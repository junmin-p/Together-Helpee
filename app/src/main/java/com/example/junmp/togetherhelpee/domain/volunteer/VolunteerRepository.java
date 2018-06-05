package com.example.junmp.togetherhelpee.domain.volunteer;

import com.example.junmp.togetherhelpee.domain.user.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VolunteerRepository {

    @GET("volunteer/{userId}/active-one")
    Call<Volunteer> getActiveOne(@Path("userId") String userId);
}

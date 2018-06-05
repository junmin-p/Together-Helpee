package com.example.junmp.togetherhelpee.domain.device;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DeviceRepository {
    @FormUrlEncoded
    @POST("device")
    Call<Void> save(@Field("deviceId") String deviceId , @Field("token") String pushToken);

}

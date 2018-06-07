package com.example.junmp.togetherhelpee.domain.user;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public interface UserRepository {

    @GET("user/helpee/{phoneNumber}")
    Call<User> getOne(@Path("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("user/helpee/{deviceId}")
    Call<Void> register(@Path("deviceId") String deviceId, @Field("age") int age,  @Field("name") String name, @Field("imageName") String imageName , @Field("gender") String gender ,  @Field("phoneNumber") String phoneNumber);
}

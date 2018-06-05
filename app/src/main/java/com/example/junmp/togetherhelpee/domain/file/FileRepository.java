package com.example.junmp.togetherhelpee.domain.file;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface FileRepository {
    @Multipart
    @POST("file/image")
    Call<String> uploadImage(@Part MultipartBody.Part image);


}

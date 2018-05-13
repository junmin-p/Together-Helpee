package com.example.junmp.togetherhelpee;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("/helpee/addUser")
    Call<ResponseBody> upload(
            @Part("id") RequestBody id,
            @Part("user_phone") RequestBody user_phone,
            @Part MultipartBody.Part userfile
    );
}

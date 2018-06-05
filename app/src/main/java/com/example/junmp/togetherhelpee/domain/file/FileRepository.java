package com.example.junmp.togetherhelpee.domain.file;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileRepository {
    @Multipart
    @POST("file/image")
    Call<UploadFile> uploadImage(@Part MultipartBody.Part image);


}

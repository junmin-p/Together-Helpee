package com.example.junmp.togetherhelpee;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface RecordPutService {
    @Multipart
    @PUT("/helpee/record")
    Call<ResponseBody> put(
            @Part("helpeeFeedbackContent") RequestBody helpee_Feedback,
            @Part("volunteerId") RequestBody volunteer_Id,
            @Part("helpeeScore") RequestBody helpee_Score,
            @Part MultipartBody.Part recordfile
    );
}

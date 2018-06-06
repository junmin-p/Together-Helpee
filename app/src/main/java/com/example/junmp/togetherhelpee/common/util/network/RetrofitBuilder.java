package com.example.junmp.togetherhelpee.common.util.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {


    public static Retrofit builder() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.11:9001/api/")

                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}

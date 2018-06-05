package com.example.junmp.togetherhelpee.common.util.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {


    public static Retrofit builder() {
        return new Retrofit.Builder()
                .baseUrl("http://lim-bo.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}

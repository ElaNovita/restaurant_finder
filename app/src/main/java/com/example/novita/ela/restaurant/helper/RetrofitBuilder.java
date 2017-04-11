package com.example.novita.ela.restaurant.helper;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by elaa on 02/04/17.
 */

public class RetrofitBuilder {
    public static final String BaseUrl = "http://192.168.12.150/cafe_finder/";
//    192.168.43.8
//    192.168.12.150

    private Context _context;

    public RetrofitBuilder(Context context) {
        this._context = context;
    }

    private OkHttpClient getClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        return builder.build();
    }


    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
    }
}

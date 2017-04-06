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
    public static final String BaseUrl = "http://192.168.43.8/cafe_finder/";


    private Context _context;

    public RetrofitBuilder(Context context) {
        this._context = context;
    }

    private OkHttpClient getClient() {
//        final String token = "afac45d407f40d4cac21348a0218a040";

//        Interceptor interceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder().addHeader("user_key", token).build();
//                return chain.proceed(request);
//            }
//        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.interceptors().add(interceptor);

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

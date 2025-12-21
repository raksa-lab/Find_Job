package com.example.find_job.data.service;

import android.content.Context;

import com.example.find_job.utils.SessionManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://backend-mobile-mad.vercel.app/api/";

    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {

        // ===============================
        // LOGGING
        // ===============================
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // ===============================
        // AUTH INTERCEPTOR
        // ===============================
        Interceptor authInterceptor = chain -> {
            Request original = chain.request();

            SessionManager sessionManager = new SessionManager(context);
            String token = sessionManager.getToken();

            Request.Builder builder = original.newBuilder();

            if (token != null && !token.isEmpty()) {
                builder.addHeader(
                        "Authorization",
                        "Bearer " + token
                );
            }

            Request request = builder.build();
            return chain.proceed(request);
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logging)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }
}

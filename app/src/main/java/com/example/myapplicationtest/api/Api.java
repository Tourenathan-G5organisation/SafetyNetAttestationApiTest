package com.example.myapplicationtest.api;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static final String TAG = Api.class.getSimpleName();

    private SafetyNetService mSafetyNetService;
    private static Api mInstance;

    public Api(){
        Gson gson = new Gson();
        mSafetyNetService = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                        .build().create(SafetyNetService.class);
    }

    public static synchronized Api getInstance(){
        if (mInstance == null) mInstance = new Api();
                return mInstance;
    }

    public SafetyNetService getSafetyNetServiceApi(){
        return mSafetyNetService;
    }
}

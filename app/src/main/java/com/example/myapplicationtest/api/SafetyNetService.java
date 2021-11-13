package com.example.myapplicationtest.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SafetyNetService {

    @POST("/androidcheck/v1/attestations/verify")
    Call<VerificationResponse> verifyReQuest(@Query("key") String key, @Body VerificationRequest request);
}

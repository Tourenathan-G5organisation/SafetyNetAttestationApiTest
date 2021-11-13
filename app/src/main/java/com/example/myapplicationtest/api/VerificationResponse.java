package com.example.myapplicationtest.api;

import com.google.gson.annotations.SerializedName;

public class VerificationResponse {

    @SerializedName("isValidSignature")
    public boolean isValidSignature;

    /**
     * Optional field that is only set when the server encountered an error processing the
     * request.
     */
    @SerializedName("error")
    public String error;
}

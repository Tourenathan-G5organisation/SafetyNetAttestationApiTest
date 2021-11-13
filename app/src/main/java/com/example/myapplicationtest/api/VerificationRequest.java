package com.example.myapplicationtest.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class VerificationRequest {

    public VerificationRequest(String signedAttestation) {
        this.signedAttestation = signedAttestation;
    }

    @SerializedName("signedAttestation")
    public String signedAttestation;

}

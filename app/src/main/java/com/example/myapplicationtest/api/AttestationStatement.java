package com.example.myapplicationtest.api;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class AttestationStatement {

    /**
     * Embedded nonce sent as part of the request.
     */
    @SerializedName("nonce")
    private String nonce;

    /**
     * Timestamp of the request.
     */
    private long timestampMs;

    /**
     * Package name of the APK that submitted this request.
     */
    private String apkPackageName;

    /**
     * Digest of certificate of the APK that submitted this request.
     */
    private String[] apkCertificateDigestSha256;

    /**
     * Digest of the APK that submitted this request.
     */
    private String apkDigestSha256;

    /**
     * The device passed CTS and matches a known profile.
     */
    private boolean ctsProfileMatch;

    /**
     * The device has passed a basic integrity test, but the CTS profile could not be verified.
     */
    private boolean basicIntegrity;

    /**
     * Types of measurements that contributed to this response.
     */
    private String evaluationType;

    public byte[] getNonce() {
        return Base64.decode(nonce, Base64.DEFAULT);
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public byte[] getApkDigestSha256() {
        return Base64.decode(apkDigestSha256, Base64.DEFAULT);
    }

    public byte[][] getApkCertificateDigestSha256() {
        byte[][] certs = new byte[apkCertificateDigestSha256.length][];
        for (int i = 0; i < apkCertificateDigestSha256.length; i++) {
            certs[i] = Base64.decode(apkCertificateDigestSha256[i], Base64.DEFAULT);
        }
        return certs;
    }

    public boolean isCtsProfileMatch() {
        return ctsProfileMatch;
    }

    public boolean hasBasicIntegrity() {
        return basicIntegrity;
    }

    public boolean hasBasicEvaluationType() {
        return evaluationType.contains("BASIC");
    }

    public boolean hasHardwareBackedEvaluationType() {
        return evaluationType.contains("HARDWARE_BACKED");
    }
}

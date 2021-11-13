package com.example.myapplicationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.myapplicationtest.api.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.gson.Gson
import com.nimbusds.jose.JWSObject
import okhttp3.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.SecureRandom
import java.util.*

class MainActivity : AppCompatActivity() {

    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playServicesResult = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(this, 13000000)
        if (playServicesResult ==
            ConnectionResult.SUCCESS
        ) {
            // The SafetyNet Attestation API is available.
            sendSendSafetyNetRequest()
        } else {
            // Prompt user to update Google Play Services.
            if (GoogleApiAvailability.getInstance().isUserResolvableError(playServicesResult)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, playServicesResult, 1002)
                    .show();
            }
        }
    }

    fun sendSendSafetyNetRequest() {
        SafetyNet.getClient(this).attest(getRequestNonce(), AppConfig.KEY)
            .addOnSuccessListener(this) {
                // Indicates communication with the service was successful.
                // Use response.getJwsResult() to get the result data.
                val result: String = it.jwsResult
                verifySafetyNetAttestationRequest(result)
            }
            .addOnFailureListener(this) { e ->
                // An error occurred while communicating with the service.
                if (e is ApiException) {
                    // An error with the Google Play services API contains some
                    // additional details.
                    val apiException = e as ApiException

                    // You can retrieve the status code using the
                    // apiException.statusCode property.
                } else {
                    // A different, unknown type of error occurred.
                    Log.d(TAG, "Error: " + e.message)
                }
            }
    }

    fun getRequestNonce(): ByteArray? {
        val nonceData = "SafeNetAttestation" + System.currentTimeMillis()
        val byteStream: ByteArrayOutputStream = ByteArrayOutputStream()
        val bytes: ByteArray = ByteArray(24)
        val random: Random = SecureRandom()
        random.nextBytes(bytes)
        try {
            byteStream.write(bytes)
            byteStream.write(nonceData.toByteArray())
        } catch (e: IOException) {
            return null;
        }
        return byteStream.toByteArray()
    }

    fun verifySafetyNetAttestationRequest(signedAttestationStatement: String) {
        Api.getInstance().safetyNetServiceApi.verifyReQuest(
            AppConfig.KEY,
            VerificationRequest(signedAttestationStatement)
        ).enqueue(object : Callback<VerificationResponse?> {
            override fun onResponse(
                call: retrofit2.Call<VerificationResponse?>,
                response: Response<VerificationResponse?>
            ) {
                var data = response.body()
                if (data?.isValidSignature!!) {
                    val jwsObject: JWSObject = JWSObject.parse(signedAttestationStatement)
                    val gson: Gson = Gson()
                    val result: AttestationStatement = gson.fromJson<AttestationStatement>(
                        jwsObject.payload.toJSONObject().toJSONString(),
                        AttestationStatement::class.java
                    )
                    sendAttestationStatementFeedback(result)
                }
            }

            override fun onFailure(call: retrofit2.Call<VerificationResponse?>, t: Throwable) {}
        })
    }

    fun sendAttestationStatementFeedback(statement: AttestationStatement){
        findViewById<TextView>(R.id.text).text = if(statement.isCtsProfileMatch) "This device matches CTS properties" else "This device may have a security issue"
    }
}
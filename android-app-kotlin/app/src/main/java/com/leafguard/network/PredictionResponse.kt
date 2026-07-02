package com.leafguard.network

import com.google.gson.annotations.SerializedName

/**
 * Kotlin twin of PredictionResponse.java.
 *
 * A Java POJO with getters/setters becomes a Kotlin data class with
 * mutable properties so the TFLiteClassifier can populate it the same way.
 * The @SerializedName values keep the JSON contract identical.
 */
data class PredictionResponse(
    @SerializedName("disease")
    var disease: String? = null,

    @SerializedName("confidence")
    var confidence: Float = 0f,

    @SerializedName("symptoms")
    var symptoms: String? = null,

    @SerializedName("treatment")
    var treatment: String? = null,

    @SerializedName("prevention")
    var prevention: String? = null
)

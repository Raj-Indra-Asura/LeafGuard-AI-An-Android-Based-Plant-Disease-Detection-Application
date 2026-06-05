package com.leafguard.network;

import com.google.gson.annotations.SerializedName;

public class PredictionResponse {

    @SerializedName("disease")
    private String disease;

    @SerializedName("confidence")
    private float confidence;

    @SerializedName("symptoms")
    private String symptoms;

    @SerializedName("treatment")
    private String treatment;

    @SerializedName("prevention")
    private String prevention;

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }
}

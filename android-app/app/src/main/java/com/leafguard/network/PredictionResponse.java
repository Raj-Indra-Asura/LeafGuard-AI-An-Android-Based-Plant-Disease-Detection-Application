package com.leafguard.network;

import com.google.gson.annotations.SerializedName;

public class PredictionResponse {
    @SerializedName("model_label")
    private String modelLabel;

    @SerializedName("disease")
    private String disease;

    @SerializedName("confidence")
    private float confidence;

    @SerializedName("uncertain")
    private boolean uncertain;

    @SerializedName("guidance_available")
    private boolean guidanceAvailable;

    @SerializedName("symptoms")
    private String symptoms;

    @SerializedName("treatment")
    private String treatment;

    @SerializedName("prevention")
    private String prevention;

    public String getDisease() {
        return disease;
    }

    public String getModelLabel() {
        return modelLabel;
    }

    public void setModelLabel(String modelLabel) {
        this.modelLabel = modelLabel;
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

    public boolean isUncertain() {
        return uncertain;
    }

    public void setUncertain(boolean uncertain) {
        this.uncertain = uncertain;
    }

    public boolean isGuidanceAvailable() {
        return guidanceAvailable;
    }

    public void setGuidanceAvailable(boolean guidanceAvailable) {
        this.guidanceAvailable = guidanceAvailable;
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

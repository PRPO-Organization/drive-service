package com.skupina1.model;

public class TransactionRequest {
    private double distanceKm;
    private double  durationMin;
    private double surge = 1.0;

    public TransactionRequest(double dist, double dur, double sur){
        this.distanceKm = dist;
        this.durationMin = dur;
        this.surge = sur;
    }

    public double getDistanceKm() {
        return this.distanceKm;
    }

    public double getDurationMin() {
        return this.durationMin;
    }

    public double getSurge() {
        return this.surge;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setDurationMin(double durationMin) {
        this.durationMin = durationMin;
    }

    public void setSurge(double surge) {
        this.surge = surge;
    }
}

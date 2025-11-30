package com.skupina1.model;

public record RideRequest(
        Long passengerId,
        Long driverId,
        double pickupLat,
        double pickupLng,
        double dropoffLat,
        double dropoffLng
) {}

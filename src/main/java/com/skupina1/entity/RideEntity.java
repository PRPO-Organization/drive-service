package com.skupina1.entity;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
public class RideEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long passengerId;
    private long driverId;
    private String status;      // NEW, IN_PROGRESS, COMPLETED
    private LocalDateTime createdAt;

    private double pickupLat;
    private double pickupLng;

    private double dropoffLat;
    private double dropoffLng;

    public RideEntity() {}

    public double getDropoffLat() {return this.dropoffLat;}
    public double getDropoffLng() {return this.dropoffLng;}
    public double getPickupLat() {return this.pickupLat;}
    public double getPickupLng() {return this.pickupLng;}
    public long getId() {return this.id;}
    public long getDriverId() {return this.driverId;}
    public long getPassengerId() {return this.passengerId;}
    public String getStatus() {return this.status;}
    public LocalDateTime getCreatedAt() {return this.createdAt;}

    public void setId(long id) {this.id = id;}
    public void setDriverId(long driverId) {this.driverId = driverId;}
    public void setDropoffLat(double dropoffLat) {this.dropoffLat = dropoffLat;}
    public void setDropoffLng(double dropoffLong) {this.dropoffLng = dropoffLong;}
    public void setPassengerId(long passengerId) {this.passengerId = passengerId;}
    public void setPickupLat(double pickupLat) {this.pickupLat = pickupLat;}
    public void setPickupLng(double pickupLong) {this.pickupLng = pickupLong;}
    public void setStatus(String status) {this.status = status;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}

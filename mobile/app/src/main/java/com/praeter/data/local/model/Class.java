package com.praeter.data.local.model;

import android.location.Location;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Parcel(Parcel.Serialization.BEAN)
public class Class {

    String name;
    String type;
    String duration;

    double latitude;
    double longitude;

    Location location;

    public Class(){}

    public Class(String name) {
        this.name = name;
    }

    public Class(String name, String type) {
        this.name = name;
        this.type = type;
    }


    public Class(String name, String type, String duration) {
        this.name = name;
        this.type = type;
        this.duration = duration;
    }

    public Class(String name, String type, String duration, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Class(String name, String type, String duration, Location location) {
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.location = location;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}

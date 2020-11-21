package com.praeter.data.local.model;

import android.location.Location;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Ancient {

    String name;

    double latitude;
    double longitude;

    Location location;

    public Ancient() {
    }

    public Ancient(String name) {
        this.name = name;
    }

    public Ancient(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Ancient(String name, Location location) {
        this.name = name;
        this.location = location;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}

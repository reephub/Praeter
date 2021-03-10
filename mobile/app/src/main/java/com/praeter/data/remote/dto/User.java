package com.praeter.data.remote.dto;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Parcel
public class User {

    @Expose
    String gender;

    @Expose
    String firstName;

    @Expose
    String lastName;

    @Expose
    String email;

    @Expose
    String password;

    @Expose
    String phoneNumber;

    @Expose
    String dateOfBirth;

    @Expose
    boolean isPremium;

    @Expose
    boolean isCustomer;

    @Expose
    boolean isProvider;

    @ParcelConstructor
    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String gender, String firstName, String lastName, String email, String password, String phoneNumber, String dateOfBirth) {
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
}

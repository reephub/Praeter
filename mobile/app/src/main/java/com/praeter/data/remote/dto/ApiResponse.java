package com.praeter.data.remote.dto;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiResponse {
    @Expose
    int code;

    @Expose
    String message;
}

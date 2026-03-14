package com.eventmgmt.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;

//    public String get(String email) {
//        return email;
//    }
}

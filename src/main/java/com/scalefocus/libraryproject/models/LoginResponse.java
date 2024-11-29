package com.scalefocus.libraryproject.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {

    private String token;

    private long expiresIn;

//    public String getToken() {
//        return token;
//    }
//
//    public LoginResponse setToken(String token) {
//        this.token = token;
//        return this;
//    }
//
//    public long getExpiresIn() {
//        return expiresIn;
//    }
//
//    public LoginResponse setExpiresIn(long expiresIn) {
//        this.expiresIn = expiresIn;
//        return this;
//    }
//
//    @Override
//    public String toString() {
//        return "LoginResponse{" +
//                "token='" + token + '\'' +
//                ", expiresIn=" + expiresIn +
//                '}';
//    }

}

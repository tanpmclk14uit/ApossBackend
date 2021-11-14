package com.example.apossbackend.model.dto;

public class JWTAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String refreshToken= "";

    public JWTAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    public JWTAuthResponse(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

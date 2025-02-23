package com.bgrfacile.bgrsignapi.dto.response;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expires_in;

    public JwtAuthenticationResponse(
            String accessToken,
            Long expires_in
    ) {
        this.accessToken = accessToken;
        this.expires_in = expires_in;
    }
}

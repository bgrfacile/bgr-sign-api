package com.bgrfacile.bgrsignapi.dto.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String statusText;
    private Integer status;
    private String message;
    private Object errors;

    public ErrorResponse(String message, Object errors) {
        this.statusText = "error";
        this.status = 400;
        this.message = message;
        this.errors = errors;
    }
}

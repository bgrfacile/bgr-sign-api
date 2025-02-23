package com.bgrfacile.bgrsignapi.dto.response;

import lombok.Data;

@Data
public class SuccessResponse {
    private String statusText;
    private Integer status;
    private Object data;
    private String message;

    public SuccessResponse(Object data, String message) {
        this.status = 200;
        this.statusText = "success";
        this.data = data;
        this.message = message;
    }
}

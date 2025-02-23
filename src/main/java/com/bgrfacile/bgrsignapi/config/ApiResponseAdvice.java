package com.bgrfacile.bgrsignapi.config;

import com.bgrfacile.bgrsignapi.dto.response.ErrorResponse;
import com.bgrfacile.bgrsignapi.dto.response.SuccessResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // affiner ici si besoin (exclure certains types, etc.)
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // Eviter de re-wrappper si la réponse est déjà au format souhaité
        if (body instanceof SuccessResponse || body instanceof ErrorResponse) {
            return body;
        }
        return new SuccessResponse(body, "Opération réussie");
    }
}

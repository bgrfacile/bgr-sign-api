package com.bgrfacile.bgrsignapi.exception;

public class SessionConflictException  extends RuntimeException {
    public SessionConflictException(String message) {
        super(message);
    }
}
package com.minjae.my_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private final int status;
    private final String error;
    private final String message;

    @Builder
    public ErrorResponseDto(int status, String error, String message){
        this.status = status;
        this.error = error;
        this.message = message;
    }
}

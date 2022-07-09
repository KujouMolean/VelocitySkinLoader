package com.molean.velocityskinloader.model.mineskin.exception;

import lombok.Data;

@Data
public class RequestTooSoonException extends MineSkinAPIException {
    private String error;
    private Long nextRequest;
    private Long delay;
}

package com.molean.velocityskinloader.model.mineskin.exception;

import lombok.Data;

@Data
public class SkinGenerateException extends MineSkinAPIException {
    private String errorCode;
    private String error;
}

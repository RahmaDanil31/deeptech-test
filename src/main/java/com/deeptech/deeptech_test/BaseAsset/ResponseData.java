package com.deeptech.deeptech_test.BaseAsset;

import lombok.Data;

@Data
public class ResponseData<T> {
    private T data;
    private String message;
    private int status;

    public ResponseData() {
    }

    public ResponseData(T data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }
}

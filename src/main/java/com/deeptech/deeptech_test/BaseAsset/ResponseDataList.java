package com.deeptech.deeptech_test.BaseAsset;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDataList<T> {
    private Metadata metadata;
    private List<T> records;

    public ResponseDataList() {
    }

    public ResponseDataList(Metadata metadata, List<T> records) {
        this.metadata = metadata;
        this.records = records;
    }
}

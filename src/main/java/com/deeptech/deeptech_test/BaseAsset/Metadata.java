package com.deeptech.deeptech_test.BaseAsset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
    private int page;
    private int perPage;
    private int pageCount;
    private long totalCount;
}

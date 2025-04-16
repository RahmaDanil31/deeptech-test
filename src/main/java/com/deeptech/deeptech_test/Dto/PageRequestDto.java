package com.deeptech.deeptech_test.Dto;

import lombok.Data;

@Data
public class PageRequestDto {

    private int page = 0;
    private int limit = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";

}

package com.respec.training.DTO;

import java.util.List;

import lombok.Data;

@Data
public class PageableDTO<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
}

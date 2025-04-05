package com.ndinhchien.langPractice.global.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageDto<T> {

    private List<T> content;

    private int pageSize;

    private int pageNumber;

    private int totalPages;

    private int totalElements;

}

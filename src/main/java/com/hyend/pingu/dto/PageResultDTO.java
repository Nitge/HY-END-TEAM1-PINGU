package com.hyend.pingu.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

@Getter
public class PageResultDTO<DTO, EN>{
    private List<DTO> dtoList;

    private int page;

    private int size;

    private int totalPage;

    private int start, end;

    private boolean prev, next;

    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        dtoList = result.stream().map(fn).toList();

        page = result.getNumber() + 1;
        size = result.getSize();
        totalPage = result.getTotalPages();

        int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
        start = tempEnd - 9;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        prev = 1 < start;
        next = tempEnd < totalPage;
        pageList = IntStream.rangeClosed(start, end).boxed().toList();

    }
}

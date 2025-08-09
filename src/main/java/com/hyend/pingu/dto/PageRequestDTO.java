package com.hyend.pingu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 쿼리스트링(page, size, sortBy, direction)을 바인딩해서
 * Spring Data Pageable 로 바꿔주는 DTO
 */
@Data
@NoArgsConstructor
public class PageRequestDTO {

    // 1부터 시작하도록(스프링 Page는 0-base라 변환 시 -1)
    private Integer page = 1;
    private Integer size = 10;

    private String sortBy; // 예: "userId"
    private Sort.Direction direction = Sort.Direction.DESC; // ASC/DESC

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

    public Pageable getPageableWithSort() {
        if (sortBy != null && !sortBy.isBlank()) {
            return PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
        }
        return getPageable();
    }
}

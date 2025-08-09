package com.hyend.pingu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 공용 페이징 응답 DTO
 * - EN(엔티티) -> DTO 로 변환함수(Function)를 받아서 dtoList 를 채움
 * - 페이지 버튼 계산(시작, 끝, 이전/다음) 포함
 */
@Getter
@ToString
@NoArgsConstructor
public class PageResultDTO<DTO, EN> {

    /** 실제 응답 데이터 목록 */
    private List<DTO> dtoList;

    /** 전체 페이지 수 */
    private int totalPage;

    /** 현재 페이지(1-base) */
    private int page;

    /** 페이지 당 사이즈 */
    private int size;

    /** 페이지 네비게이션: 시작/끝 페이지, 이전/다음 존재 여부, 페이지 번호 목록 */
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    private List<Integer> pageList;

    /**
     * Spring Data Page 결과를 받아 EN -> DTO 매핑까지 수행
     */
    public PageResultDTO(Page<EN> result, Function<EN, DTO> convert) {
        this.dtoList = result.stream()
                .map(convert)
                .collect(Collectors.toList());

        this.totalPage = result.getTotalPages();
        this.page = result.getNumber() + 1;  // Page 는 0-base, 응답은 1-base
        this.size = result.getSize();

        // 페이지 네비게이션 계산 (10개 단위)
        int tempEnd = (int) (Math.ceil(this.page / 10.0)) * 10;
        this.start = tempEnd - 9;
        if (start < 1) start = 1;

        this.end = Math.min(tempEnd, totalPage);

        this.prev = start > 1;
        this.next = totalPage > tempEnd;

        this.pageList = IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }
}

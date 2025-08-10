package com.hyend.pingu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PinUpdateRequestDTO {

    private String title;
    private String content;
    private char scope;

}

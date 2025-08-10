package com.hyend.pingu.dto;

import com.hyend.pingu.entity.PostEntity;
import com.hyend.pingu.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PinSaveRequestDTO {

    private String title;
    private String content;
    private float latitude;
    private float longitude;
    private char scope;

    public PostEntity toPostEntity(UserEntity user){
        return PostEntity.builder()
                .user(user)
                .title(this.title)
                .content(this.content)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .scope(this.scope)
                .build();
    }
}

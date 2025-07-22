package com.hyend.pingu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FileEntity extends BaseEntity {

    /*
    파일 어떻게 다룰지 확인 필요
     */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private String uploadPath;

    private String fileName;

    private String fileType;

}

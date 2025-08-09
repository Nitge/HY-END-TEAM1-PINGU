package com.hyend.pingu.entity;

import com.hyend.pingu.enumeration.Scope;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "post")
public class PostEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;

    private String content;

    private long likeCount;

    private long viewCount;

    private float latitude;

    private float longitude;

    @Enumerated(EnumType.STRING)
    private Scope scope;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files = new ArrayList<>();

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeScope(Scope scope) {
        this.scope = scope;
    }

    public void changeFiles(List<FileEntity> files) {
        this.files.clear();
        files.forEach(fileEntity -> {
            fileEntity.setPost(this);
            this.files.add(fileEntity);
        });
    }

}

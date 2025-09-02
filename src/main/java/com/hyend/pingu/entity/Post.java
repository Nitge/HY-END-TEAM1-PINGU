package com.hyend.pingu.entity;

import com.hyend.pingu.enumeration.Scope;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String content;

    private Long likeCount;

    private Long viewCount;

    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    @Enumerated(EnumType.STRING)
    private Scope scope;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<File> files = new ArrayList<>();

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeScope(Scope scope) {
        this.scope = scope;
    }

    public void changeFiles(List<File> files) {
        this.files.clear();
        files.forEach(file -> {
            file.setPost(this);
            this.files.add(file);
        });
    }

    public void changeLocation(Point Location) {
        this.location = Location;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}

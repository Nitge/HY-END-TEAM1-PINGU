package com.hyend.pingu.entity;

import com.hyend.pingu.dto.PostFolderDTO;
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
public class PostFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_folder_mapping",
            joinColumns = @JoinColumn(name = "post_folder_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private final List<Post> posts = new ArrayList<>();

    public void changeName(String newName) {
        this.name = newName;
    }

    public void changePosts(PostFolderDTO postFolderDTO) {
        List<Post> oldPosts = this.getPosts();
        List<Post> newPosts = postFolderDTO.getPostIds().stream()
                .map(postId -> Post.builder().id(postId).build())
                .toList();

        // toAdd = newPosts - oldPosts (차집합)
        List<Post> toAdd = new ArrayList<>(newPosts);
        toAdd.removeAll(oldPosts);

        // toRemove = oldPosts - newPosts (차집합)
        List<Post> toRemove = new ArrayList<>(oldPosts);
        toRemove.removeAll(newPosts);

        // newPosts에 존재하는 객체만 남김
        oldPosts.removeAll(toRemove);
        oldPosts.addAll(toAdd);
    }
}

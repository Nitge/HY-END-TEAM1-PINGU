package com.hyend.pingu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FriendshipEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long friendshipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id1")
    private UserEntity friend1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id2")
    private UserEntity friend2;
}

package com.hyend.pingu.entity;

import com.hyend.pingu.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "friendship")
public class FriendshipEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendshipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id1")
    private UserEntity friend1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id2")
    private UserEntity friend2;

    @Enumerated(EnumType.STRING)
    private Status status;
}

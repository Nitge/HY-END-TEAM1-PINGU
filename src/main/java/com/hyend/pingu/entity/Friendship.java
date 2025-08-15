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
public class Friendship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id1")
    private User friend1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id2")
    private User friend2;

    @Enumerated(EnumType.STRING)
    private Status status;
}

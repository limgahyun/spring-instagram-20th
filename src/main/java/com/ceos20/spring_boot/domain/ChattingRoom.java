package com.ceos20.spring_boot.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rood_id")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "other_user_id")
    private User otherUser;
}

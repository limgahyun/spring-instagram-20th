package com.ceos20.spring_boot.domain;

import jakarta.persistence.*;

@Entity
public class Follow {
    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private User followingUser;

}

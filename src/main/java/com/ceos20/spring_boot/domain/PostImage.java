package com.ceos20.spring_boot.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String alt;

    @Column(nullable = false)
    private String type;
}

package com.ceos20.spring_boot.domain;

import jakarta.persistence.*;

@Entity
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String url;
    private String name;
    private String alt;
    private String type;
}

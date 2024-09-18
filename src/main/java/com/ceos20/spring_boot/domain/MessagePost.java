package com.ceos20.spring_boot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MessagePost extends Message {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}

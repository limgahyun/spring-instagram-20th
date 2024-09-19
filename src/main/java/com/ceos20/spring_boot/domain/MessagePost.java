package com.ceos20.spring_boot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class MessagePost extends Message {
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}

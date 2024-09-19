package com.ceos20.spring_boot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class MessageImage extends Message {
    @Column (nullable = false)
    private String url;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private String alt;

    @Column (nullable = false)
    private String type;
}

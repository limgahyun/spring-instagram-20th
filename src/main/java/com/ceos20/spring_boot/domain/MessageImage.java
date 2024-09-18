package com.ceos20.spring_boot.domain;

import jakarta.persistence.Entity;

@Entity
public class MessageImage extends Message {
    private String url;
    private String name;
    private String alt;
    private String type;
}

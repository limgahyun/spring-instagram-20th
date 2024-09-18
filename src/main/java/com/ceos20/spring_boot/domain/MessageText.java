package com.ceos20.spring_boot.domain;

import jakarta.persistence.Entity;

@Entity
public class MessageText extends Message {
    private String textContent;
}

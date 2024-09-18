package com.ceos20.spring_boot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class MessageText extends Message {
    @Column(nullable = false)
    private String textContent;
}

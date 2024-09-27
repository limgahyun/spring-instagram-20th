package com.ceos20.spring_boot.chatting.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class MessageText extends Message {
    @Column(nullable = false)
    private String textContent;
}

package com.ceos20.spring_boot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = true, length = 11)
    private String phone;

    @Builder
    public User(String nickname, String name, String email, String password, String phone) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}

package com.ceos20.spring_boot.user.domain;

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
    private Long id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true, length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(Long id, String nickname, String name, String email, String password, String phone) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = UserRole.USER;
    }
}

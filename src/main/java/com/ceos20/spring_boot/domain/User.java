package com.ceos20.spring_boot.domain;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    private String nickname;
    private String name;
    private String email;
    private String password;
    private String phone;
}

package com.ceos20.spring_boot.user.repository;

import com.ceos20.spring_boot.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // nickname, email 값에 대한 중복체크를 위한 boolean값
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}

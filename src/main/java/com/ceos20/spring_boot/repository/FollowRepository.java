package com.ceos20.spring_boot.repository;

import com.ceos20.spring_boot.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}

package com.ceos20.spring_boot.repository;

import com.ceos20.spring_boot.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<ChattingRoom, Long> {
}

package com.ceos20.spring_boot.chatting.repository;

import com.ceos20.spring_boot.chatting.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<ChattingRoom, Long> {
}

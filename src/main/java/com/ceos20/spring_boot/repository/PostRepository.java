package com.ceos20.spring_boot.repository;

import com.ceos20.spring_boot.domain.Follow;
import com.ceos20.spring_boot.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}

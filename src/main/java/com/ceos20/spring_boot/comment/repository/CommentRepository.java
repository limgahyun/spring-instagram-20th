package com.ceos20.spring_boot.comment.repository;

import com.ceos20.spring_boot.comment.domain.PostComment;
import com.ceos20.spring_boot.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPost(Post post);
};

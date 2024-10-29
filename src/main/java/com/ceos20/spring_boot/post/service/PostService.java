package com.ceos20.spring_boot.post.service;

import com.ceos20.spring_boot.post.domain.Post;
import com.ceos20.spring_boot.post.dto.request.PostCreatRequestDto;
import com.ceos20.spring_boot.post.repository.PostRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor
@Transactional (readOnly = true)
public class PostService {
    private PostRepository postRepository;

    @Transactional
    public void createPost (PostCreatRequestDto postCreatRequestDto) {
        Post post = postCreatRequestDto.toEntity();
        postRepository.save(post);
    }

}

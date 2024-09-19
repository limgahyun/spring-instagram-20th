package com.ceos20.spring_boot.repository;

import com.ceos20.spring_boot.domain.Post;
import com.ceos20.spring_boot.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post1;
    private Post post2;

    @BeforeEach
    public void setUp() {

        //given
        user = User.builder()
                .name("test")
                .email("test@gmail.com")
                .nickname("nickname")
                .password("1234!")
                .phone("12345678")
                .build();

        post1 = Post.builder()
                .user(user)
                .content("posting1")
                .build();

        post2 = Post.builder()
                .user(user)
                .content("posting2")
                .build();

        //when
        userRepository.save(user);
        postRepository.save(post1);
        postRepository.save(post2);
    }

    @Test
    public void findAll() {
        //then
        List<Post> allPosts = postRepository.findAll();
        Assertions.assertEquals(2, allPosts.size());
    }

    @Test
    public void findById() {
        //then
        Optional<Post> post = postRepository.findById(post1.getId());
        Assertions.assertEquals(post1.getUser(), post.get().getUser());
    }
}

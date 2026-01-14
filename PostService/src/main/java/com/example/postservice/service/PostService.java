package com.example.postservice.service;

import com.example.postservice.controller.PostServiceController;
import com.example.postservice.model.Post;
import com.example.postservice.model.User;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final UserRepository userRepository;

    public Optional<Post> getPost(Integer postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public List<Post> getPostsOfUser(Integer userId) {
        return postRepository.findAllByUserId(userId);
    }

    public Post addPost(Post post) {
        post.setCreationDate(new Date());
        post.setId(null);

        Integer userId = post.getUser().getId();

        User user = userRepository.findById(userId).orElseThrow();

        post.setUser(user);

        userRepository.save(user);

        postRepository.saveAndFlush(post);

        logger.info(post.toString());

        return post;
    }

    public Integer deletePost(Integer postId) {
        postRepository.deleteById(postId);

        return postId;
    }
}

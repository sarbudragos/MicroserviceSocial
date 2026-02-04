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
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final UserRepository userRepository;
    private final String randomPostBaseUrl = "http://nodered:1880";
    private final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

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

    public Post addRandomPost(Integer userId) throws IOException, InterruptedException {
        Post randomPost = new Post();
        randomPost.setCreationDate(new Date());
        randomPost.setId(null);

        User user = userRepository.findById(userId).orElseThrow();
        randomPost.setUser(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(randomPostBaseUrl + "/random"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info(response.body());

        randomPost.setContent(response.body());

        userRepository.save(user);

        postRepository.saveAndFlush(randomPost);

        logger.info(randomPost.toString());

        return randomPost;
    }

    public Integer deletePost(Integer postId) {
        postRepository.deleteById(postId);

        return postId;
    }
}

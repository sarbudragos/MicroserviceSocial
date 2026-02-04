package org.example.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.model.DTO.PostDTO;
import org.example.apigateway.model.User;
import org.example.apigateway.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> getPost(
            @PathVariable Integer id
    ) throws Exception {

        PostDTO post = postService.getPost(id);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getPosts(
    ) throws Exception {

        List<PostDTO> post = postService.getAllPosts();

        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts-of/{user_id}")
    public ResponseEntity<List<PostDTO>> getPostsOfUser(
            @PathVariable Integer user_id
    ) throws Exception {

        List<PostDTO> post = postService.getPostsOfUser(user_id);

        return ResponseEntity.ok(post);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDTO> addPost(
            @RequestBody PostDTO post
    ) throws Exception {

        User user = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        logger.info(user.toString());
        post.setUserId(user.getId());
        logger.info(post.toString());
        PostDTO result = postService.addPost(post);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/posts/random")
    public ResponseEntity<PostDTO> addRandomPost() throws Exception {

        User user = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        logger.info(user.toString());
        PostDTO result = postService.addRandomPost(user.getId());

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Integer> deletePost(
            @PathVariable Integer id
    ) throws Exception {

        Integer result = postService.deletePost(id);

        return ResponseEntity.ok(result);
    }
}

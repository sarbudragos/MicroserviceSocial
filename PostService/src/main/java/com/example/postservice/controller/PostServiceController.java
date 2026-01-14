package com.example.postservice.controller;


import com.example.postservice.model.DTO.PostDTO;
import com.example.postservice.model.DTO.RpcRequest;
import com.example.postservice.model.Post;
import com.example.postservice.model.User;
import com.example.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class PostServiceController {
    private final PostService postService;
    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(PostServiceController.class);

    @RabbitListener(queues = "post-request")
    public String userRequest(RpcRequest request) {
        String command = request.getCommand();
        logger.info(command);
        return switch (command) {
            case "get_post" -> {
                try {
                    Post post = postService.getPost(objectMapper.readValue(request.getPayload(), Integer.class)).orElseThrow();
                    yield objectMapper.writeValueAsString(new PostDTO(
                            post.getId(),
                            post.getContent(),
                            post.getCreationDate(),
                            post.getUser().getId()
                    ));
                } catch (NoSuchElementException e) {
                    yield "not_found";
                } catch (Exception e) {
                    logger.error(Arrays.toString(e.getStackTrace()));
                    yield "error";
                }
            }
            case "get_posts_of_user" -> {
                try {
                    List<PostDTO> posts = postService.getPostsOfUser(objectMapper.readValue(request.getPayload(), Integer.class))
                            .stream()
                            .map(post -> new PostDTO(post.getId(), post.getContent(), post.getCreationDate(), post.getUser().getId()))
                            .toList();
                    yield objectMapper.writeValueAsString(posts);
                } catch (Exception e) {
                    logger.error(Arrays.toString(e.getStackTrace()));
                    yield "error";
                }
            }
            case "get_all_posts" -> {
                try {
                    List<PostDTO> posts = postService.getAllPosts()
                            .stream()
                            .map(post -> new PostDTO(post.getId(), post.getContent(), post.getCreationDate(), post.getUser().getId()))
                            .toList();
                    yield objectMapper.writeValueAsString(posts);
                } catch (Exception e) {
                    logger.error(Arrays.toString(e.getStackTrace()));
                    yield "error";
                }
            }
            case "add_post" -> {
                try {
                    PostDTO payloadPostDTO = objectMapper.readValue(request.getPayload(), PostDTO.class);
                    logger.info(payloadPostDTO.toString());
                    Post post = postService.addPost(new Post(
                            payloadPostDTO.getId(),
                            payloadPostDTO.getContent(),
                            payloadPostDTO.getCreationDate(),
                            User.builder().id(payloadPostDTO.getUserId()).build()
                    ));


                    PostDTO resultPostDTO = new PostDTO(
                            post.getId(),
                            post.getContent(),
                            post.getCreationDate(),
                            post.getUser().getId()
                    );
                    yield objectMapper.writeValueAsString(resultPostDTO);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    logger.error(Arrays.toString(e.getStackTrace()));
                    yield "error";
                }
            }
            case "delete_post" -> {
                try {
                    Integer postId = postService.deletePost(objectMapper.readValue(request.getPayload(), Integer.class));
                    yield objectMapper.writeValueAsString(postId);
                } catch (Exception e) {
                    logger.error(Arrays.toString(e.getStackTrace()));
                    yield "error";
                }
            }
            default -> "Unknown command";
        };
    }
}

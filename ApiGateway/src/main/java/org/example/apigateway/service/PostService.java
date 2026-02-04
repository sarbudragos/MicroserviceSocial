package org.example.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.model.DTO.PostDTO;
import org.example.apigateway.model.DTO.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final RabbitTemplate template;

    private final String routingKey = "routing-key";

    private final Exchange postExchange;
    ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(PostService.class);;


    public PostDTO getPost(Integer postId) throws Exception {
        RpcRequest rpcRequest = new RpcRequest("get_post", objectMapper.writeValueAsString(postId));

        String result = template.convertSendAndReceiveAsType(
                postExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

        if(result == null){
            throw new Exception("Message not received.");
        }
        if(result.equals("not_found")){
            throw new NoSuchElementException("No post with id " + postId);
        }
        if(result.equals("error")){
            throw new NoSuchElementException("An error occurred.");
        }

        return objectMapper.readValue(result, PostDTO.class);
    }

    public List<PostDTO> getPostsOfUser(Integer userId) throws Exception {
        RpcRequest rpcRequest = new RpcRequest("get_posts_of_user", objectMapper.writeValueAsString(userId));

        String result = template.convertSendAndReceiveAsType(
                postExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

        if(result == null){
            throw new Exception("Message not received.");
        }
        if(result.equals("error")){
            throw new NoSuchElementException("An error occurred.");
        }

        return objectMapper.readValue(result, new TypeReference<ArrayList<PostDTO>>() {});
    }

    public PostDTO addPost(PostDTO postDTO) throws Exception {
        RpcRequest rpcRequest = new RpcRequest("add_post", objectMapper.writeValueAsString(postDTO));

        String result = template.convertSendAndReceiveAsType(
                postExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

        if(result == null){
            throw new Exception("Message not received.");
        }
        if(result.equals("error")){
            throw new NoSuchElementException("An error occurred.");
        }

        return objectMapper.readValue(result, PostDTO.class);
    }

    public PostDTO addRandomPost(Integer userId) throws Exception {
        RpcRequest rpcRequest = new RpcRequest("add_random_post", objectMapper.writeValueAsString(userId));

        String result = template.convertSendAndReceiveAsType(
                postExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

        if(result == null){
            throw new Exception("Message not received.");
        }
        if(result.equals("error")){
            throw new NoSuchElementException("An error occurred.");
        }

        return objectMapper.readValue(result, PostDTO.class);
    }



    public Integer deletePost(Integer postId) throws Exception {
        RpcRequest rpcRequest = new RpcRequest("delete_post", objectMapper.writeValueAsString(postId));

        String result = template.convertSendAndReceiveAsType(
                postExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

        if(result == null){
            throw new Exception("Message not received.");
        }
        if(result.equals("error")){
            throw new NoSuchElementException("An error occurred.");
        }

        return objectMapper.readValue(result, Integer.class);
    }

    public List<PostDTO> getAllPosts() throws Exception {
        RpcRequest rpcRequest = new RpcRequest("get_all_posts", "");

        String result = template.convertSendAndReceiveAsType(
                postExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

        if(result == null){
            throw new Exception("Message not received.");
        }
        if(result.equals("error")){
            throw new NoSuchElementException("An error occurred.");
        }

        logger.info(result);
        return objectMapper.readValue(result, new TypeReference<>() {});
    }
}
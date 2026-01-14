package com.example.postservice.model.DTO;

import com.example.postservice.model.User;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Integer id;
    private String content;
    private Date creationDate;
    private Integer userId;
}

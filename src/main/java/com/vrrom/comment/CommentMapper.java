package com.vrrom.comment;

import com.vrrom.admin.Admin;
import com.vrrom.admin.mapper.AdminMapper;
import com.vrrom.application.model.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static List<CommentResponse> toCommentResponses(List<Comment> comments){
        if(comments == null) return null;
        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : comments) {
            responses.add(toResponse(comment));
        }
        return responses;
    }

    public static Comment toEntity(CommentRequest commentRequest, Application application, Admin admin) {
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setApplication(application);
        comment.setAdmin(admin);
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }

    private static CommentResponse toResponse(Comment comment){
       return new CommentResponse(
               comment.getCreatedAt(),
               comment.getText(),
               AdminMapper.toDTO(comment.getAdmin())
       );
    }
}

package com.vrrom.comment;

import com.vrrom.admin.dtos.AdminDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private LocalDateTime createdAt;
    private String text;
    private AdminDTO admin;
}

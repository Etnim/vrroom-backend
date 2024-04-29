package com.vrrom.application.dto;

import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.comment.CommentRequest;
import com.vrrom.comment.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseFromAdmin {
    private double interestRate;
    private BigDecimal agreementFee;
    private ApplicationStatus applicationStatus;
    private List<CommentResponse> comments;
}

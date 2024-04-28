package com.vrrom.application.dto;

import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.comment.CommentRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestFromAdmin {
    private double interestRate;
    private BigDecimal agreementFee;
    private ApplicationStatus applicationStatus;
    private CommentRequest comment;
}

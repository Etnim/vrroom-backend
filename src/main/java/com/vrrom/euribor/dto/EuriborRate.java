package com.vrrom.euribor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class EuriborRate {
    @JsonProperty("Euribor value")
    private double rate;
}

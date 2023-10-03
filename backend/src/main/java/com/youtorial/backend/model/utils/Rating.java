package com.youtorial.backend.model.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rating {

    @JsonProperty("sum")
    public int sum;

    @JsonProperty("ratingNum")
    public int ratingNum;

}

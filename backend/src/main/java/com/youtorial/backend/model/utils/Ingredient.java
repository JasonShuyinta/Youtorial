package com.youtorial.backend.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Ingredient implements Serializable {

    private String quantity;
    private String ing;
}

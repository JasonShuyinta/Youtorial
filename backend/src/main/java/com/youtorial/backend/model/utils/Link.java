package com.youtorial.backend.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Link implements Serializable {

    private String name;
    private String lk;
}

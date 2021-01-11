package com.example.demo.pojo;

import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer resCode;
    private String msg;
    private T data;
}

package com.example.demo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class JingFengResultDTO<T> {
    private Integer errcode;
    private String globalTicket;
    private T data;
}

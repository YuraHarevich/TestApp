package com.example.TestApp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MyErrorResponse {
    private final String message;
    private final String time;
}
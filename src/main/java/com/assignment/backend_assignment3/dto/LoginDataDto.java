package com.assignment.backend_assignment3.dto;

import lombok.Data;

@Data
public class LoginDataDto {
    private String token;
    private UserAccountDto user;
}

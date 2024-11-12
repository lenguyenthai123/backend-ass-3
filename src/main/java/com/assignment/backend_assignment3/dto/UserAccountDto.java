package com.assignment.backend_assignment3.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccountDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private String accessToken;
}

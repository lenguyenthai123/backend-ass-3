package com.assignment.backend_assignment3.service;

import com.assignment.backend_assignment3.dto.ApiResponseDto;
import com.assignment.backend_assignment3.dto.UserAccountDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserAccountService {
    ApiResponseDto register(UserAccountDto userAccount);

    ApiResponseDto login(UserAccountDto userAccount);

    ApiResponseDto getInfo(HttpServletRequest request);

    ApiResponseDto logout(HttpServletRequest request);

    UserAccountDto loadUserById(Long id);

    ApiResponseDto getProfile(HttpServletRequest request);

}

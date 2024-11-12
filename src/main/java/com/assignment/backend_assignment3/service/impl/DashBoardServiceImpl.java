package com.assignment.backend_assignment3.service.impl;

import com.assignment.backend_assignment3.domain.UserAccount;
import com.assignment.backend_assignment3.dto.ApiResponseDto;
import com.assignment.backend_assignment3.dto.UserAccountDto;
import com.assignment.backend_assignment3.repository.UserAccountRepository;
import com.assignment.backend_assignment3.service.DashBoardService;
import com.assignment.backend_assignment3.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public ApiResponseDto getDashBoardData(HttpServletRequest request) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        try {
            // Get some information to identify user from access token}
            UserAccountDto userContext = UserContext.getCurrentUser();
            Long id = userContext.getId();

            UserAccount userAccount = userAccountRepository.findById(id).orElse(null);
            apiResponseDto.setStatusCode("200");
            apiResponseDto.setMessage("Success");
            apiResponseDto.setData(userAccount);
        } catch (Exception e) {
            apiResponseDto.setStatusCode("500");
            apiResponseDto.setMessage("Internal Server Error");
        }
        return apiResponseDto;
    }
}

package com.assignment.backend_assignment3.service;

import com.assignment.backend_assignment3.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

public interface DashBoardService {

    ApiResponseDto getDashBoardData(HttpServletRequest request);
}

package com.assignment.backend_assignment3.controller;


import com.assignment.backend_assignment3.dto.ApiResponseDto;
import com.assignment.backend_assignment3.dto.UserAccountDto;
import com.assignment.backend_assignment3.service.DashBoardService;
import com.assignment.backend_assignment3.service.UserAccountService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

@RestController
public class DashBoardController {

    private final String root = "/home";

    @Autowired
    private DashBoardService dashBoardService;

    @ApiOperation("Get some data from home page")
    @GetMapping(value = root)
    public ResponseEntity<?> home(HttpServletRequest request) {
        ApiResponseDto responseDto = dashBoardService.getDashBoardData(request);
        return ResponseEntity.ok(responseDto);
    }
}

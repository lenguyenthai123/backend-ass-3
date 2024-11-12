package com.assignment.backend_assignment3.controller;

import com.assignment.backend_assignment3.dto.ApiResponseDto;
import com.assignment.backend_assignment3.dto.UserAccountDto;
import com.assignment.backend_assignment3.service.UserAccountService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController {

    private final String root = "/user";

    @Autowired
    private UserAccountService userAccountService;

    @ApiOperation("Register new user")
    @PostMapping(value = root + "/register")
    public ResponseEntity<?> register(@RequestBody UserAccountDto userAccount, HttpServletRequest request) {
        ApiResponseDto responseDto = userAccountService.register(userAccount);

        if (responseDto.getStatusCode().equals("SUCCESS")) return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        if (responseDto.getStatusCode().equals("FAIL"))
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation("Login user")
    @PostMapping(value = root + "/login")
    public ResponseEntity<?> login(@RequestBody UserAccountDto userAccount, HttpServletRequest request) {
        ApiResponseDto responseDto = userAccountService.login(userAccount);
        if (responseDto.getStatusCode().equals("SUCCESS")) return new ResponseEntity<>(responseDto, HttpStatus.OK);
        if (responseDto.getStatusCode().equals("FAIL"))
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation("Get user by token")
    @GetMapping(value = root + "/auth/info")
    public ResponseEntity<?> getUser( HttpServletRequest request) {
        ApiResponseDto responseDto = userAccountService.getInfo(request);
        if (responseDto.getStatusCode().equals("SUCCESS")) return new ResponseEntity<>(responseDto, HttpStatus.OK);
        if (responseDto.getStatusCode().equals("FAIL"))
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation("Logout user")
    @PostMapping(value = root + "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        ApiResponseDto responseDto = userAccountService.logout(request);
        if (responseDto.getStatusCode().equals("SUCCESS")) return new ResponseEntity<>(responseDto, HttpStatus.OK);
        if (responseDto.getStatusCode().equals("FAIL"))
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @ApiOperation("Get some data from profile page")
    @GetMapping(value = root + "/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        ApiResponseDto responseDto = userAccountService.getProfile(request);
        return ResponseEntity.ok(responseDto);
    }
}

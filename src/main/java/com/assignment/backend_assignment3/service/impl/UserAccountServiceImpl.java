package com.assignment.backend_assignment3.service.impl;

import com.assignment.backend_assignment3.config.JwtTokenProvider;
import com.assignment.backend_assignment3.domain.UserAccount;
import com.assignment.backend_assignment3.dto.ApiResponseDto;
import com.assignment.backend_assignment3.dto.LoginDataDto;
import com.assignment.backend_assignment3.dto.UserAccountDto;
import com.assignment.backend_assignment3.repository.UserAccountRepository;
import com.assignment.backend_assignment3.service.UserAccountService;
import com.assignment.backend_assignment3.service.mapstruct.UserAccountMapper;
import com.assignment.backend_assignment3.utils.PasswordUtils;
import com.assignment.backend_assignment3.utils.UserContext;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountRepository repository;

    @Autowired
    private UserAccountMapper mapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public ApiResponseDto register(UserAccountDto userAccount) {

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        // Kiểm tra nếu dữ liệu người dùng null
        if (userAccount == null) {
            apiResponseDto.setStatusCode("FAIL");
            apiResponseDto.setMessage("Dữ liệu không hợp lệ");
            return apiResponseDto;
        }

        // Kiểm tra nếu email hoặc mật khẩu rỗng
        if (StringUtils.isBlank(userAccount.getEmail()) || StringUtils.isBlank(userAccount.getPassword())) {
            apiResponseDto.setStatusCode("FAIL");
            apiResponseDto.setMessage("Email hoặc mật khẩu không được trống");
            return apiResponseDto;
        }

        // Kiểm tra nếu mật khẩu có độ dài dưới 6 ký tự
        if (userAccount.getPassword().length() < 6) {
            apiResponseDto.setStatusCode("FAIL");
            apiResponseDto.setMessage("Mật khẩu phải có ít nhất 6 ký tự");
            return apiResponseDto;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(userAccount.getEmail())) {
            apiResponseDto.setStatusCode("FAIL");
            apiResponseDto.setMessage("Email không hợp lệ");
            return apiResponseDto;
        }

        try {
            UserAccount anotherUserAccount = repository.findByEmail(userAccount.getEmail()).orElse(null);
            if (anotherUserAccount == null) {
                String hashedPassword = PasswordUtils.hashPassword(userAccount.getPassword());
                UserAccount newUserAccount = mapper.toEntity(userAccount);

                newUserAccount.setPassword(hashedPassword);
                newUserAccount.setCreatedAt(LocalDateTime.now());
                UserAccount savedUserAccount = repository.save(newUserAccount);

                apiResponseDto.setStatusCode("SUCCESS");
                apiResponseDto.setMessage("Đăng ký thành công");
            } else {
                apiResponseDto.setStatusCode("FAIL");
                apiResponseDto.setMessage("Email đã tồn tại");
            }
        } catch (Exception e) {
            apiResponseDto.setStatusCode("ERROR");
            apiResponseDto.setMessage("Lỗi hệ thống");
        }
        return apiResponseDto;
    }

    @Override
    @Transactional
    public ApiResponseDto login(UserAccountDto userAccount) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        if (userAccount == null) {
            apiResponseDto.setStatusCode("FAIL");
            apiResponseDto.setMessage("Dữ liệu không hợp lệ");
            apiResponseDto.setData(null);
            return apiResponseDto;
        }
        if (userAccount.getEmail() == null || userAccount.getEmail().isEmpty() || userAccount.getPassword() == null || userAccount.getPassword().isEmpty()) {
            apiResponseDto.setStatusCode("FAIL");
            apiResponseDto.setMessage("Email hoặc mật khẩu không hợp lệ");
            apiResponseDto.setData(null);
            return apiResponseDto;
        }

        try {
            UserAccount anotherUserAccount = repository.findByEmail(userAccount.getEmail()).orElse(null);
            if (anotherUserAccount != null) {
                if (PasswordUtils.verifyPassword(userAccount.getPassword(), anotherUserAccount.getPassword())) {
                    apiResponseDto.setStatusCode("SUCCESS");
                    apiResponseDto.setMessage("Đăng nhập thành công");

                    String accessToken = jwtTokenProvider.generateToken(anotherUserAccount);
                    anotherUserAccount.setAccessToken(accessToken);
                    repository.save(anotherUserAccount);

                    LoginDataDto loginDataDto = new LoginDataDto();
                    loginDataDto.setToken(accessToken);
                    loginDataDto.setUser(mapper.toDto(anotherUserAccount));
                    apiResponseDto.setData(loginDataDto);

                } else {
                    apiResponseDto.setStatusCode("FAIL");
                    apiResponseDto.setMessage("Email hoặc mật khẩu không đúng");
                    apiResponseDto.setData(null);
                }
            } else {
                apiResponseDto.setStatusCode("FAIL");
                apiResponseDto.setMessage("Email hoặc mật khẩu không đúng");
                apiResponseDto.setData(null);
            }
        } catch (Exception e) {
            apiResponseDto.setStatusCode("ERROR");
            apiResponseDto.setMessage("Lỗi hệ thống");
            apiResponseDto.setData(null);
        }
        return apiResponseDto;

    }

    @Override
    public ApiResponseDto getInfo(HttpServletRequest request) {
        UserAccountDto userContext = UserContext.getCurrentUser();
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        if (userContext != null) {
            apiResponseDto.setStatusCode("SUCCESS");
            apiResponseDto.setMessage("Đã xác thực");
            apiResponseDto.setData(userContext);
        } else {
            apiResponseDto.setStatusCode("FAIL");
            apiResponseDto.setMessage("Chưa xác thực");
        }
        return apiResponseDto;
    }

    @Override
    @Transactional
    public ApiResponseDto logout(HttpServletRequest request) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        try {
            UserAccountDto userContext = UserContext.getCurrentUser();
            Long id = userContext.getId();
            UserAccount userAccount = repository.findById(id).orElse(null);
            if (userAccount != null) {
                userAccount.setAccessToken(null);
                repository.save(userAccount);
                apiResponseDto.setStatusCode("SUCCESS");
                apiResponseDto.setMessage("Đăng xuất thành công");
            } else {
                apiResponseDto.setStatusCode("FAIL");
                apiResponseDto.setMessage("Người dùng không tồn tại");
            }
        } catch (Exception e) {
            apiResponseDto.setStatusCode("ERROR");
            apiResponseDto.setMessage("Lỗi hệ thống");
        }
        return apiResponseDto;
    }

    @Override
    public UserAccountDto loadUserById(Long id) {
        UserAccount userAccount = repository.findById(id).orElse(null);
        if (userAccount != null) {
            return mapper.toDto(userAccount);
        }
        return null;
    }

    @Override
    public ApiResponseDto getProfile(HttpServletRequest request) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        try {
            // Get some information to identify user from access token}
            UserAccountDto userContext = UserContext.getCurrentUser();
            Long id = userContext.getId();

            UserAccount userAccount = repository.findById(id).orElse(null);
            UserAccountDto userAccountDto = mapper.toDto(userAccount);
            userAccountDto.setAccessToken(null);
            apiResponseDto.setStatusCode("200");
            apiResponseDto.setMessage("SUCCESS");
            apiResponseDto.setData(userAccountDto);
        } catch (Exception e) {
            apiResponseDto.setStatusCode("500");
            apiResponseDto.setMessage("Internal Server Error");
        }
        return apiResponseDto;
    }

    // Hàm kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        // Biểu thức chính quy để kiểm tra email hợp lệ
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Kiểm tra email với biểu thức chính quy
        return email.matches(emailRegex);
    }

}

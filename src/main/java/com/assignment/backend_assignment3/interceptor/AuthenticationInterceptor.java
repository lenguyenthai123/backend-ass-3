package com.assignment.backend_assignment3.interceptor;

import com.assignment.backend_assignment3.config.JwtTokenProvider;
import com.assignment.backend_assignment3.dto.UserAccountDto;
import com.assignment.backend_assignment3.service.UserAccountService;
import com.assignment.backend_assignment3.service.mapstruct.UserAccountMapper;
import com.assignment.backend_assignment3.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Component
@Log
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserAccountService service;

    @Autowired
    private UserAccountMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Bỏ qua xác thực cho các yêu cầu OPTIONS (preflight request)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String errMessage = "";
        try {
            // Lấy jwt từ request
            String jwt = getJwtFromRequest(request);
            int status = tokenProvider.validateToken(jwt);
            if (StringUtils.hasText(jwt) && status == 1) {
                // Lấy id user từ chuỗi jwt
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                // Lấy thông tin người dùng từ id
                UserAccountDto foundUser = service.loadUserById(userId);

                if (foundUser != null && Objects.equals(foundUser.getAccessToken(), jwt)) {
                    // Nếu người dùng hợp lệ, set thông tin cho Security Context
                    UserContext.setCurrentUser(foundUser);
                    return true;
                }
                if (foundUser != null && !Objects.equals(foundUser.getAccessToken(), jwt)) {
                    errMessage = "Phiên đăng nhập đã hết hạn";
                }
            }
            errMessage = status != 2 ? "Bạn không có quyền truy cập vào tài nguyên này" : "Phiên đăng nhập đã hết hạn";
        } catch (Exception ex) {
            log.info("failed on set user authentication");
            errMessage = "Lỗi xác thực người dùng";
        }
        response.setCharacterEncoding("UTF-8");  // Đặt mã hóa ký tự
        response.setContentType("text/html; charset=UTF-8");  // Đặt nội dung là HTML với UTF-8
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Đặt mã trạng thái 401
        response.getWriter().write(errMessage);  // Ghi nội dung với các ký tự có dấu
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContext.clear();
        return;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

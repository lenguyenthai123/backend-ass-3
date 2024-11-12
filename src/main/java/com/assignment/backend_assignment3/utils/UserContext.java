package com.assignment.backend_assignment3.utils;

import com.assignment.backend_assignment3.dto.UserAccountDto;

public class UserContext {

    private static final ThreadLocal<UserAccountDto> currentUser = new ThreadLocal<>();

    // Đặt thông tin người dùng
    public static void setCurrentUser(UserAccountDto user) {
        currentUser.set(user);
    }

    // Lấy thông tin người dùng
    public static UserAccountDto getCurrentUser() {
        return currentUser.get();
    }

    // Xóa thông tin người dùng sau khi xử lý xong yêu cầu
    public static void clear() {
        currentUser.remove();
    }
}
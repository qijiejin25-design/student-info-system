package com.qijiejin.studentinfo.controller;

import com.qijiejin.studentinfo.dto.ApiResponse;
import com.qijiejin.studentinfo.dto.LoginRequest;
import com.qijiejin.studentinfo.entity.SysUser;
import com.qijiejin.studentinfo.interceptor.LoginInterceptor;
import com.qijiejin.studentinfo.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest req, HttpSession session) {
        SysUser user = authService.login(req.getUsername(), req.getPassword());
        session.setAttribute(LoginInterceptor.SESSION_USER_KEY, user);
        return ApiResponse.ok(Map.of(
                "username", user.getUsername(),
                "nickname", user.getNickname(),
                "sessionId", session.getId()
        ));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.ok();
    }

    @GetMapping("/me")
    public ApiResponse<?> me(HttpSession session) {
        Object u = session.getAttribute(LoginInterceptor.SESSION_USER_KEY);
        return ApiResponse.ok(u);
    }
}

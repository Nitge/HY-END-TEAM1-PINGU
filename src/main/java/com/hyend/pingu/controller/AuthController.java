package com.hyend.pingu.controller;

import com.hyend.pingu.dto.LoginRequestDTO;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequestDTO loginRequestDTO,
                                        HttpServletRequest request) {
        User user = userService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user.getId());
            session.setMaxInactiveInterval(1800);
            return ResponseEntity.ok(user.getId());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(-1L);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("logout success");
    }
}

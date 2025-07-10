package com.lion.CalPick.service;

import com.lion.CalPick.domain.User;
import com.lion.CalPick.domain.UserPrincipal;
import com.lion.CalPick.dto.LoginRequest;
import com.lion.CalPick.dto.LoginResponse;
import com.lion.CalPick.dto.SignUpRequest;
import com.lion.CalPick.repository.UserRepository;
import com.lion.CalPick.util.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void signup(SignUpRequest signupRequest) {
        if (userRepository.existsByUserId(signupRequest.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User user = new User();
        System.out.println("Received userId in AuthService: " + signupRequest.getUserId()); // Debug log
        user.setUserId(signupRequest.getUserId());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        System.out.println("Received nickname in AuthService: " + signupRequest.getNickname()); // Debug log
        user.setNickname(signupRequest.getNickname());

        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserId(),
                            loginRequest.getPassword()
                    )
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateToken(userPrincipal.getUsername(), userPrincipal.getNickname());
            return new LoginResponse(accessToken, userPrincipal.getNickname(), userPrincipal.getUsername());
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        } catch (Exception e) {
            throw new RuntimeException("로그인 처리 중 서버 오류가 발생했습니다.", e);
        }
    }
}

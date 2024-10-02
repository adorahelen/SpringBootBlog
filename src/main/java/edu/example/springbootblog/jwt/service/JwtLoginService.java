package edu.example.springbootblog.jwt.service;

import edu.example.springbootblog.domain.User;
import edu.example.springbootblog.dto.LoginRequest;
import edu.example.springbootblog.dto.UserDTO;
import edu.example.springbootblog.jwt.config.JwtUtil;
import edu.example.springbootblog.jwt.controller.dto.JwtInfoDto;
import edu.example.springbootblog.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtLoginService {

    private static final Logger logger = LoggerFactory.getLogger(JwtLoginService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public JwtInfoDto login(LoginRequest loginRequestDto) {
        String email = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("로그인 실패: 이메일 '{}'에 대한 비밀번호가 일치하지 않습니다.", email);

            throw new BadCredentialsException("Not Matches password");
        }

        UserDTO userDTO = user.toUserInfoDto();

        logger.info("로그인 성공: 이메일 '{}'에 대한 토큰이 생성되었습니다.", email);

        return jwtUtil.createToken(userDTO);
    }
}
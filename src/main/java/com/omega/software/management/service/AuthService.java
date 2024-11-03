package com.omega.software.management.service;

import com.omega.software.management.data.auth.JwtUtils;
import com.omega.software.management.data.dto.LoginRequest;
import com.omega.software.management.data.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().toLowerCase();
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwt(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt)
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getFirstName() + " " + userDetails.getLastName(),
                        userDetails.getEmail(),
                        roles, jwt));
    }
}

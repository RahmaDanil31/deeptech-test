package com.deeptech.deeptech_test.Controller;

import com.deeptech.deeptech_test.BaseAsset.ResponseData;
import com.deeptech.deeptech_test.Dto.LoginRequestDto;
import com.deeptech.deeptech_test.Service.CustomUserDetailsService;
import com.deeptech.deeptech_test.Service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<String>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        ResponseData<String> response;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDto.getEmail());
            String token = tokenService.createToken(userDetails.getUsername());

            response = new ResponseData<>(token, "Login successful", 200);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException e) {
            response = new ResponseData<>(null, "Invalid credentials", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseData<String>> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseData<String> response;
        try {
            String authorization = authorizationHeader.substring(7);
            String token = tokenService.refreshToken(authorization);
            response = new ResponseData<>(token, HttpStatus.OK.toString(), 200);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response = new ResponseData<>(null, "Invalid credentials", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseData<String>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseData<String> response;
        try {
            String authorization = authorizationHeader.substring(7);
            tokenService.logout(authorization);
            response = new ResponseData<>("Logout successful", HttpStatus.OK.toString(), 200);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response = new ResponseData<>(null, "Invalid credentials", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

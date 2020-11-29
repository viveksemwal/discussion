package com.vivek.discussion.controller;

import com.vivek.discussion.dto.AuthenticationResponse;
import com.vivek.discussion.dto.LoginRequest;
import com.vivek.discussion.dto.RegisterRequest;
import com.vivek.discussion.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        log.info(registerRequest.getEmail());
        authService.signup(registerRequest);

        return new ResponseEntity<>("user registration link send", HttpStatus.OK );
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verify(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("User is register",HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);


    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.status(OK).body(" Token Deleted Successfully!!");
    }


}

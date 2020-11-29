package com.vivek.discussion.service;

import com.vivek.discussion.dto.AuthenticationResponse;
import com.vivek.discussion.dto.LoginRequest;
import com.vivek.discussion.dto.RegisterRequest;
import com.vivek.discussion.exceptions.SpringDiscussionException;
import com.vivek.discussion.model.NotificationEmail;
import com.vivek.discussion.model.User;
import com.vivek.discussion.model.VerificationToken;
import com.vivek.discussion.repository.UserRepository;
import com.vivek.discussion.repository.VerificationTokenRepository;
import com.vivek.discussion.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class AuthService {

    // constructor injection is recommended over direct autowired annotation
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserDetailsService userDetailsService;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token=generateRandomToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate Your Account",user.getEmail(),"thank you For Signing In Into our disscusion form" +
                "Please click the link below to get : "+
                "http://localhost:8080/api/auth/accountVerification/"+token));

    }

    private String generateRandomToken (User user){
        String s= UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(s);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return s;

    }

    public void verifyAccount(String token) {
       Optional<VerificationToken> verificationToken= verificationTokenRepository.findAllByToken(token);
       verificationToken.orElseThrow(() -> new SpringDiscussionException("Invalid Token"));
       fetchUserAndUnable(verificationToken.get());

    }

    @Transactional
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new SpringDiscussionException("User name not found - " + principal.getUsername()));
    }

    @Transactional
    void fetchUserAndUnable(VerificationToken verificationToken) {
        String username=verificationToken.getUser().getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()-> new SpringDiscussionException("User not found with name : "+username));
        user.setEnabled(true);
        userRepository.save(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails details=userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token= jwtProvider.generateToken(details);
        return new AuthenticationResponse(token,loginRequest.getUsername());
    }


}

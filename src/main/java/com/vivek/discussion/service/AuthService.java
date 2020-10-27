package com.vivek.discussion.service;

import com.vivek.discussion.dto.RegisterRequest;
import com.vivek.discussion.model.NotificationEmail;
import com.vivek.discussion.model.User;
import com.vivek.discussion.model.VerificationToken;
import com.vivek.discussion.repository.UserRepository;
import com.vivek.discussion.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    // constructor injection is recommended over direct autowired annotation
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

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
        mailService.sendMail(new NotificationEmail("Please Activate Your Account",user.getEmail(),"thankyou For Signing In Into our disscusion form" +
                "Please click the link below to get"+
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
}

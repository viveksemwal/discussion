package com.vivek.discussion.security;

import com.vivek.discussion.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.security.Key;


@Service
public class JwtProvider {

    private Key key;

    @PostConstruct
    public void init(){
       key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(Authentication authentication){
        UserDetails principal=(UserDetails) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername())
                .signWith(key)
                .compact();
    }
}

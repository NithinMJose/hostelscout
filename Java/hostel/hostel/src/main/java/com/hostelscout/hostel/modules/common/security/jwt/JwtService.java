package com.hostelscout.hostel.modules.common.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hostelscout.hostel.modules.common.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration-minutes}")
    private long jwtExpirationMinutes;

    public String generateToken(String subject, Role role) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        Instant now = Instant.now();
        return JWT.create()
                .withSubject(subject)
                .withClaim("role", role.name())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(jwtExpirationMinutes, ChronoUnit.MINUTES)))
                .sign(algorithm);
    }

    public String validateAndGetSubject(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    public String validateAndGetRole(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getClaim("role")
                .asString();
    }



}



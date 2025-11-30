package com.hostelscout.hostel.modules.common.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret:default-secret-please-change}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration-minutes:60}")
    private long jwtExpirationMinutes;

    public String generateToken(String subject) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(jwtExpirationMinutes, ChronoUnit.MINUTES)))
                .sign(algorithm);
    }

}


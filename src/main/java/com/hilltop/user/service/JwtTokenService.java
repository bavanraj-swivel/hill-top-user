package com.hilltop.user.service;

import com.hilltop.user.exception.TokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt token service
 */
@Service
public class JwtTokenService {

    //Secret used to encrypt & decrypt token
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * This method is used to validate jwt token.
     *
     * @param token jwt token
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        } catch (JwtException e) {
            throw new TokenException("Invalid token", e);
        }
    }

    /**
     * This method is used to generate jwt token.
     *
     * @param mobileNo mobileNo
     * @return jwt token
     */
    public String generateToken(String mobileNo) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(mobileNo)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * This method is used to create key to encrypt & decrypt jwt token.
     *
     * @return key
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

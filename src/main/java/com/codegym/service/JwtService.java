package com.codegym.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "12345678901234567890123456789012"; // ít nhất 32 ký tự
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30 phút

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Tạo token mới
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Giải mã và lấy username từ token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Kiểm tra token hợp lệ
    public boolean isTokenValid(String token, String username) {
        String extracted = extractUsername(token);
        return extracted.equals(username) && !isTokenExpired(token);
    }

    // Kiểm tra token hết hạn chưa
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Hàm parse token mới theo chuẩn 0.12.x
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

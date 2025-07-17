package com.example.shopapp.configurations;

import com.example.shopapp.entities.User;
import com.example.shopapp.exceptions.InvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("dateOfBirth", user.getDateOfBirth());
        generateSecretKey();
        try{
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() * 1000L + expiration)) // get current time in seconds, then plus "expiration"
                    .signWith(createSecretKey(), SignatureAlgorithm.HS256)
                                .compact();
            return token;
        }
        catch (Exception ex){
            throw new InvalidParamException("Cannot generate token, error: " + ex.getMessage());
        }
    }

    private Key createSecretKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    // Use to generate String secretKey
    private void generateSecretKey(){
        byte[] bytes = new byte[48];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        secretKey = Encoders.BASE64.encode(bytes); // From bytes to String -> Encode
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claim = this.extractAllClaims(token);
        return claimsResolver.apply(claim);
    }

    public boolean isTokenExpired(String token){
        Date expiration = this.extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}

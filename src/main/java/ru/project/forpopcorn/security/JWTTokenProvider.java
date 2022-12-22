package ru.project.forpopcorn.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.project.forpopcorn.entity.User;

import java.util.Date;

@Component
public class JWTTokenProvider {

    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + SecurityConstans.EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(user.getNickname())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstans.SECRET)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(SecurityConstans.SECRET) // SecretKey to verify the JWS signature
                    .parseClaimsJws(token); // Produce original JWS
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex) {
            LOG.error(ex.getMessage());
            return false;
        }
    }

    public String getNicknameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(SecurityConstans.SECRET) // SecretKey to verify the JWS signature
                .parseClaimsJws(token) // Produce original JWS
                .getBody().getSubject();
    }

}

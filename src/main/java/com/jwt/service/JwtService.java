package com.jwt.service;


import com.jwt.model.RefreshToken;
import com.jwt.model.UserPrinciple;
import com.jwt.repository.RefreshTokenRepository;
import com.jwt.repository.UserRepo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Component
@Service
public class JwtService {

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @Autowired
  UserRepo userRepo;
  private static final String SECRET_KEY = "123456789";
  private static final long EXPIRE_TIME = 30000;
  private static final Logger logger = LoggerFactory.getLogger(JwtService.class.getName());

  public String generateTokenLogin(Authentication authentication) {
    UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME * 1000))
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME)).signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature -> Message: {} ", e);
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token -> Message: {}", e);
    } catch (ExpiredJwtException e) {
      logger.error("Expired JWT token -> Message: {}", e);
    } catch (UnsupportedJwtException e) {
      logger.error("Unsupported JWT token -> Message: {}", e);
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty -> Message: {}", e);
    }

    return false;
  }

  public String getUserNameFromJwtToken(String token) {

    String userName = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody().getSubject();
    return userName;
  }
}

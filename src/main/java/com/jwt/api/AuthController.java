package com.jwt.api;

import com.jwt.exception.TokenRefreshException;
import com.jwt.model.*;
import com.jwt.service.JwtService;
import com.jwt.service.RefreshTokenService;
import com.jwt.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    //Xác thực username và password
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

    //Set thông tin đã authenticate vào Security Context
    SecurityContextHolder.getContext().setAuthentication(authentication);


    //Trả lại JWT cho người dùng
    String jwt = jwtService.generateTokenLogin(authentication);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    User currentUser = userService.findByUsername(user.getUsername()).get();
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(currentUser.getId());
    return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), currentUser.getFullName(), userDetails.getAuthorities(), refreshToken.getToken()));
  }


  @GetMapping("/random")
  public String randomStuff(){
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    String username = userDetails.getUsername();
    Optional<User> user = userService.findByUsername(username);
    if(user.isPresent()){
      return new String("JWT Hợp lệ mới có thể thấy được message này");
    }else{
      return "Không phải người dùng này";
    }

  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              String token = jwtService.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }


}

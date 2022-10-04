package com.jwt.api;

import com.jwt.model.JwtResponse;
import com.jwt.model.User;
import com.jwt.service.JwtService;
import com.jwt.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

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
    return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), currentUser.getFullName(), userDetails.getAuthorities()));
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


}

package com.jwt.model;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {

  private Long id;
  private String token;
  private String type = "Bearer";
  private String username;
  private String refreshToken;
  private String name;
  private Collection<? extends GrantedAuthority> roles;

  public JwtResponse(String accessToken, Long id, String username, String name, Collection<? extends GrantedAuthority> roles, String refreshToken) {
    this.token = accessToken;
    this.username = username;
    this.roles = roles;
    this.name = name;
    this.id = id;
    this.refreshToken = refreshToken;
  }


  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void setRoles(Collection<? extends GrantedAuthority> roles) {
    this.roles = roles;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Collection<? extends GrantedAuthority> getRoles() {
    return roles;
  }
}

package com.jwt.service;

import com.jwt.model.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends GeneralService<User>, UserDetailsService {
  Optional<User> findByUsername(String username);
}

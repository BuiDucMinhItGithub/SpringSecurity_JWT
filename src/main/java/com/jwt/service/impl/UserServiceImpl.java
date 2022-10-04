package com.jwt.service.impl;

import com.jwt.model.User;
import com.jwt.model.UserPrinciple;
import com.jwt.repository.UserRepo;
import com.jwt.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {


  @Autowired
  UserRepo userRepo;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public Iterable<User> findAll() {
    return userRepo.findAll();
  }

  @Override
  public Optional<User> findById(Long id) {
    return userRepo.findById(id);
  }

  @Override
  public User save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepo.save(user);
  }

  @Override
  public void remove(Long id) {
    userRepo.deleteById(id);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepo.findByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepo.findByUsername(username);
    if (!userOptional.isPresent()) {
      throw new UsernameNotFoundException(username);
    }
    return UserPrinciple.build(userOptional.get());
  }
}

package com.jwt.service.impl;

import com.jwt.model.Role;
import com.jwt.repository.RoleRepo;
import com.jwt.service.RoleService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  RoleRepo roleRepo;

  @Override
  public Iterable<Role> findAll() {
    return roleRepo.findAll();
  }

  @Override
  public Optional<Role> findById(Long id) {
    return roleRepo.findById(id);
  }

  @Override
  public Role save(Role role) {
    return roleRepo.save(role);
  }

  @Override
  public void remove(Long id) {
     roleRepo.deleteById(id);
  }

  @Override
  public Role findByName(String name) {
    return roleRepo.findByName(name);
  }
}

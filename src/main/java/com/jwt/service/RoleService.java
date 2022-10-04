package com.jwt.service;

import com.jwt.model.Role;

public interface RoleService extends GeneralService<Role> {
  Role findByName(String name);
}

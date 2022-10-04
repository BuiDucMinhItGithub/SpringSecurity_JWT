package com.jwt;

import com.jwt.model.Role;
import com.jwt.model.User;
import com.jwt.service.RoleService;
import com.jwt.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtApplication {

  @Autowired
  private UserService userService;
  @Autowired
  private RoleService roleService;

  public static void main(String[] args) {
    SpringApplication.run(JwtApplication.class, args);
  }

  @PostConstruct
  public void init() {
    List<User> users = (List<User>) userService.findAll();
    List<Role> roleList = (List<Role>) roleService.findAll();
    if (roleList.isEmpty()) {
      Role roleAdmin = new Role();
      roleAdmin.setId(1L);
      roleAdmin.setName("ROLE_ADMIN");
      roleService.save(roleAdmin);
      Role roleUser = new Role();
      roleUser.setId(2L);
      roleUser.setName("ROLE_USER");
      roleService.save(roleUser);
    }
    if (users.isEmpty()) {
      User admin = new User();
      Set<Role> roles = new HashSet<>();
      Role roleAdmin = new Role();
      roleAdmin.setId(1L);
      roleAdmin.setName("ROLE_ADMIN");
      roles.add(roleAdmin);
      admin.setUsername("admin");
      admin.setFullName("admin");
      admin.setPassword("123456");
      admin.setEmail("email@gmail.com");
      admin.setRoles(roles);
      userService.save(admin);
    }
  }

}

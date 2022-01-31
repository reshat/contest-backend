package com.group.contestback.controllers;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Role;
import com.group.contestback.services.AppUserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final AppUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @PostMapping("/user/add")
    public ResponseEntity<AppUser> addUsers(@RequestBody AppUser user) {
        return ResponseEntity.ok().body(userService.saveAppUser(user));
    }
    @PostMapping("/role/add")
    public ResponseEntity<Role> addUsers(@RequestBody Role role) {
        return ResponseEntity.ok().body(userService.saveRole(role));
    }
    @PostMapping("/user/addrole")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }
}
@Data
class RoleToUserForm {
    private String username;
    private String rolename;
}

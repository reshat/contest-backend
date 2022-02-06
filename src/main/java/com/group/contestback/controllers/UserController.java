package com.group.contestback.controllers;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Role;
import com.group.contestback.services.AppUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"User controller"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class UserController {
    private final AppUserService userService;


    @ApiOperation(value = "Возращает всех пользователей")
    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @ApiOperation(value = "Добавляет нового пользователя", notes = "Роли указывать необязательно, для этого существует другой запрос")
    @PostMapping("/user/add")
    public ResponseEntity<AppUser> addUsers(@RequestBody AppUser user) {
        return ResponseEntity.ok().body(userService.saveAppUser(user));
    }
    @ApiOperation(value = "Добавляет новую роль")
    @PostMapping("/role/add")
    public ResponseEntity<Role> addUsers(@RequestBody Role role) {
        return ResponseEntity.ok().body(userService.saveRole(role));
    }
    @ApiOperation(value = "Добавляет роль к пользователю")
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

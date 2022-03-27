package com.group.contestback.controllers;

import com.group.contestback.models.AppUser;
import com.group.contestback.services.AppUserService;
import com.group.contestback.services.EmailServiceCS;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"User controller"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {
    private final AppUserService userService;
    @Autowired
    private EmailServiceCS emailServiceCS;

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
    @ApiOperation(value = "Добавляет роль к пользователю")
    @PostMapping("/user/addrole")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getLogin(), form.getRoleName(), form.getDescription());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Изменяет группу пользователя")
    @PostMapping("/user/setgroup")
    public ResponseEntity<?> setUserGroup(@RequestBody GroupToUserForm form) {
        userService.setUserGroup(form.getLogin(), form.getGroupId());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Отправка тестового собщения на почту разработчика")
    @PostMapping("/user/sendmail")
    public void sendMail() {
        emailServiceCS.sendSimpleMessage("reshat.sultan@yandex.ru","subj","test");
    }
    @ApiOperation(value = "Добавляет почту к пользователю")
    @PostMapping("/user/addEmail")
    public ResponseEntity<?> addEmailToUser(@RequestBody EmailToUserForm form) {
        userService.addEmailToUser(form.getLogin(), form.getEmail());
        return ResponseEntity.ok().build();
    }
}
@Data
class RoleToUserForm {
    private String login;
    private String roleName;
    private String description;
}
@Data
class EmailToUserForm {
    private String login;
    private String email;
}
@Data
class GroupToUserForm {
    private String login;
    private Integer groupId;
}

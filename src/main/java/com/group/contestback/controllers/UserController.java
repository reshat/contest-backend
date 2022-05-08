package com.group.contestback.controllers;

import com.group.contestback.models.AppUser;
import com.group.contestback.responseTypes.UserPageResponse;
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
import org.springframework.data.domain.Page;
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

    @ApiOperation(value = "Возращает всех пользователей")
    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @ApiOperation(value = "Возращает определенную страницу, определенного размера пользователей")
    @GetMapping("/usersPage")
    public ResponseEntity<Page<UserPageResponse>> getUsersPage(@RequestBody PageSize pageSize) {
        return ResponseEntity.ok().body(userService.getUsersPage(pageSize.getPage(), pageSize.getPageSize()));
    }
    @ApiOperation(value = "Возращает определенную страницу, определенного размера пользователей")
    @GetMapping("/usersPageFind")
    public ResponseEntity<Page<UserPageResponse>> getUsersPageFind(@RequestBody PageSizeName pageSizeName) {
        return ResponseEntity.ok().body(userService.findUsersByLastNamePage(pageSizeName.getPage(), pageSizeName.getPageSize(), pageSizeName.getStr()));
    }
    @ApiOperation(value = "Добавляет нового пользователя", notes = "Роли указывать необязательно, для этого существует другой запрос")
    @PostMapping("/user/add")
    public ResponseEntity<?> addUsers(@RequestBody UserRegistration user) {
        AppUser appUser = new AppUser(user.getFirstName(),user.getLastName(),user.getMiddleName(),user.getLogin(),
                user.getPassword(), "", user.getEmail(), 2, null);
        return ResponseEntity.ok().body(userService.saveAppUser(appUser));
    }
    @ApiOperation(value = "Добавляет роль к пользователю")
    @PostMapping("/addrole")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) throws Exception {
        userService.addRoleToUser(form.getLogin(), form.getRoleName(), form.getDescription());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Изменяет группу пользователя")
    @PostMapping("/user/setgroup")
    public ResponseEntity<?> setUserGroup(@RequestBody GroupToUserForm form) {
        userService.setUserGroup(form.getLogin(), form.getGroupId());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Добавляет почту к пользователю")
    @PostMapping("/user/addEmail")
    public ResponseEntity<?> addEmailToUser(@RequestBody EmailToUserForm form) {
        userService.addEmailToUser(form.getLogin(), form.getEmail());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Сброс пароля")
    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword form) {
        userService.resetPassword(form.getLogin(), form.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
@Data
class UserRegistration {
    private String firstName;
    private String middleName;
    private String lastName;
    private String login;
    private String password;
    private String email;
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
@Data
class PageSize {
    private Integer page;
    private Integer pageSize;
}
@Data
class PageSizeName {
    private Integer page;
    private Integer pageSize;
    private String str;
}
@Data
class ResetPassword {
    private String login;
    private String newPassword;
}


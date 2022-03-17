package com.group.contestback.services;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Roles;

import java.util.List;

public interface AppUserService {
    AppUser saveAppUser(AppUser user);
    void addRoleToUser(String login, String roleName, String description);
    AppUser getAppUser(String login);
    List<AppUser> getUsers();
    void addEmailToUser(String login, String email);
}

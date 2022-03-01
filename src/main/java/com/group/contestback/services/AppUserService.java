package com.group.contestback.services;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Role;

import java.util.List;

public interface AppUserService {
    AppUser saveAppUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String role);
    AppUser getAppUser(String username);
    List<AppUser> getUsers();
    void addEmailToUser(String username, String email);
}

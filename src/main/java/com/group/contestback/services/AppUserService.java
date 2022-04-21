package com.group.contestback.services;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Roles;
import com.group.contestback.responseTypes.UserPageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AppUserService {
    AppUser saveAppUser(AppUser user);
    void addRoleToUser(String login, String roleName, String description);
    AppUser getAppUser(String login);
    List<AppUser> getUsers();
    Page<UserPageResponse> getUsersPage(int page, int pageSize);
    Page<UserPageResponse> findUsersByLastNamePage(int page, int pageSize, String str);

    void addEmailToUser(String login, String email);
    void setUserGroup(String login, Integer id);
}

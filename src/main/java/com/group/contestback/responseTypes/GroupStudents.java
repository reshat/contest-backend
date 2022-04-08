package com.group.contestback.responseTypes;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Attempts;
import com.group.contestback.models.Groups;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupStudents{
    Groups groups;
    List<UserTasks> userTasks = new ArrayList<>();
    public void addUser(AppUser user, List<Attempts> manualAttempts) {
        this.userTasks.add(new UserTasks(user, manualAttempts));
    }
}
@Data
class UserTasks{
    AppUser user;
    List<Attempts> manualAttempts;

    public UserTasks(AppUser user, List<Attempts> manualAttempts) {
        this.user = user;
        this.manualAttempts = manualAttempts;
    }
}

package com.group.contestback.services;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Groups;
import com.group.contestback.models.Roles;
import com.group.contestback.repositories.AppUserRepo;
import com.group.contestback.repositories.GroupsRepo;
import com.group.contestback.repositories.RolesRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class AppUserServiceClass implements AppUserService, UserDetailsService {
    private final AppUserRepo userRepo;
    private final RolesRepo rolesRepo;
    private final GroupsRepo groupsRepo;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private EmailServiceCS emailServiceCS;
    @Override
    public AppUser saveAppUser(AppUser user) {
        user.setPassHash(passwordEncoder.encode(user.getPassHash()));
        log.info("Registering new user" + user.getLogin() + " " + user.getEmail());
        emailServiceCS.sendSimpleMessage(user.getEmail(),"Регистрация","Вы были успешно зарегистрированы");
        return userRepo.save(user);
    }

    @Override
    public void addRoleToUser(String login, String roleName, String description) {
        AppUser user = userRepo.findByLogin(login);
        Roles roles = rolesRepo.findByName(roleName);
        if(user == null) {
            log.error("user not found");
        } else if (roles == null) {
            log.error("role not found");
        } else {
            user.setRoleId(roles.getId());
        }
    }

    @Override
    public AppUser getAppUser(String login) {
        return userRepo.findByLogin(login);
    }

    @Override
    public List<AppUser> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public void addEmailToUser(String login, String email) {
        AppUser user = userRepo.findByLogin(login);
        emailServiceCS.sendSimpleMessage(user.getEmail(),"Изменение почты","Ваша почта была изменена на " + email);
        user.setEmail(email);
        emailServiceCS.sendSimpleMessage(user.getEmail(),"Изменение почты","Ваша почта была изменена на " + email);
    }

    @Override
    public void setUserGroup(String login, Integer id) {
        AppUser user = userRepo.findByLogin(login);
        Optional<Groups> groups = groupsRepo.findById(id);
        if(user == null) {
            log.error("user not found");
        } else if(groups.isEmpty()) {
            log.error("group not found");
        } else {
            user.setGroupId(groups.get().getId());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        AppUser user = userRepo.findByLogin(login);
        if(user == null) {
            log.error("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        rolesRepo.findAllById(user.getRoleId()).forEach(role-> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(user.getLogin(), user.getPassHash(), authorities);
    }
}

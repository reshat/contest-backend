package com.group.contestback.services;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Groups;
import com.group.contestback.models.Mails;
import com.group.contestback.models.Roles;
import com.group.contestback.repositories.AppUserRepo;
import com.group.contestback.repositories.GroupsRepo;
import com.group.contestback.repositories.RolesRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;

import static org.hibernate.criterion.Restrictions.like;

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
        log.info("Save app user" + user.getPassHash());
        user.setPassHash(passwordEncoder.encode(user.getPassHash()));
        log.info("Registering new user" + user.getLogin() + " " + user.getEmail());
        Mails mails = new Mails(user.getEmail(),"Вы были успешно зарегистрированы",new Date());
        emailServiceCS.sendSimpleMessage(mails);
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
    public Page<AppUser> getUsersPage(int page, int pageSize) {
        return userRepo.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<AppUser> findUsersByLastNamePage(int page, int pageSize, String str) {
        return userRepo.nameSearch('%' + str + '%', PageRequest.of(page,pageSize));
    }

    @Override
    public void addEmailToUser(String login, String email) {
        AppUser user = userRepo.findByLogin(login);
        Mails mail1 = new Mails(user.getEmail(), "Изменение почты\",\"Ваша почта была изменена на " + email, new Date());
        emailServiceCS.sendSimpleMessage(mail1);
        user.setEmail(email);
        Mails mail2 = new Mails(user.getEmail(), "Изменение почты\",\"Ваша почта была изменена на " + email, new Date());
        emailServiceCS.sendSimpleMessage(mail2);
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
@Data
class UserRegistration {
    private String firstName;
    private String middleName;
    private String lastName;
    private String login;
    private String password;
    private String email;
}

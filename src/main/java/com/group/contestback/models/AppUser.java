package com.group.contestback.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class AppUser {
    @Id
    private Integer id;
    @Column(name="firstname")
    private String firstName;
    @Column(name="lastname")
    private String lastName;
    @Column(name="middlename")
    private String middleName;
    private String login;
    @Column(name="passhash")
    private String passHash;
    @Column(name="passsalt")
    private String passSalt;
    private String email;
    @Column(name="roleid")
    private Integer roleId;
    @Column(name="groupid")
    private Integer groupId;
}

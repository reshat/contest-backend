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
public class AppUser {
    @Id
    private Integer id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String login;
    private String passHash;
    private String passSalt;
    private String email;
    private Integer roleId;
}

package com.hc.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by hexi on 16-10-3.
 */
@Entity
@Table(name = "role")
public class Role implements Serializable, GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "name",nullable = false)
    private String name;

    @ManyToMany(targetEntity = User.class, mappedBy = "roles")
    private Set<User> users;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Role() {

    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}

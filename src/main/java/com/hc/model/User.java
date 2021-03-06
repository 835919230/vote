package com.hc.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Created by hexi on 16-10-3.
 */
@Entity
@Table(name = "user")
public class User implements Serializable, UserDetails, Authentication {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private String nickname;
    private Date createTime;
    private Date lastLoginTime;
    private String lastLoginIp;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns =
            {@JoinColumn(name = "user_id", referencedColumnName = "id" )},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @OneToMany(targetEntity = Vote.class,cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Vote> votes;

    public User() {

    }

    public long getId() {
        return id;
    }

    @Column(name = "username",nullable = false,unique = true)
    public String getUsername() {
        return username;
    }


    @Column(name = "password",nullable = false)
    public String getPassword() {
        return password;
    }

    @Column(name = "nickname",nullable = false)
    public String getNickname() {
        return nickname;
    }

    @Column(name = "create_time", nullable = false)
    public Date getCreateTime() {
        return createTime;
    }

    @Column(name = "last_login_time", nullable = false)
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    @Column(name = "last_login_ip",nullable = false)
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                '}';
    }

    // UserDetail properties
    @Override
    public String getName() {
        return this.nickname;
    }

    @Override
    public boolean implies(Subject subject) {
        if (subject == null)
            return false;
        return subject.getPrincipals().contains(this.username);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    //Authentication properties
    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return lastLoginIp;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

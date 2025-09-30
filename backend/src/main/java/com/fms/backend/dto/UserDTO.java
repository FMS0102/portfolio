package com.fms.backend.dto;

import com.fms.backend.models.Role;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String email;
    private String password;
    private Set<Role> roles;

    public UserDTO() {
    }

    public UserDTO(UUID id, String name, String email, String password, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDto)) return false;
        return Objects.equals(getId(), userDto.getId()) && Objects.equals(getName(), userDto.getName()) && Objects.equals(getEmail(), userDto.getEmail()) && Objects.equals(getPassword(), userDto.getPassword()) && Objects.equals(getRoles(), userDto.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getPassword(), getRoles());
    }
}

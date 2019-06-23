package akeene.resources.requests;

import akeene.resources.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

public class UpdateUserRequest {

    private UUID id;
    private String email;
    private String displayName;
    private String password;
    private User.Role role;

    public UpdateUserRequest(UUID id, String email, String displayName, String password, User.Role role) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.role = role;
    }

    public UpdateUserRequest() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}

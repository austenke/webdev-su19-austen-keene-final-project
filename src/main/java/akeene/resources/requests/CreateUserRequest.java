package akeene.resources.requests;

import akeene.resources.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class CreateUserRequest {

    @Email(regexp = ".+@.+\\..+")
    private String email;

    @NotEmpty
    @Length(min = 3, max = 20)
    private String displayName;

    @NotEmpty
    //@Length(min = PasswordManagementConfiguration.MIN_LENGTH, max = PasswordManagementConfiguration.MAX_LENGTH)
    private String password;

    private User.Role role;

    public CreateUserRequest(String email, String displayName, String password, User.Role role) {
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.role = role;
    }

    public CreateUserRequest() {

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

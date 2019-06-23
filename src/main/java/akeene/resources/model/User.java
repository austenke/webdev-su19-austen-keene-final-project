package akeene.resources.model;

import akeene.auth.PasswordDigest;
import akeene.jdbc.model.DBUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import akeene.auth.Roles;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.builder.Builder;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.*;


@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public abstract class User implements Principal {

    public enum Role {
        CURATOR, LISTENER
    }

    @NotEmpty
    public abstract UUID getId();

    @NotEmpty
    @Email(regexp = ".+@.+\\..+")
    public abstract String getEmail();

    @NotEmpty
    @Length(min = 3, max = 20)
    public abstract String getDisplayName();

    @NotEmpty
    public abstract PasswordDigest getPasswordDigest();

    @NotEmpty
    public abstract Role getRole();
}

package akeene.jdbc.dao.helpers;

import akeene.auth.PasswordDigest;
import akeene.resources.model.ImmutableUser;
import akeene.resources.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserMapper implements ResultSetMapper<User> {

    @Override
    public User map(final int index, final ResultSet r, final StatementContext ctx) throws SQLException {
        final UUID id = UUID.fromString(r.getString("id"));
        final String email = r.getString("email");
        final String displayName = r.getString("displayName");
        final PasswordDigest passwordDigest = PasswordDigest.fromDigest(r.getString("passwordDigest"));
        final User.Role role = User.Role.valueOf(r.getString("role"));

        return ImmutableUser.builder()
                .id(id)
                .name(email)
                .email(email)
                .displayName(displayName)
                .role(role)
                .passwordDigest(passwordDigest)
                .build();
    }
}

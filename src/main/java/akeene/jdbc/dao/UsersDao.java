package akeene.jdbc.dao;

import akeene.auth.PasswordDigest;
import akeene.jdbc.PasswordDigestArgumentFactory;
import akeene.jdbc.UUIDArgumentFactory;
import akeene.jdbc.dao.helpers.UserMapper;
import akeene.resources.model.User;
import com.google.inject.Singleton;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterArgumentFactory;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RegisterMapper(UserMapper.class)
@RegisterArgumentFactory({UUIDArgumentFactory.class, PasswordDigestArgumentFactory.class})
public abstract class UsersDao {

    @SqlQuery("select * from users")
    public abstract List<User> getAllUsers();

    @SingleValueResult
    @SqlQuery("select * from users where users.id=:id")
    public abstract Optional<User> getUser(@Bind("id") UUID id);

    @SingleValueResult
    @SqlQuery("select * from users where users.email=:email")
    public abstract Optional<User> getUserWithEmail(@Bind("email") String email);

    @SqlUpdate("insert into users (id, email, displayName, passwordDigest, role) values "
               + "(:id, :email, :displayName, :passwordDigest, :role)")
    public abstract void insertUser(@BindBean User user);

    @SqlUpdate("update users set email=:email, displayName=:displayName, " +
            "passwordDigest=:passwordDigest, role=:role where id=:id")
    public abstract void updateUser(@Bind("id") UUID id,
                                    @Bind("email") String email,
                                    @Bind("displayName") String displayName,
                                    @Bind("passwordDigest") PasswordDigest passwordDigest,
                                    @Bind("role") User.Role role);

    @SqlUpdate("delete from users where id=:id")
    public abstract int deleteUser(@Bind("id") UUID id);
}

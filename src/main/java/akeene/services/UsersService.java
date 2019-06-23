package akeene.services;

import akeene.jdbc.dao.ActionsDao;
import akeene.resources.model.ImmutableProfile;
import akeene.resources.model.ImmutableUser;
import akeene.resources.model.Profile;
import akeene.resources.requests.CreateUserRequest;
import akeene.resources.requests.UpdateUserRequest;
import akeene.services.model.LoginResult;
import akeene.auth.JwtTokenManager;
import akeene.auth.PasswordDigest;
import akeene.auth.PasswordManagementConfiguration;
import akeene.errors.Exceptions;
import akeene.jdbc.dao.UsersDao;
import akeene.resources.model.User;
import com.google.inject.Singleton;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;

@Singleton
public class UsersService {
    public static final String USER_CREATION_ERROR = "Error creating user";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String INCORRECT_PASSWORD = "Incorrect password";

    private final DBI dbi;
    private final JwtTokenManager jwtTokenManager;
    private final PasswordManagementConfiguration passwordManagementConfiguration;

    @Inject
    public UsersService(final DBI dbi, final JwtTokenManager jwtTokenManager,
                        final PasswordManagementConfiguration passwordManagementConfiguration) {
        this.dbi = dbi;
        this.jwtTokenManager = jwtTokenManager;
        this.passwordManagementConfiguration = passwordManagementConfiguration;
    }

    public List<User> getAllUsers() {
        try (final Handle handle = dbi.open()) {
            final UsersDao usersDao = handle.attach(UsersDao.class);
            return usersDao.getAllUsers();
        } catch (Error e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    public User getUser(final UUID userId) {
        try (final Handle handle = dbi.open()) {
            System.out.println("Trying to get user with id: " + userId);
            final UsersDao usersDao = handle.attach(UsersDao.class);
            return usersDao.getUser(userId)
                    .orElseThrow(() -> Exceptions.webAppException(USER_NOT_FOUND, Response.Status.NOT_FOUND));
        }
    }

    public Profile getProfile(final UUID userId) {
        try (final Handle handle = dbi.open()) {
            System.out.println("Trying to get user profile with id: " + userId);
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);

            User user = getUser(userId);

            return ImmutableProfile.builder()
                    .id(userId)
                    .displayName(user.getDisplayName())
                    .followers(actionsDao.getFollowerCount(userId))
                    .follows(actionsDao.getFollowCount(userId))
                    .build();
        }
    }

    public User updateUser(User user, UpdateUserRequest updateUserRequest) {
        if (!user.getId().equals(updateUserRequest.getId())) {
            throw Exceptions.webAppException("You cannot modify this user", Response.Status.BAD_REQUEST);
        }

        try (final Handle handle = dbi.open()) {
            final UsersDao usersDao = handle.attach(UsersDao.class);

            String email = updateUserRequest.getEmail().equals(user.getEmail())?
                    user.getEmail() : updateUserRequest.getEmail();

            String displayName = updateUserRequest.getDisplayName().equals(user.getDisplayName())?
                    user.getDisplayName() : updateUserRequest.getDisplayName();

            PasswordDigest password;

            if (!updateUserRequest.getPassword().equals("")) {
                PasswordDigest newPass = PasswordDigest.generateFromPassword(
                        passwordManagementConfiguration.getBcryptCost(), updateUserRequest.getPassword());
                password = newPass.equals(user.getPasswordDigest())?
                        user.getPasswordDigest() : newPass;
            } else {
                password = user.getPasswordDigest();
            }

            User.Role role = updateUserRequest.getRole().equals(user.getRole())?
                    user.getRole() : updateUserRequest.getRole();

            usersDao.updateUser(user.getId(), email, displayName, password, role);

            return usersDao.getUser(user.getId()).orElseThrow(() ->
                    Exceptions.webAppException(USER_CREATION_ERROR, Response.Status.NOT_FOUND));
        }
    }

    public User createUser(CreateUserRequest createUserRequest) {
        try (final Handle handle = dbi.open()) {
            final UsersDao usersDao = handle.attach(UsersDao.class);

             if (usersDao.getUserWithEmail(createUserRequest.getEmail()).isPresent())
                 throw Exceptions.webAppException(USER_ALREADY_EXISTS, Response.Status.BAD_REQUEST);

            final User newUser = ImmutableUser.builder()
                    .id(UUID.randomUUID())
                    .name(createUserRequest.getEmail())
                    .email(createUserRequest.getEmail())
                    .displayName(createUserRequest.getDisplayName())
                    .role(createUserRequest.getRole())
                    .passwordDigest(PasswordDigest.generateFromPassword(
                            passwordManagementConfiguration.getBcryptCost(), createUserRequest.getPassword()))
                    .build();

            usersDao.insertUser(newUser);

            return usersDao.getUser(newUser.getId()).orElseThrow(() ->
                    Exceptions.webAppException(USER_CREATION_ERROR, Response.Status.NOT_FOUND));
        }
    }

    public String refreshJWT(final User user) {
        return generateToken(user);
    }

    public Optional<LoginResult> loginUser(final String email, final String password) {
        try (final Handle handle = dbi.open()) {
            final UsersDao usersDao = handle.attach(UsersDao.class);

            final Optional<User> maybeUser = usersDao.getUserWithEmail(email);

            System.out.println("User is present: " + maybeUser.isPresent());

            if (maybeUser.isPresent()) {
                System.out.println("User is: " + maybeUser.get());
                final User user = maybeUser.get();
                if (user.getPasswordDigest().checkPassword(password)) {
                    final String token = generateToken(user);
                    return Optional.of(new LoginResult(user.getId(), token));
                }
            }
            return Optional.empty();
        }
    }

    private String generateToken(final User user) {
        final Map<String, Object> claim = jwtTokenManager.toJWTClaim(user);
        return jwtTokenManager.getJwtSigner().sign(claim, jwtTokenManager.getJwtOptions());
    }

}

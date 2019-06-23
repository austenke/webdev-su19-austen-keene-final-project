package akeene.resources;

import akeene.auth.JWTConfiguration;
import akeene.errors.Exceptions;
import akeene.resources.model.Profile;
import akeene.resources.model.User;
import akeene.resources.requests.CreateUserRequest;
import akeene.resources.requests.LoginRequest;
import akeene.resources.requests.UpdateUserRequest;
import akeene.services.model.LoginResult;
import akeene.services.UsersService;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);

    private final UsersService usersService;
    private final JWTConfiguration jwtConfiguration;

    @Inject
    public UsersResource(final UsersService usersService, final JWTConfiguration jwtConfiguration) {
        this.usersService = usersService;
        this.jwtConfiguration = jwtConfiguration;
    }

    @GET
    public User dumpUser(@Auth final User principal) {
        return principal;
    }

    @GET
    @Path("/{userId}")
    public User getUser(@PathParam("userId") final UUID userId) {
        System.out.println("Looking up ID: " + userId);
        return usersService.getUser(userId);
    }

    @GET
    @Path("/profile/{userId}")
    public Profile getProfile(@PathParam("userId") final UUID userId) {
        return usersService.getProfile(userId);
    }

    @GET
    @Path("/refreshJWT")
    public Response refreshJWT(@Auth final User principal) {
        final String token = usersService.refreshJWT(principal);
        return Response
                .ok(usersService.getUser(principal.getId()))
                .cookie(buildCookie(jwtConfiguration.getCookieName(), token))
                .build();
    }

    private NewCookie buildCookie(String name, String token) {
        return new NewCookie(name, token,"/", "", "", 1000000, false);
    }

    @POST
    @Path("/createUser")
    public User createUser(@Valid final CreateUserRequest request) {
        return usersService.createUser(request);
    }

    @PUT
    @Path("/updateUser")
    public User updateUser(@Auth final User principal, final UpdateUserRequest request) {
        return usersService.updateUser(principal, request);
    }

    @POST
    @Path("/login")
    public Response loginUser(@Valid final LoginRequest loginRequest) {
        System.out.println("Got login request: " + loginRequest);
        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();
        final Optional<LoginResult> maybeResult = usersService.loginUser(email, password);

        if (maybeResult.isPresent()) {
            final LoginResult result = maybeResult.get();
            return Response
                    .ok(usersService.getUser(result.getUserId()))
                    .cookie(buildCookie(jwtConfiguration.getCookieName(), result.getToken()))
                    .build();
        } else {
            throw Exceptions.webAppException(UsersService.USER_NOT_FOUND, Response.Status.NOT_FOUND);
        }
    }
}
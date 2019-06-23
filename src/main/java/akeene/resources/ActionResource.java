package akeene.resources;

import akeene.resources.model.User;
import akeene.services.ActionService;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/actions")
@Produces(MediaType.APPLICATION_JSON)
public class ActionResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);

    private final ActionService actionService;

    @Inject
    public ActionResource(final ActionService actionService) {
        this.actionService = actionService;
    }

    @GET
    @Path("/search/{query}")
    public String search(@PathParam("query") final String query) {
        return actionService.searchPlaylists(query);
    }

    @GET
    @Path("/follows")
    public List<UUID> getFollows(@Auth final User user) {
        return actionService.getFollows(user);
    }

    @GET
    @Path("/followers")
    public List<UUID> getFollowers(@Auth final User user) {
        return actionService.getFollowers(user);
    }

    @GET
    @Path("/playlists")
    public List<String> getPlaylists(@Auth final User user) {
        return actionService.getPlaylists(user);
    }

    @POST
    @Path("/follow/{userId}")
    public Response followUser(@Auth final User user, @PathParam("userId") final UUID userId) {
        actionService.followUser(user, userId);
        return Response.ok().build();
    }

    @POST
    @Path("/unfollow/{userId}")
    public Response unfollowUser(@Auth final User user, @PathParam("userId") final UUID userId) {
        actionService.unfollowUser(user, userId);
        return Response.ok().build();
    }

    @POST
    @Path("/playlists/{playlistId}")
    public Response addPlaylist(@Auth final User user, @PathParam("playlistId") final String playlistId) {
        actionService.addPlaylist(user, playlistId);
        return Response.ok().build();
    }

    @POST
    @Path("/playlists/{playlistId}/delete")
    public Response deletePlaylist(@Auth final User user, @PathParam("playlistId") final String playlistId) {
        actionService.deletePlaylist(user, playlistId);
        return Response.ok().build();
    }
}

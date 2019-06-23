package akeene.services;

import akeene.jdbc.dao.ActionsDao;
import akeene.resources.model.User;
import com.google.inject.Singleton;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Singleton
public class ActionService {

    private final DBI dbi;
    private final SpotifyApi spotifyApi;
    private long expiration;

    @Inject
    public ActionService(final DBI dbi, final SpotifyApi spotifyApi) {
        this.dbi = dbi;
        this.spotifyApi = spotifyApi;
        this.expiration = authorize();
    }

    private long authorize() {
        try {
            ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return System.currentTimeMillis() + (clientCredentials.getExpiresIn() * 1000);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String searchPlaylists(String query) {
        try {
            if ((expiration - 2000) < System.currentTimeMillis()) this.expiration = authorize();

            return spotifyApi.searchPlaylists(query).limit(18).build().getJson();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public List<UUID> getFollows(User user) {
        try (final Handle handle = dbi.open()) {
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);
            return actionsDao.getFollows(user.getId());
        }
    }

    public List<UUID> getFollowers(User user) {
        try (final Handle handle = dbi.open()) {
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);
            return actionsDao.getFollowers(user.getId());
        }
    }

    public List<String> getPlaylists(User user) {
        try (final Handle handle = dbi.open()) {
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);
            return actionsDao.getPlaylists(user.getId());
        }
    }

    public void followUser(User follower, UUID followedId) {
        try (final Handle handle = dbi.open()) {
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);
            actionsDao.followUser(follower.getId(), followedId);
        }
    }

    public void unfollowUser(User follower, UUID followedId) {
        try (final Handle handle = dbi.open()) {
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);
            actionsDao.unfollowUser(follower.getId(), followedId);
        }
    }

    public void addPlaylist(User user, String playlistId) {
        try (final Handle handle = dbi.open()) {
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);
            actionsDao.addPlaylist(user.getId(), playlistId);
        }
    }

    public void deletePlaylist(User user, String playlistId) {
        try (final Handle handle = dbi.open()) {
            final ActionsDao actionsDao = handle.attach(ActionsDao.class);
            actionsDao.deletePlaylist(user.getId(), playlistId);
        }
    }
}

package akeene.jdbc.dao;

import akeene.jdbc.UUIDArgumentFactory;
import akeene.jdbc.dao.helpers.UUIDMapper;
import com.google.inject.Singleton;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterArgumentFactory;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.UUID;

@Singleton
@RegisterMapper(UUIDMapper.class)
@RegisterArgumentFactory({UUIDArgumentFactory.class})
public abstract class ActionsDao {

    @SqlQuery("select followedId from follows where followerId=:userId")
    public abstract List<UUID> getFollows(@Bind("userId") UUID userId);

    @SqlQuery("select followerId from follows where followedId=:userId")
    public abstract List<UUID> getFollowers(@Bind("userId") UUID userId);

    @SqlQuery("select count(*) from follows where followerId=:userId")
    public abstract int getFollowCount(@Bind("userId") UUID userId);

    @SqlQuery("select count(*) from follows where followedId=:userId")
    public abstract int getFollowerCount(@Bind("userId") UUID userId);

    @SqlQuery("select playlistId from playlists where userId=:userId")
    public abstract List<String> getPlaylists(@Bind("userId") UUID userId);

    @SqlUpdate("insert into follows (followerId, followedId) values (:followerId, :followedId)")
    public abstract void followUser(@Bind("followerId") UUID followerId, @Bind("followedId") UUID followedId);

    @SqlUpdate("delete from follows where followerId=:followerId and followedId=:followedId")
    public abstract void unfollowUser(@Bind("followerId") UUID followerId, @Bind("followedId") UUID followedId);

    @SqlUpdate("insert into playlists (userId, playlistId) values (:userId, :playlistId)")
    public abstract void addPlaylist(@Bind("userId") UUID userId, @Bind("playlistId") String playlistId);

    @SqlUpdate("delete from playlists where playlistId=:playlistId and userId=:userId")
    public abstract void deletePlaylist(@Bind("userId") UUID userId, @Bind("playlistId") String playlistId);
}

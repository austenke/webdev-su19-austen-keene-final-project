package akeene.resources.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.*;


@Value.Immutable
@JsonSerialize(as = ImmutableProfile.class)
@JsonDeserialize(as = ImmutableProfile.class)
public abstract class Profile {

    public abstract UUID getId();
    public abstract String getDisplayName();
    public abstract int getFollowers();
    public abstract int getFollows();
}

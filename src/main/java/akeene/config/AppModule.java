package akeene.config;

import akeene.AppConfiguration;
import akeene.auth.JWTConfiguration;
import akeene.auth.JwtTokenManager;
import akeene.auth.PasswordManagementConfiguration;
import akeene.resources.ActionResource;
import akeene.resources.UsersResource;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.hubspot.dropwizard.guicier.DropwizardAwareModule;
import com.wrapper.spotify.SpotifyApi;
import io.dropwizard.jdbi.DBIFactory;
import org.skife.jdbi.v2.DBI;

public class AppModule extends DropwizardAwareModule<AppConfiguration> {

    @Override
    public void configure(Binder binder) {
        binder.bind(UsersResource.class);
        binder.bind(ActionResource.class);
    }

    @Singleton
    @Provides
    public SpotifyApi providesSpotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId("9374a4dd0b204fa894f8c46b8793007f")
                .setClientSecret("1e09154adb2d40b2b5fd9da453718db2")
                .build();
    }

    @Singleton
    @Provides
    public PasswordManagementConfiguration providesPasswordManagementConfiguration() {
        return getConfiguration().getPasswordManagement();
    }

    @Singleton
    @Provides
    public JWTConfiguration getJWTConfiguration() {
        return getConfiguration().getJwt();
    }

    @Singleton
    @Provides
    public JwtTokenManager providesJwtTokenManager(JWTConfiguration jwtConfiguration) {
        return jwtConfiguration.buildTokenManager();
    }

    @Singleton
    @Provides
    public DBI providesDBI() {
        final DBIFactory factory = new DBIFactory();
        return factory.build(getEnvironment(), getConfiguration().getDatabase(), "webdev");
    }
}

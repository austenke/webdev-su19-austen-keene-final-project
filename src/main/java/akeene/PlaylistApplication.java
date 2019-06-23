package akeene;

import akeene.config.AppModule;
import akeene.resources.model.User;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guicier.GuiceBundle;
import com.palantir.indexpage.IndexPageBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class PlaylistApplication extends Application<AppConfiguration> {

    public static void main(String... args) throws Exception {
        new PlaylistApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        GuiceBundle<AppConfiguration> guiceBundle = GuiceBundle.defaultBuilder(AppConfiguration.class)
                .modules(new AppModule())
                .stage(Stage.PRODUCTION)
                .enableGuiceEnforcer(false)
                .build();

        bootstrap.addBundle(guiceBundle);

        // no index file parameter because index is served by IndexPageBundle
        bootstrap.addBundle(new IndexPageBundle(ImmutableSet.of("/", "/login", "/register", "/profile", "/settings")));
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));


        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.addBundle(new MigrationsBundle<AppConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(final AppConfiguration configuration) {
                return configuration.getDatabase();
            }
        });
    }

    @Override
    public void run(AppConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new AuthDynamicFeature(configuration.getJwt().buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        //Required to use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
package akeene;

import akeene.auth.JWTConfiguration;
import akeene.auth.PasswordManagementConfiguration;
import com.palantir.indexpage.IndexPageConfigurable;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppConfiguration extends Configuration implements IndexPageConfigurable {
    @Valid
    @NotNull
    private JWTConfiguration jwt = new JWTConfiguration();

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private PasswordManagementConfiguration passwordManagement = new PasswordManagementConfiguration();

    @Valid
    @NotNull
    private String indexPagePath = "";

    @JsonProperty
    public JWTConfiguration getJwt() {
        return jwt;
    }

    @JsonProperty
    public void setJwt(final JWTConfiguration jwt) {
        this.jwt = jwt;
    }

    @JsonProperty
    public DataSourceFactory getDatabase() {
        return database;
    }

    @JsonProperty
    public void setDatabase(final DataSourceFactory database) {
        this.database = database;
    }

    @JsonProperty
    public PasswordManagementConfiguration getPasswordManagement() {
        return passwordManagement;
    }

    @JsonProperty
    public void setPasswordManagement(final PasswordManagementConfiguration passwordManagement) {
        this.passwordManagement = passwordManagement;
    }

    @JsonProperty
    @Override
    public String getIndexPagePath() {
        return indexPagePath;
    }

    @JsonProperty
    public void setIndexPagePath(String indexPagePath) {
        this.indexPagePath = indexPagePath;
    }
}

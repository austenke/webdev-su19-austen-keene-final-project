package akeene.auth;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import akeene.resources.model.User;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import java.util.Map;

public class JwtTokenManager {

    private final JWTSigner jwtSigner;
    private final JWTSigner.Options jwtOptions;
    private final JWTVerifier jwtVerifier;
    private final ObjectMapper jsonMapper;

    JwtTokenManager(final String jwtSecret, final int jwtExpirySeconds) {
        jwtSigner = new JWTSigner(jwtSecret);
        jwtVerifier = new JWTVerifier(jwtSecret);
        jwtOptions = new JWTSigner.Options().setExpirySeconds(jwtExpirySeconds);

        jsonMapper = new ObjectMapper().registerModule(new GuavaModule());
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JWTSigner getJwtSigner() {
        return jwtSigner;
    }

    public JWTSigner.Options getJwtOptions() {
        return jwtOptions;
    }

    JWTVerifier getJwtVerifier() {
        return jwtVerifier;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> toJWTClaim(final User user) {
        try {
            return jsonMapper.convertValue(user, Map.class);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException("Can't JSONify User, something's badly wrong", e);
        }
    }

    User fromJWTClaim(final Map<String, Object> claim)
            throws IllegalArgumentException {
        System.out.println(claim);
        final User user = jsonMapper.convertValue(claim, User.class);
        if (user.getId() == null) {
            throw new IllegalStateException("User from claim is missing id: " + claim);
        }
        if (user.getRole() == null) {
            throw new IllegalStateException("User from claim is missing roles: " + claim);
        }
        if (user.getEmail() == null) {
            throw new IllegalStateException("User from claim is missing email: " + claim);
        }
        if (user.getDisplayName() == null) {
            throw new IllegalStateException("User from claim is missing displayName: " + claim);
        }
        if (user.getPasswordDigest() == null) {
            throw new IllegalStateException("User from claim is missing password: " + claim);
        }
        return user;
    }
}

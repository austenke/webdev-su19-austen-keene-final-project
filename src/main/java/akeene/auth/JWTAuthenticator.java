package akeene.auth;

import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTVerifyException;
import akeene.resources.model.User;
import com.google.common.base.Preconditions;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Cookie;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.Optional;

public class JWTAuthenticator implements Authenticator<Cookie, User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticator.class);

    private final JwtTokenManager jwtTokenManager;

    @Inject
    public JWTAuthenticator(final JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public Optional<User> authenticate(final Cookie cookie) throws AuthenticationException {
        final String token = cookie.getValue();
        final Optional<Map<String, Object>> maybeClaims = getClaims(token);

        try {
            if (!maybeClaims.isPresent()) return Optional.empty();

            return Optional.of(jwtTokenManager.fromJWTClaim(maybeClaims.get()));
        } catch (final IllegalArgumentException e) {
            LOGGER.warn("Unable to read User from JWT claim", e);
            return Optional.empty();
        }
    }

    private Optional<Map<String, Object>> getClaims(final String token) throws AuthenticationException {
        try {
            return Optional.of(jwtTokenManager.getJwtVerifier().verify(token));
        } catch (final JWTExpiredException e) {
            LOGGER.warn("Expired token '" + token + "'", e);
        } catch (final SignatureException e) {
            LOGGER.warn("Unable to verify token '" + token + "'", e);
        } catch (final NoSuchAlgorithmException | InvalidKeyException | IOException | JWTVerifyException e) {
            throw new AuthenticationException("Unable to verify claims in JWT", e);
        }
        return Optional.empty();
    }
}
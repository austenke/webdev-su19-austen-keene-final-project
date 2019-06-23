package akeene.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordDigest {
    private final String digest;

    public PasswordDigest(@JsonProperty("digest") final String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public boolean checkPassword(final String passwordToCheck) {
        return BCrypt.checkpw(passwordToCheck, digest);
    }

    public static PasswordDigest fromDigest(final String digest) {
        return new PasswordDigest(digest);
    }

    public static PasswordDigest generateFromPassword(final int bcryptCost, final String password) {
        final String salt = BCrypt.gensalt(bcryptCost);
        return PasswordDigest.fromDigest(BCrypt.hashpw(password, salt));
    }
}

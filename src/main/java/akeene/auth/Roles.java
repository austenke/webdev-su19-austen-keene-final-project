package akeene.auth;

import akeene.resources.model.User;

import java.util.UUID;

public enum Roles {
    USER {
        @Override
        protected boolean canReadUser(final User principal, final UUID userId) {
            return userId.equals(principal.getId());
        }

        @Override
        protected boolean canWriteUser(final User principal, final UUID userId) {
            return userId.equals(principal.getId());
        }
    },
    ADMIN {
        @Override
        protected boolean canReadUser(final User principal, final UUID userId) {
            return true;
        }

        @Override
        protected boolean canWriteUser(final User principal, final UUID userId) {
            return true;
        }
    };

    public String getName() {
        return name();
    }

    public boolean canRead(final Class<?> resourceClass, final User principal, final UUID resourceId) {
        return resourceClass == User.class && canReadUser(principal, resourceId);
    }

    protected abstract boolean canReadUser(User principal, UUID userId);

    public boolean canWrite(final Class<?> resourceClass, final User principal, final UUID resourceId) {
        return resourceClass == User.class && canWriteUser(principal, resourceId);
    }

    protected abstract boolean canWriteUser(User principal, UUID userId);
}

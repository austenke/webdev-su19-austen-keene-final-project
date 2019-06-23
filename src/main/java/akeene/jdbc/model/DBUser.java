package akeene.jdbc.model;

import akeene.auth.PasswordDigest;
import akeene.auth.Roles;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DBUser {
    private UUID id;
    private Date createdDate;
    private Date modifiedDate;
    private Date deletedDate;
    private Date lastLoginDate;
    private Date lastPasswordChangeDate;
    private Date emailValidatedDate;
    private String email;
    private String displayName;
    private PasswordDigest passwordDigest;
    private Set<Roles> roles;
    private UUID squad;
    private UUID duo;
    private boolean emailVerified;
    private String botId;
    private String accountId;

    public DBUser(UUID id, Date createdDate, Date modifiedDate, Date deletedDate, Date lastLoginDate, Date lastPasswordChangeDate, Date emailValidatedDate, String email, String displayName, PasswordDigest passwordDigest, Set<Roles> roles, UUID squad, UUID duo, boolean emailVerified, String botId, String accountId) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.deletedDate = deletedDate;
        this.lastLoginDate = lastLoginDate;
        this.lastPasswordChangeDate = lastPasswordChangeDate;
        this.emailValidatedDate = emailValidatedDate;
        this.email = email;
        this.displayName = displayName;
        this.passwordDigest = passwordDigest;
        this.roles = roles;
        this.squad = squad;
        this.duo = duo;
        this.emailVerified = emailVerified;
        this.botId = botId;
        this.accountId = accountId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Date getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }

    public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
        this.lastPasswordChangeDate = lastPasswordChangeDate;
    }

    public Date getEmailValidatedDate() {
        return emailValidatedDate;
    }

    public void setEmailValidatedDate(Date emailValidatedDate) {
        this.emailValidatedDate = emailValidatedDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public PasswordDigest getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(PasswordDigest passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public UUID getSquad() {
        return squad;
    }

    public void setSquad(UUID squad) {
        this.squad = squad;
    }

    public UUID getDuo() {
        return duo;
    }

    public void setDuo(UUID duo) {
        this.duo = duo;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    private static Date clone(final Date date) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime());
    }

}

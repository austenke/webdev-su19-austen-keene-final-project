package akeene.jdbc.dao.helpers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDMapper implements ResultSetMapper<UUID> {

    @Override
    public UUID map(final int index, final ResultSet r, final StatementContext ctx) throws SQLException {
        return UUID.fromString(r.getString(1));
    }
}

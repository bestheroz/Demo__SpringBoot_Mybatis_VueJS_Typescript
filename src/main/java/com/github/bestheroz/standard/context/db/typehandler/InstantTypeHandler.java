package com.github.bestheroz.standard.context.db.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.time.Instant;

@MappedTypes(Instant.class)
@MappedJdbcTypes(value = {JdbcType.TIME, JdbcType.TIMESTAMP, JdbcType.DATE})
public class InstantTypeHandler implements TypeHandler<Instant> {

    @Override
    public Instant getResult(final ResultSet rs, final String columnName) throws SQLException {
        final Timestamp ts = rs.getTimestamp(columnName);
        return ts == null ? null : Instant.ofEpochMilli(ts.getTime());
    }

    @Override
    public Instant getResult(final ResultSet rs, final int columnIndex) throws SQLException {
        final Timestamp ts = rs.getTimestamp(columnIndex);
        return ts == null ? null : Instant.ofEpochMilli(ts.getTime());
    }

    @Override
    public Instant getResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        final Timestamp ts = cs.getTimestamp(columnIndex);
        return ts == null ? null : Instant.ofEpochMilli(ts.getTime());
    }

    @Override
    public void setParameter(final PreparedStatement ps, final int i, final Instant parameter, final JdbcType arg3) throws SQLException {
        ps.setTimestamp(i, parameter == null ? null : new Timestamp(parameter.toEpochMilli()));
    }

}

package com.github.bestheroz.standard.context.db.typehandler;

import com.github.bestheroz.standard.common.util.EscapeUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(value = {String.class})
@MappedJdbcTypes(value = {JdbcType.CHAR, JdbcType.NCHAR, JdbcType.VARCHAR, JdbcType.NVARCHAR, JdbcType.LONGVARCHAR, JdbcType.LONGNVARCHAR, JdbcType.CLOB, JdbcType.NCLOB})
public class StringTypeHandler implements TypeHandler<String> {
    @Override
    public String getResult(final ResultSet rs, final String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }

    @Override
    public void setParameter(final PreparedStatement ps, final int i, final String parameter, final JdbcType arg3) throws SQLException {
        ps.setString(i, EscapeUtils.escapeAll(parameter));
    }

}

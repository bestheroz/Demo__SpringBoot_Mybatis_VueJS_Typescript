package com.github.bestheroz.standard.common.mybatis.typehandler;

import com.github.bestheroz.standard.common.util.MapperUtils;
import com.google.gson.JsonParser;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@Slf4j
@MappedTypes(value = {List.class})
public abstract class AbstractListTypeHandler<T> extends BaseTypeHandler<T> {
  protected Type getType() {
    // This class should be able to determine the correct
    // type from the superclass using reflection. If it can't,
    // override this method in the superclass and provide the
    // correct TypeToken. For example:
    // return new TypeToken<List<String>>(){}.getType();
    final ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    return pt.getActualTypeArguments()[0];
  }

  private T fromJson(final String json) {
    if (json == null) {
      return null;
    }
    // 이래야 하나 싶지만 더 좋은 방법을 찾을 수 있으려나..
    String result = json.replaceAll("\\\\", "");
    if (StringUtils.startsWith(result, "\"")) {
      result = result.substring(1);
    }
    if (StringUtils.endsWith(result, "\"")) {
      result = result.substring(0, result.length() - 1);
    }
    return MapperUtils.toObject(JsonParser.parseString(result), this.getType());
  }

  @Override
  public T getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
    return this.fromJson(rs.getString(columnName));
  }

  @Override
  public T getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return this.fromJson(rs.getString(columnIndex));
  }

  @Override
  public T getNullableResult(final CallableStatement cs, final int columnIndex)
      throws SQLException {
    return this.fromJson(cs.getString(columnIndex));
  }

  @Override
  public void setNonNullParameter(
      final PreparedStatement ps, final int i, final T parameter, final JdbcType arg3)
      throws SQLException {
    ps.setString(i, MapperUtils.toString(parameter));
  }
}

package it.geosolutions.swgeoserver.entry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//设置映射类型
@MappedTypes(Object.class)
public class JsonTypeHandler<T extends Object>  extends BaseTypeHandler<T> {
    private Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        if (clazz == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.clazz = clazz;
    }

    /**
     * 设置非空参数
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    /*@Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }*/

    private static final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        jsonObject.setType("json");
        jsonObject.setValue(parameter.toString());
        ps.setObject(i, jsonObject);
    }

    /**
     * 根据列名，获取可以为空的结果
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String sqlJson = rs.getString(columnName);
        if (null != sqlJson) {
            return JSONObject.parseObject(sqlJson, clazz);
        }
        return null;
    }

    /**
     * 根据列索引，获取可以为空的结果
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String sqlJson = rs.getString(columnIndex);
        if (null != sqlJson) {
            return JSONObject.parseObject(sqlJson, clazz);
        }
        return null;
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String sqlJson = cs.getString(columnIndex);
        if (null != sqlJson) {
            return JSONObject.parseObject(sqlJson, clazz);
        }
        return null;
    }

}
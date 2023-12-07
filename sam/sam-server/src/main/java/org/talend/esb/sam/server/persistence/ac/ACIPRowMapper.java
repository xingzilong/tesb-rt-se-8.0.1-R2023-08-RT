package org.talend.esb.sam.server.persistence.ac;

import com.google.gson.Gson;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库表 access_control_ip 对应的实体类
 *
 */
public class ACIPRowMapper implements RowMapper<ACIP> {

    private Gson gson = new Gson();

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public ACIP mapRow(ResultSet rs, int rowNum) throws SQLException {
        ACIP acIP = new ACIP();
        acIP.setId(rs.getString("id"));
        acIP.setServiceKey(rs.getString("service_key"));
        acIP.setType(rs.getString("type"));
//        acIP.setBlackList(gson.fromJson(rs.getString("black_list"), new TypeToken<HashMap<String, String>>() {
//        }.getType()));
//        acIP.setWhiteList(gson.fromJson(rs.getString("white_list"), new TypeToken<HashMap<String, String>>() {
//        }.getType()));
        acIP.setBlackList(rs.getString("black_list"));
        acIP.setWhiteList(rs.getString("white_list"));
        acIP.setStatus(rs.getString("status"));
        return acIP;
    }
}
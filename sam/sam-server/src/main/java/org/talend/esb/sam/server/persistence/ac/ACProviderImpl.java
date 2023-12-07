package org.talend.esb.sam.server.persistence.ac;

import org.talend.esb.sam.server.persistence.dialects.DatabaseDialect;
import org.talend.esb.sam.server.persistence.dialects.DialectFactory;
import com.google.gson.Gson;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 访问控制， Restful管理控制接口数据库服务提供者{@link org.talend.esb.sam.server.persistence.ac.ACProvider}接口实现类
 *
 */
public class ACProviderImpl extends JdbcDaoSupport implements ACProvider {

    private final RowMapper<ACIP> acIPRowMapper = new ACIPRowMapper();

    private Gson gson = new Gson();

    private String dialect;

    private DatabaseDialect dbDialect;

    private NamedParameterJdbcTemplate npJdbcTemplate = null;

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public void init() {
        DialectFactory dialectFactory = new DialectFactory(getDataSource());
        this.dbDialect = dialectFactory.getDialect(dialect);
        this.npJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
    }

    @Override
    public int saveACIP(ACIP acIP) {
        String sql = "INSERT INTO  " +
                "access_control_ip " +
                "(id, service_key, type, black_list, white_list, status, create_time, update_time) " +
                "VALUES " +
                "(:id, :serviceKey, :type, :blackList, :whiteList, :status, :createTime, :updateTime) ";
        Date date = new Date();
        acIP.setCreateTime(date);
        acIP.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acIP);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public int removeACIPByServiceKey(String serviceKey) {
        String sql = "DELETE FROM access_control_ip WHERE service_key = :serviceKey ";
        int rs = npJdbcTemplate.update(sql, Collections.singletonMap("serviceKey", serviceKey));
        return rs;
    }

    @Override
    public int updateACIP(ACIP acIP) {
        String sql = "UPDATE " +
                "access_control_ip " +
                "SET " +
                "type = :type, black_list = :blackList, white_list = :whiteList, update_time = :updateTime " +
                "WHERE " +
                "id = :id " +
                "AND " +
                "service_key = :serviceKey ";
        Date date = new Date();
        acIP.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acIP);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public ACIP getACIPByServiceKey(String serviceKey) {
        String sql = "SELECT " +
                "id, service_key, type, black_list, white_list, status " +
                "FROM " +
                "access_control_ip " +
                "WHERE " +
                "service_key = :serviceKey ";
        ACIP rs = npJdbcTemplate.queryForObject(sql, Collections.singletonMap("serviceKey", serviceKey), acIPRowMapper);
        return rs;
    }

    @Override
    public List<ACIP> listAllACIP() {
        String sql = "SELECT " +
                "id, service_key, type, black_list, white_list, status " +
                "FROM " +
                "access_control_ip ";
        List<ACIP> rs = npJdbcTemplate.query(sql, acIPRowMapper);
        return rs;
    }

    @Override
    public int updateACIPStatus(ACIP acIP) {
        String sql = "UPDATE " +
                "access_control_ip " +
                "SET " +
                "status = :status, update_time = :updateTime " +
                "WHERE " +
                "id = :id " +
                "AND " +
                "service_key = :serviceKey ";
        Date date = new Date();
        acIP.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acIP);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public int saveACFlow(ACFlow acFlow) {
        String sql = "INSERT INTO " +
                "access_control_flow " +
                "(id, service_key, type, time_interval, interval_threshold, single_threshold, status, create_time, update_time) " +
                "VALUES " +
                "(:id, :serviceKey, :type, :timeInterval, :intervalThreshold, :singleThreshold, :status, :createTime, :updateTime) ";
        Date date = new Date();
        acFlow.setCreateTime(date);
        acFlow.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acFlow);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public int removeACFlowByServiceKey(String serviceKey) {
        String sql = "DELETE FROM access_control_flow WHERE service_key = :serviceKey ";
        int rs = npJdbcTemplate.update(sql, Collections.singletonMap("serviceKey", serviceKey));
        return rs;
    }

    @Override
    public int updateACFlow(ACFlow acFlow) {
        String sql = "UPDATE " +
                "access_control_flow " +
                "SET " +
                "type = :type, time_interval = :timeInterval, interval_threshold = :intervalThreshold, " +
                "single_threshold = :singleThreshold, update_time = :updateTime " +
                "WHERE " +
                "id = :id " +
                "AND " +
                "service_key = :serviceKey ";
        Date date = new Date();
        acFlow.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acFlow);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public ACFlow getACFlowByServiceKey(String serviceKey) {
        String sql = "SELECT " +
                "id, service_key, type, time_interval, interval_threshold, single_threshold, status " +
                "FROM " +
                "access_control_flow " +
                "WHERE " +
                "service_key = :serviceKey ";
        ACFlow rs = npJdbcTemplate.queryForObject(sql, Collections.singletonMap("serviceKey", serviceKey), new BeanPropertyRowMapper<>(ACFlow.class));
        return rs;
    }

    @Override
    public List<ACFlow> listAllACFlow() {
        String sql = "SELECT " +
                "id, service_key, type, time_interval, interval_threshold, single_threshold, status " +
                "FROM " +
                "access_control_flow ";
        List<ACFlow> rs = npJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ACFlow.class));
        return rs;
    }

    @Override
    public int updateACFlowStatus(ACFlow acFlow) {
        String sql = "UPDATE " +
                "access_control_flow " +
                "SET " +
                "status = :status, update_time = :updateTime " +
                "WHERE " +
                "id = :id " +
                "AND " +
                "service_key = :serviceKey ";
        Date date = new Date();
        acFlow.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acFlow);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public int saveACFrequency(ACFrequency acFrequency) {
        String sql = "INSERT INTO " +
                "access_control_frequency " +
                "(id, service_key, type, time_interval, threshold, status, create_time, update_time) " +
                "VALUES " +
                "(:id, :serviceKey, :type, :timeInterval, :threshold, :status, :createTime, :updateTime) ";
        Date date = new Date();
        acFrequency.setCreateTime(date);
        acFrequency.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acFrequency);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public int removeACFrequencyByServiceKey(String serviceKey) {
        String sql = "DELETE FROM access_control_frequency WHERE service_key = :serviceKey ";
        int rs = npJdbcTemplate.update(sql, Collections.singletonMap("serviceKey", serviceKey));
        return rs;
    }

    @Override
    public int updateACFrequency(ACFrequency acFrequency) {
        String sql = "UPDATE " +
                "access_control_frequency " +
                "SET " +
                "type = :type, time_interval = :timeInterval, threshold = :threshold, update_time = :updateTime " +
                "WHERE " +
                "id = :id " +
                "AND " +
                "service_key = :serviceKey ";
        Date date = new Date();
        acFrequency.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acFrequency);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }

    @Override
    public ACFrequency getACFrequencyByServiceKey(String serviceKey) {
        String sql = "SELECT " +
                "id, service_key, type, time_interval, threshold, status " +
                "FROM " +
                "access_control_frequency " +
                "WHERE " +
                "service_key = :serviceKey ";
        ACFrequency rs = npJdbcTemplate.queryForObject(sql, Collections.singletonMap("serviceKey", serviceKey), new BeanPropertyRowMapper<>(ACFrequency.class));
        return rs;
    }

    @Override
    public List<ACFrequency> listAllACFrequency() {
        String sql = "SELECT " +
                "id, service_key, type, time_interval, threshold, status " +
                "FROM " +
                "access_control_frequency ";
        List<ACFrequency> rs = npJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ACFrequency.class));
        return rs;
    }

    @Override
    public int updateACFrequencyStatus(ACFrequency acFrequency) {
        String sql = "UPDATE " +
                "access_control_frequency " +
                "SET " +
                "status = :status, update_time = :updateTime " +
                "WHERE " +
                "id = :id " +
                "AND " +
                "service_key = :serviceKey ";
        Date date = new Date();
        acFrequency.setUpdateTime(date);
        BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(acFrequency);
        int rs = npJdbcTemplate.update(sql, beanParam);
        return rs;
    }
}

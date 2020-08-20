package cn.com.bluemoon.shorturl.servcie.impl;

import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.repository.ShortUrlQueryRecordRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @version v1.0
 * @ProjectName: short-url
 * @ClassName: ShortUrlQueryRecordServiceImpl
 * @Description: TODO(一句话描述该类的功能)
 * @Author: mayuli
 * @Date: 2020/8/19 17:16
 */
@Service(version = "3.0.0")
public class ShortUrlQueryRecordServiceImpl implements ShortUrlQueryRecordService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShortUrlQueryRecordRepository shortUrlQueryRecordRepository;


    @Override
    public void save(List<ShortUrlQueryRecordDto> list) {
        String sql = "INSERT INTO pf_short_url_query_record( ip,create_time, long_url) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                String ip= list.get(i).getIp();
                Timestamp create_time = list.get(i).getCreateTime();
                String long_url = list.get(i).getLongUrl();
                ps.setString(1, ip);
                ps.setTimestamp(2, create_time);
                ps.setString(3, long_url);
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }


}

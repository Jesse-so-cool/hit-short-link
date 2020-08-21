package cn.com.bluemoon.shorturl.servcie.impl;

import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.redis.RedisUtils;
import cn.com.bluemoon.shorturl.repository.ShortUrlQueryRecordRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import com.alibaba.dubbo.config.annotation.Service;



import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.standard.utils.ResponseBeanUtil;

import net.sf.json.JSONArray;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(ShortUrlQueryRecordServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShortUrlQueryRecordRepository shortUrlQueryRecordRepository;

    @Autowired
    RedisUtils redisUtils;

    public final static String key = "record-error-list";

    @Override
    public void save(List<ShortUrlQueryRecordDto> list) {
        String sql = "INSERT INTO pf_short_url_query_record( ip,create_time, long_url) VALUES (?, ?, ?)";

        try {
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
        }catch (Exception e){
            for (int i = 0, len = list.size(); i<len; i++){
                redisUtils.push(key, list.get(i).toString());
            }
            logger.debug("数据异常:"+e.getMessage());
        }

    }

    @Override
    public ResponseBean getErrorMsg(int start , int end) {
        List<String> res = redisUtils.range(key,start,end);
        return  ResponseBeanUtil.createScBean(res);
    }


}

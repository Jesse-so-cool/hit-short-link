package cn.com.bluemoon.shorturl.servcie.impl;

import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.init.RecordTask;
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
import java.util.ArrayList;
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
    public boolean save(List<ShortUrlQueryRecordDto> list) {
        String sql = "INSERT INTO pf_short_url_query_record( ip,create_time, long_url,short_url) VALUES (?, ?, ?,?)";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String ip = list.get(i).getIp();
                    Timestamp createTime = list.get(i).getCreateTime();
                    String longUrl = list.get(i).getLongUrl();
                    String shortUrl = list.get(i).getShortUrl();
                    ps.setString(1, ip);
                    ps.setTimestamp(2, createTime);
                    ps.setString(3, longUrl);
                    ps.setString(4, shortUrl);
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
            return true;
        } catch (Exception e) {

            for (int i = 0, len = list.size(); i < len; i++) {
                redisUtils.push(key, list.get(i).toString());
            }
            logger.debug("数据异常:" + e.getMessage());
            return false;
        }

    }

    @Override
    public ResponseBean getErrorMsg(int start, int end) {
        List<String> res = redisUtils.range(key, start, end);
        return ResponseBeanUtil.createScBean(res);
    }

    @Override
    public ResponseBean checkErrorMsg(int amount, boolean flag) {

        List<String> errorMsgList = redisUtils.batchPopList(key, amount);
        if (errorMsgList.size() > 0) {
            if (flag) {
                //数据库
                List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<>();
                for (String msg : errorMsgList) {
                    ShortUrlQueryRecordDto shortUrlQueryRecordDto = JSONObject.parseObject(msg, ShortUrlQueryRecordDto.class);
                    shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
                }
                if (!save(shortUrlQueryRecordDtoList)) {
                    return ResponseBeanUtil.createFailBean(-1, "数据仍存在问题，请检查后重新提交");
                }
                return ResponseBeanUtil.createScBean("数据刷新完毕");
            } else {
                return ResponseBeanUtil.createScBean("数据提交成功");
            }
        }
        return ResponseBeanUtil.createScBean("缓存数据已完全清除");
    }

    @Override
    public void saveShortUrlQueryRecordDto(ShortUrlQueryRecordDto shortUrlQueryRecordDto) {
        redisUtils.push(RecordTask.key, JSONObject.toJSONString(shortUrlQueryRecordDto));
    }


}

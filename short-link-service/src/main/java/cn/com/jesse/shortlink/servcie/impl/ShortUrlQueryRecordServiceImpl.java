package cn.com.jesse.shortlink.servcie.impl;

import cn.com.jesse.shortlink.dto.ShortUrlQueryRecordDto;
import cn.com.jesse.shortlink.init.RecordTask;
import cn.com.jesse.shortlink.redis.RedisUtils;
import cn.com.jesse.shortlink.servcie.ShortUrlQueryRecordService;


import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.standard.utils.ResponseBeanUtil;

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
 * @Author: mayuli
 * @Date: 2020/8/19 17:16
 */
@org.springframework.stereotype.Service
public class ShortUrlQueryRecordServiceImpl implements ShortUrlQueryRecordService {

    Logger logger = LoggerFactory.getLogger(ShortUrlQueryRecordServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    RedisUtils redisUtils;

    public final static String KEY = "record-error-list";

    @Override
    public boolean save(List<ShortUrlQueryRecordDto> list) {
        String sql = "INSERT INTO pf_short_url_query_record( ip,create_time, long_url,short_url,user_agent) VALUES (?, ?, ?,?,?)";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String ip = list.get(i).getIp();
                    Timestamp createTime = list.get(i).getCreateTime();
                    String longUrl = list.get(i).getLongUrl();
                    String shortUrl = list.get(i).getShortUrl();
                    String userAgent = list.get(i).getUserAgent();
                    ps.setString(1, ip);
                    ps.setTimestamp(2, createTime);
                    ps.setString(3, longUrl);
                    ps.setString(4, shortUrl);
                    ps.setString(5, userAgent);
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
            return true;
        } catch (Exception e) {

            for (int i = 0, len = list.size(); i < len; i++) {
                redisUtils.push(KEY, list.get(i).toString());
            }
            logger.error(e.getMessage(), e);
            return false;
        }

    }

    @Override
    public ResponseBean getErrorMsg(int start, int end) {
        if (start < 0 || end < 0) {
            return ResponseBeanUtil.createFailBean(-1, "??????????????????????????????");
        }
        List<String> res = redisUtils.range(KEY, start, end);
        int size = res.size();
        if (size > 0) {
            List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = parse(res);
            return ResponseBeanUtil.createScBean(shortUrlQueryRecordDtoList);
        }
        return ResponseBeanUtil.createScBean("???????????????");

    }

    private List<ShortUrlQueryRecordDto> parse(List<String> res) {
        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<>();
        for (String msg : res) {
            ShortUrlQueryRecordDto shortUrlQueryRecordDto = JSONObject.parseObject(msg, ShortUrlQueryRecordDto.class);
            shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        }
        return shortUrlQueryRecordDtoList;
    }

    @Override
    public ResponseBean checkErrorMsg(int amount, boolean flag) {

        if (amount < 0) {
            return ResponseBeanUtil.createFailBean(-1, "??????????????????????????????");
        }

        List<String> errorMsgList = redisUtils.batchPopList(KEY, amount);
        if (errorMsgList.size() > 0) {
            if (flag) {
                //?????????
                List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = parse(errorMsgList);
                if (!save(shortUrlQueryRecordDtoList)) {
                    return ResponseBeanUtil.createFailBean(-1, "????????????????????????????????????????????????");
                }
                return ResponseBeanUtil.createScBean("??????????????????");
            } else {
                return ResponseBeanUtil.createScBean("??????????????????");
            }
        }
        return ResponseBeanUtil.createScBean("???????????????????????????");
    }

    @Override
    public void saveShortUrlQueryRecordDto(ShortUrlQueryRecordDto shortUrlQueryRecordDto) {
        redisUtils.push(RecordTask.key, JSONObject.toJSONString(shortUrlQueryRecordDto));
    }


}

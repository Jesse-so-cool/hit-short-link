package cn.com.jesse.shortlink.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @version v1.0
 * @ProjectName: short-url
 * @ClassName: ShortUrlQueryRecordDto
 * @Author: mayuli
 * @Date: 2020/8/19 20:54
 */
@Data
public class ShortUrlQueryRecordDto implements Serializable {

    private String ip;
    private Timestamp createTime;
    private String longUrl;
    private String shortUrl;
    private String userAgent;

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
    }

}

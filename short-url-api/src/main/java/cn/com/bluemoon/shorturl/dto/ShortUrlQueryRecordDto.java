package cn.com.bluemoon.shorturl.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import com.alibaba.fastjson.JSONObject;
/**
 * @version v1.0
 * @ProjectName: short-url
 * @ClassName: ShortUrlQueryRecordDto
 * @Description: TODO(一句话描述该类的功能)
 * @Author: mayuli
 * @Date: 2020/8/19 20:54
 */
@Data

public class ShortUrlQueryRecordDto implements Serializable {
    private long id;
    private String ip;
    private Timestamp createTime;
    private String longUrl;
    private String shortUrl;

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }

}

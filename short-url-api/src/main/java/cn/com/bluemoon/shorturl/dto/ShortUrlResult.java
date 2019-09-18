package cn.com.bluemoon.shorturl.dto;

import lombok.Data;

/**
 * 短链接返回结果
 *
 * @author XuZhuohao
 * @date 2019/9/18
 */
@Data
public class ShortUrlResult {
    /**
     * 短链接
     */
    private String shortUrl;
    /**
     * 长连接
     */
    private String longUrl;
    /**
     * 返回信息
     */
    private String responseMsg;
    /**
     * 是否成功
     */
    private boolean isSuccess;

    public ShortUrlResult(String shortUrl, String longUrl, String responseMsg, boolean isSuccess) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.responseMsg = responseMsg;
        this.isSuccess = isSuccess;
    }
    public ShortUrlResult(){

    }
}

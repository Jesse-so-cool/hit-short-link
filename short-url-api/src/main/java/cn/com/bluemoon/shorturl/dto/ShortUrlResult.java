package cn.com.bluemoon.shorturl.dto;


/**
 * 短链接返回结果
 *
 */
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

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ShortUrlResult(String shortUrl, String longUrl, String responseMsg, boolean isSuccess) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.responseMsg = responseMsg;
        this.isSuccess = isSuccess;
    }
    public ShortUrlResult(){

    }
}

package cn.com.bluemoon.shorturl.dto;


public class ShortUrlDto {
    /**
     * 长链接
     */
    private String longUrl;

    /**
     * 有效期(单位: 天)
     */
    private Long validDate;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Long getValidDate() {
        return validDate;
    }

    public void setValidDate(Long validDate) {
        this.validDate = validDate;
    }
}

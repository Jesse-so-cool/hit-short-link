package cn.com.bluemoon.shorturl.dto;



//import com.bluemoon.pf.mgr.anno.BmParam;

import com.bluemoon.pf.mgr.anno.*;

import java.io.Serializable;


public class ShortUrlDto implements Serializable {
    /**
     * 长链接 必填
     */
   // @BmParam(value = "longUrl",comment = "长链接")
    private String longUrl;
    /**
     * 有效期(单位: 天)  非必填
     */
  //  @BmParam(value = "validDate",comment = "有效期/天")
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

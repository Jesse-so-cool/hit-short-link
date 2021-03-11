package cn.com.jesse.shortlink.dto;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @version v1.0
 * @ProjectName: short-url
 * @ClassName: ShortUrlQueryRecordEntity
 * @Description: pf_short_url_query_record表 实体类
 * @Author: mayuli
 * @Date: 2020/8/17 9:33
 */
@Entity
@Table(name = "pf_short_url_query_record", schema = "moonmiddle", catalog = "")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ShortUrlQueryRecordEntity {

    private long id;
    private String ip;
    private Timestamp createTime;
    private String longUrl;
    private String shortUrl;
    private String userAgent;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ip", nullable = true, length = 15)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "long_url", nullable = true, length = 255)
    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Basic
    @Column(name = "short_url", nullable = true, length = 10)
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @Basic
    @Column(name = "user_agent")
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}

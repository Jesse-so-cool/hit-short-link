package cn.com.bluemoon.shorturl.dto;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author XuZhuohao
 * @date 2019/9/18
 */
@Entity
@Table(name = "pf_short_url", schema = "moonmiddle", catalog = "")
@EntityListeners(AuditingEntityListener.class)
public class PfShortUrlEntity {
    private long id;
    private String longUrl;
    private Byte isValid;
    private Timestamp validDate;
    private Timestamp createDate;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    @Column(name = "is_valid", nullable = true)
    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    @Basic
    @Column(name = "valid_date", nullable = true)
    public Timestamp getValidDate() {
        return validDate;
    }

    public void setValidDate(Timestamp validDate) {
        this.validDate = validDate;
    }

    @Basic
    @Column(name = "create_date", nullable = true)
    @CreatedDate
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PfShortUrlEntity that = (PfShortUrlEntity) o;
        return id == that.id &&
                Objects.equals(longUrl, that.longUrl) &&
                Objects.equals(isValid, that.isValid) &&
                Objects.equals(validDate, that.validDate) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, longUrl, isValid, validDate, createDate);
    }
}

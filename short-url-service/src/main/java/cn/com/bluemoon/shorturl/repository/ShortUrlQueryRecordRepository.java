package cn.com.bluemoon.shorturl.repository;

import cn.com.bluemoon.shorturl.dto.ShortUrlEntity;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @version v1.0
 * @ProjectName: short-url
 * @ClassName: ShortUrlQueryRecordRepository
 * @Description: 操作 pf_short_url_query_record 表
 * @Author: mayuli
 * @Date: 2020/8/17 9:58
 */
public interface ShortUrlQueryRecordRepository extends JpaRepository<ShortUrlQueryRecordEntity, Long> {


}

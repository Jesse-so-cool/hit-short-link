package cn.com.bluemoon.shorturl.repository;

import cn.com.bluemoon.shorturl.dto.PfShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author XuZhuohao
 * @date 2019/9/18
 */
public interface PfShortUrlRepository extends JpaRepository<PfShortUrlEntity, Long> {
}

package cn.com.bluemoon.shorturl.repository;

import cn.com.bluemoon.shorturl.dto.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @date 2019/9/18
 */
public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {


    ShortUrlEntity findByLongUrlAndAndIsValid(String longUrl, byte isValid);

}

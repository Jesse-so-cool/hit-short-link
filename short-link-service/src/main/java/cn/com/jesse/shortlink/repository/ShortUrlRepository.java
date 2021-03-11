package cn.com.jesse.shortlink.repository;

import cn.com.jesse.shortlink.dto.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @date 2019/9/18
 */
public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {


    ShortUrlEntity findByLongUrlAndAndIsValid(String longUrl, byte isValid);

    ShortUrlEntity findByIdAndAndIsValid(Long id, byte isValid);

}

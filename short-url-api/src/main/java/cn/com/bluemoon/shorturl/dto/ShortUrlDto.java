package cn.com.bluemoon.shorturl.dto;

import lombok.Data;

@Data
public class ShortUrlDto {

    private String longUrl;
    private Long validDate;

}

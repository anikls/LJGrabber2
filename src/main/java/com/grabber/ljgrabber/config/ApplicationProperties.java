package com.grabber.ljgrabber.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Data
public class ApplicationProperties {

    /** Папка формирования выходных файлов. */
    String outPath;

    /** Автор поста. */
    String author;

    /** Дата, с которой начинаем просматривать посты. */
    String startDate;

    /** Ключ яндекс переводчика. */
    String translateKey;


}

package com.grabber.ljgrabber.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Configuration
@ConfigurationProperties(prefix = "grabber")
@Data
public class ApplicationProperties {

    /** Папка формирования выходных файлов. */
    String outPath;

    /** Дата, с которой начинаем просматривать посты. */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate;

    /** Ключ яндекс переводчика. */
    String translateKey;


}

package com.grabber.ljgrabber.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

	private long id;

    private long itemId;

    private LocalDateTime eventTime;

	private String subject;

	private String url;

	private String body;

	private String author;

    public String getEventText() {
        return Jsoup.parse(body).text();
    }

    public String getText() {
        return Jsoup.parse(body).text();
    }
}

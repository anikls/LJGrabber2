package com.grabber.ljgrabber.entity.dto;

import com.grabber.ljgrabber.utils.Conversion;
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

    public String getFormatText() {
        return Jsoup.parse(body).text();
    }

    public String getText() {
        // Сохраняет исходное форматирование
        String text = body.replaceAll("\r\n", "<br/>");
        //final String text2 = Jsoup.parse(text).text();
        //Jsoup.
        return Conversion.transformURLIntoLinks(text);
        //return text;
    }
}

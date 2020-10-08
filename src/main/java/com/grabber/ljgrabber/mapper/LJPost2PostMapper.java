package com.grabber.ljgrabber.mapper;

import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.entity.lj.LJPost;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Маппер преобразования сущности LJPost в PostDto.
 */
public class LJPost2PostMapper extends PropertyMap<LJPost, PostDto> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void configure() {
        map(source.getItemid(), destination.getId());
        map(source.getSubject(), destination.getSubject());
        map(source.getEvent(), destination.getBody());
        map(source.getAuthor(), destination.getAuthor());
        using(eventTimeConverter).map(source.getEventtime(), destination.getEventTime());
    }

    private Converter<String, LocalDateTime> eventTimeConverter =
            src -> LocalDateTime.parse(src.getSource(), FORMATTER);
}

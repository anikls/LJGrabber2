package com.grabber.ljgrabber.mapper;

import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.lj.entity.LJPost;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LJPost2PostMapper extends PropertyMap<LJPost, PostDto> {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void configure() {
        map(source.getItemid(), destination.getId());
        map(source.getSubject(), destination.getSubject());
        map(source.getEvent(), destination.getBody());
        map(source.getAutorId(), destination.getAuthorId());
        using(eventTimeConverter).map(source.getEventtime(), destination.getEventTime());
    }

    private Converter<String, Date> eventTimeConverter = src -> {
        try {
            return FORMATTER.parse(src.getSource());
        } catch (ParseException e) {
            return null;
        }
    };
}

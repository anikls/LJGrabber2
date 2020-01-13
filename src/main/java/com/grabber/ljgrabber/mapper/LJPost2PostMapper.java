package com.grabber.ljgrabber.mapper;

import com.grabber.ljgrabber.db.entity.Post;
import com.grabber.ljgrabber.lj.entity.LJPost;
import org.modelmapper.PropertyMap;

public class LJPost2PostMapper extends PropertyMap<LJPost, Post> {

    @Override
    protected void configure() {
        map(source.getItemid(), destination.getId());
        map(source.getSubject(), destination.getSubject());
        map(source.getEvent(), destination.getBody());
        map(source.getAutorId(), destination.getAuthorId());
       // using(eventTimeConverter).map(source.getEventtime(), destination.getEventTime());
    }

//    private Converter<String, Date> eventTimeConverter = src -> {
//        SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            return new Date();
//
//
//    };
}

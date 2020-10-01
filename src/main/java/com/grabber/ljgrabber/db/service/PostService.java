package com.grabber.ljgrabber.db.service;

import com.grabber.ljgrabber.entity.dto.PostDto;

import java.util.List;

public interface PostService {
    void save(PostDto post);
    List<PostDto> findAll();
    PostDto getById(Long id);
}

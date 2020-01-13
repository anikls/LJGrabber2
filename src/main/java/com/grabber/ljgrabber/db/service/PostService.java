package com.grabber.ljgrabber.db.service;

import com.grabber.ljgrabber.db.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void save(Post post);
    List<Post> findAll();
    Optional<Post> getById(Long id);
}

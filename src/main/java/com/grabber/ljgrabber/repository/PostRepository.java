package com.grabber.ljgrabber.repository;

import com.grabber.ljgrabber.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> readAll();
    List<Post> readAllByYear(String year);
    Optional<Post> read(long id);
    Optional<Post> lastPost();
    Optional<Post> firstPost();
    void saveNew(List<Post> items);
}

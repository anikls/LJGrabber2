package com.grabber.ljgrabber.db.repository;

import com.grabber.ljgrabber.db.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long>, JpaRepository<Post, Long> {
    List<Post> findAll();
    Optional<Post> readById(long id);
    Optional<Post> findFirstByOrderByEventTimeDesc();
}

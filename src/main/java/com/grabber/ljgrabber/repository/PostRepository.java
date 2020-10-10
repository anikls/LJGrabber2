package com.grabber.ljgrabber.repository;

import com.grabber.ljgrabber.entity.db.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface PostRepository extends CrudRepository<Post, Long>, JpaRepository<Post, Long> {

    /**
     * Получение списка всех публикаций автора.
     * @param author
     * @return
     */
    List<Post> findAllByAuthor(String author);

    /**
     * Получение списка публикаций автора за заданное время.
     * @param author
     * @param start
     * @param end
     * @return
     */
    Stream<Post> findPostsByAuthorAndEventTimeBetween(String author, LocalDateTime start, LocalDateTime end);

    /**
     * Получение публикации по идентификатору.
     * @param id
     * @return
     */
    Optional<Post> getPostByItemId(long id);

    /**
     * Последняя публикация автора.
     * @return
     */
    Optional<Post> findFirstByAuthorOrderByEventTimeDesc(String author);

    /**
     * Первая публикация автора.
     * @return
     */
    Optional<Post> findFirstByAuthorOrderByEventTimeAsc(String author);
}

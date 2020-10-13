package com.grabber.ljgrabber.service;

import com.grabber.ljgrabber.entity.dto.PostDto;

import java.util.List;
import java.util.Optional;

/**
 * Работа с локальным хранилищем публикаций.
 */
public interface PostService {
    /**
     * Сохраняет публикацию в хранилище.
     * @param post
     */
    void save(PostDto post);

    /**
     * Выгрузка всех публикаций автора.
     * @return
     */
    List<PostDto> findAll(String author);

    /**
     * Выгрузка всех публикаций автора за год.
     * @return
     */
    List<PostDto> findAllByYear(String author, int year);

    /**
     * Получить публикацию по идентификатору.
     * @param id идентификатор публикации
     * @return
     */
    PostDto getById(Long id);

    /**
     * Получить публикацию автора по идентификатору публикации.
     * @param author автор публикации
     * @param id идентификатор публикации
     * @return
     */
    PostDto getByItemId(String author, Long id);

    /**
     * Получить последнюю по дате публикацию автора.
     * @return
     */
    Optional<PostDto> getLastPost(String author);

    /**
     * Получить первую по дате публикацию автора.
     * @return
     */
    Optional<PostDto> getFirstPost(String author);
}

package com.grabber.ljgrabber.db.service;

import com.grabber.ljgrabber.entity.dto.PostDto;

import java.util.List;
import java.util.Optional;

public interface PostService {
    /**
     * Сохраняет публикацию в хранилище.
     * @param post
     */
    void save(PostDto post);

    /**
     * Выгрузка всех публикаций.
     * @return
     */
    List<PostDto> findAll();

    /**
     * Получить публикацию по идентификатору.
     * @param id идентификатор публикации
     * @return
     */
    PostDto getById(Long id);

    /**
     * Получить последнюю по дате публикацию.
     * @return
     */
    Optional<PostDto> getLastPost();
}

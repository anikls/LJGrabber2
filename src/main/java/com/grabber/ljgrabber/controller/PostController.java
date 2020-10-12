package com.grabber.ljgrabber.controller;

import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для работы с сохраненными публикациями.
 *
 * @author aniko
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    /**
     * Сервис для работы с публикациями.
     */
    private final PostService postService;

    /**
     * Получить все публикации автора.
     * @param author автор публикаций
     * @return
     */
    @GetMapping("/{author}/all")
    public List<PostDto> retrieveAllPosts(@PathVariable String author) {
        return postService.findAll(author);
    }

    /**
     * Получить публикацию по идентификатору.
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public PostDto retrievePost(@PathVariable long id) {
        return postService.getById(id);
    }

}

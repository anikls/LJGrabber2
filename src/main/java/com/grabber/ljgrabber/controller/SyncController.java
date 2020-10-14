package com.grabber.ljgrabber.controller;

import com.grabber.ljgrabber.exception.PostExistsException;
import com.grabber.ljgrabber.service.PostService;
import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.service.LJClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Контроллер для синхронизации данных из внешних источников.
 *
 * @author aniko
 */
@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
@Slf4j
public class SyncController {

    private static final String NEW_LINE = "<br/>";

    /**
     * Сервис для работы с хранилищем публикаций.
     */
    private final PostService postService;

    /**
     * Клиент для получения данных из LJ.
     */
    private final LJClient ljClient;

    /**
     * Маппер для преобразования сущностей.
     */
    private final ModelMapper modelMapper;

    /**
     * Загрузка публикаций из LJ за указанный год в локальное хранилище.
     * @param author автор публикаций
     * @param year год загрузки публикаций
     * @return лог выполнения операции
     */
    @GetMapping("/lj/{author}/{year}")
    public String downloadPosts(@PathVariable("author") String author,
                                @PathVariable("year") Integer year) {

        final StringBuilder console = new StringBuilder();

        ljClient.downloadPosts(author, year)
                .forEach(post -> {
                    try {
                        postService.save(modelMapper.map(post, PostDto.class));
                        log.info("Успешно загружена публикация {}", post);
                        console.append("Успешно загружена публикация ").append(post.getItemId()).append(NEW_LINE);
                    } catch (PostExistsException e) {
                        log.warn("Публикация {} уже загружена", post);
                        console.append("Публикация ").append(post.getItemId()).append(" уже загружена").append(NEW_LINE);
                    }
                });

        return console.toString();
    }

    /**
     * Загрузка последних публикаций из LJ,
     * отсутствующих в локальном хранилище
     * @param author автор публикаций
     * @param startDate дата начала синхронизации (если не указана, то будет использована дата последней публикации)
     * @return лог выполнения операции
     */
    @GetMapping("/lj/{author}")
    public String loadNewPosts(
            @PathVariable("author") String author,
            @Nullable
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        final StringBuilder console = new StringBuilder();

        ljClient.downloadNewPosts(author, startDate)
            .forEach(post -> {
                try {
                    postService.save(modelMapper.map(post, PostDto.class));
                    log.info("Успешно загружена публикация {}", post);
                    console.append("Успешно загружена публикация ").append(post.getItemId()).append(NEW_LINE);
                } catch (PostExistsException e) {
                    log.warn("Публикация {} уже загружена", post);
                    console.append("Публикация ").append(post.getItemId()).append(" уже загружена").append(NEW_LINE);
                }
        });

        return console.toString();
    }
}

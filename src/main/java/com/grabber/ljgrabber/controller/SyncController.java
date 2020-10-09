package com.grabber.ljgrabber.controller;

import com.grabber.ljgrabber.exception.PostExistsException;
import com.grabber.ljgrabber.service.PostService;
import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.entity.lj.LJPost;
import com.grabber.ljgrabber.service.LJClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для синхронизации данных.
 *
 * @author aniko
 */
@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
@Slf4j
public class SyncController {

    private final PostService postService;
    private final LJClient ljClient;

    /**
     * Маппер для преобразования сущностей.
     */
    private final ModelMapper modelMapper;

    /**
     * Загрузка публикаций из LJ за указанный год в БД.
     * @param author автор публикаций
     * @param year год загрузки публикаций
     * @return
     */
    @GetMapping("/lj/{author}/{year}")
    public String downloadPosts(@PathVariable("author") String author,
                                                @PathVariable("year") Integer year)
     {
        final StringBuilder console = new StringBuilder();

        List<LJPost> ljPosts = ljClient.downloadPosts(author, year);
         ljPosts.forEach(post -> {
             try {
                 postService.save(modelMapper.map(post, PostDto.class));
                 console.append("Успешно загружена публикация ").append(post.getItemId()).append("<br/>");
             } catch (PostExistsException e){
                 console.append("Публикация ").append(post.getItemId()).append(" уже загружена").append("<br/>");
             }
         });

        return console.toString();
    }

    /**
     * Загрузка последних публикаций из LJ,
     * отсутствующих в локальном хранилище
     *
     * @return
     */
    @GetMapping("/lj/{author}")
    public String loadNewPosts(
            @PathVariable("author") String author,
            @Nullable
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        final StringBuilder console = new StringBuilder();

        List<LJPost> ljPosts = ljClient.downloadNewPosts(author, startDate);
        ljPosts.forEach(post -> {
            try {
                postService.save(modelMapper.map(post, PostDto.class));
                console.append("Успешно загружена публикация ").append(post.getItemId()).append("<br/>");
            } catch (PostExistsException e){
                console.append("Публикация ").append(post.getItemId()).append(" уже загружена").append("<br/>");
            }
        });

        return console.toString();
    }

}

package com.grabber.ljgrabber.controller;

import com.grabber.ljgrabber.service.HtmlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для генерации html.
 *
 * @author aniko
 */
@RestController
@RequestMapping("/html")
@RequiredArgsConstructor
@Slf4j
public class HtmlController {

    /**
     * Сервис для работы с html
     */
    private final HtmlService htmlService;

    /**
     * Выгрузить все публикации автора в html
     * @param author автор публикаций
     * @return
     */
    @GetMapping("/{author}/all")
    public String generateHtml(@PathVariable("author") String author,
                               @RequestParam("title") String title) {
        return htmlService.generateAll(author, title);
    }

    /**
     * Выгрузить конкретную публикации автора в html
     * @param author автор публикаций
     * @param itemId идентификатор публикации
     * @return
     */
    @GetMapping("/{author}/{itemId}")
    public String generateHtml(@PathVariable("author") String author,
                               @PathVariable("itemId") long itemId) {
        return htmlService.generateHtmlPost(author, itemId);
    }
}

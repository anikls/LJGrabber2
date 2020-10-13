package com.grabber.ljgrabber.service;

/**
 * Сервис для работы с генерацией html.
 */
public interface HtmlService {
    /**
     * Сгенерировать html-представление публикация для всех постов
     */
    String generateAll(String author);

    /**
     * Выгрузить конкретную публикации автора в html
     * @param author
     * @param itemId
     * @return
     */
    String generateHtmlPost(String author, long itemId);
}

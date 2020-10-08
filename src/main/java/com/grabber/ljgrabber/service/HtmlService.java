package com.grabber.ljgrabber.service;

/**
 * Сервис для работы с генерацией html
 */
public interface HtmlService {
    /**
     * Сгенерировать html-представление публикация для всех постов
     */
    void generateAll(String author);
}

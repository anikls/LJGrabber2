package com.grabber.ljgrabber.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка при существовании публикации.
 */
@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
public class PostExistsException extends RuntimeException {

    private long idPost;

    public PostExistsException(long idPost) {
        this.idPost = idPost;
    }
}

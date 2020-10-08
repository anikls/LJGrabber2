package com.grabber.ljgrabber.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка при существовании публикации.
 */
@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
@Data
public class PostExistsException extends RuntimeException {

    private final long idPost;

    public PostExistsException(long idPost) {
        this.idPost = idPost;
    }
}

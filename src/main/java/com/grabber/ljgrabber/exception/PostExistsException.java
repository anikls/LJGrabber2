package com.grabber.ljgrabber.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка при существовании публикации.
 */
@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostExistsException extends RuntimeException {

    @Getter
    private final long idPost;

    public PostExistsException(long idPost) {
        this.idPost = idPost;
    }
}

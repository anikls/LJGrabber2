package com.grabber.ljgrabber.entity.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "POST")
public class Post {
	/**
	 * Внутренний идентификатор записи.
	 */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;

	/**
	 * Идентификатор в опубликованном источнике.
	 */
	@Column(name = "item_id", nullable = false)
	private long itemId;

	/**
	 * Дата/время публикации.
	 */
	@Column(name = "event_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime eventTime;

	/**
	 * Заголовок публикации.
	 */
    @Column(name = "subject")
	private String subject;

	/**
	 * Ссылка на первоисточник.
	 */
    @Column(name = "url", nullable = false)
	private String url;

	/**
	 * Контент публикации.
	 */
	@Column(name = "body", nullable = false, columnDefinition="TEXT")
	private String body;

	/**
	 * Имя автора.
	 */
	@Column(name = "author", nullable = false)
	private String author;
}

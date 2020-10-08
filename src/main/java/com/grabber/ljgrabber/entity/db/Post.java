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
	@Id
	@Column(name = "id_post", nullable = false)
	private long id;

	@Column(name = "event_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "subject")
	private String subject;

    @Column(name = "url", nullable = false)
	private String url;

    @Column(name = "body", nullable = false, columnDefinition="TEXT")
	private String body;

    @Column(name = "author", nullable = false)
	private String author;
}

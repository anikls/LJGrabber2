package com.grabber.ljgrabber.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "POST")
public class Post {
	@Id
	@GeneratedValue
	@Column(name = "id_post", nullable = false)
	private long id;

	@Column(name = "event_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date eventTime;

    @Column(name = "subject")
	private String subject;

    @Column(name = "url", nullable = false)
	private String url;

    @Column(name = "body", nullable = false)
	private String body;

    @Column(name = "author_id", nullable = false)
	private Long authorId;
}

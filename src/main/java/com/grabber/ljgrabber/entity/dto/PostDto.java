package com.grabber.ljgrabber.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

	private long id;

	private LocalDateTime eventTime;

	private String subject;

	private String url;

	private String body;

	private String author;
}

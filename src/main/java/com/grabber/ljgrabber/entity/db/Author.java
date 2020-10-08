package com.grabber.ljgrabber.entity.db;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "AUTHOR")
public class Author {
	@Id
	@GeneratedValue
	@Column(name = "id_author", nullable = false)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;
}

package com.grabber.ljgrabber.entity.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
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

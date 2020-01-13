package com.grabber.ljgrabber.lj.entity;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {
	
	@XmlElement(name="id")
	private long id;
	@XmlElement(name="name")
	private String name;
		
}

package com.grabber.ljgrabber.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="posts")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ListPost {

	@XmlElement(name="post")
	private List<Post> posts = new ArrayList();
	
}

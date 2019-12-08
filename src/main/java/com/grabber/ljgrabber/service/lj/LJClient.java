package com.grabber.ljgrabber.service.lj;

import com.grabber.ljgrabber.entity.Author;
import com.grabber.ljgrabber.entity.Post;

import java.util.List;

public interface LJClient {
	List<Post> loadFromLJ(Author autor, int year, int month, int day);
	List<Post> loadFromLJ(Author autor, String lastSync);
}

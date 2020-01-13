package com.grabber.ljgrabber.lj.service;

import com.grabber.ljgrabber.lj.entity.Author;
import com.grabber.ljgrabber.lj.entity.LJPost;

import java.util.List;

public interface LJClient {
	List<LJPost> loadFromLJ(Author author, int year, int month, int day);
	List<LJPost> loadFromLJ(Author author, String lastSync);
	List<LJPost> downloadPosts(Author author, int year);
}

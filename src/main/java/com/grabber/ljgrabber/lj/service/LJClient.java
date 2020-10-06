package com.grabber.ljgrabber.lj.service;

import com.grabber.ljgrabber.lj.entity.LJPost;

import java.util.List;

public interface LJClient {
	List<LJPost> loadFromLJ(String author, int year, int month, int day);
	List<LJPost> loadFromLJ(String author, String lastSync);
	List<LJPost> downloadPosts(String author, int year);
}

package com.grabber.ljgrabber.service;

import com.grabber.ljgrabber.entity.lj.LJPost;

import java.util.List;

/**
 * Получение информации из LJ.
 */
public interface LJClient {
	List<LJPost> loadFromLJ(String author, int year, int month, int day);
	List<LJPost> loadFromLJ(String author, String lastSync);
	List<LJPost> downloadPosts(String author, int year);
}

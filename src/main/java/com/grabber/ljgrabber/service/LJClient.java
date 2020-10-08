package com.grabber.ljgrabber.service;

import com.grabber.ljgrabber.entity.lj.LJPost;

import java.time.LocalDate;
import java.util.List;

/**
 * Получение информации из LJ.
 */
public interface LJClient {

	/**
	 * Выгрузить все публикации автора за год из LJ за указанный день
	 * @param author
	 * @param date
	 * @return
	 */
	List<LJPost> loadFromLJ(String author, LocalDate date);

	/**
	 * Выгрузить все публикации автора за год из LJ
	 * @param author
	 * @param year
	 * @return
	 */
	List<LJPost> downloadPosts(String author, int year);

	/**
	 * Выгрузить публикации автора с момента последней синхронизации из LJ
	 * @param author
	 * @return
	 */
	List<LJPost> downloadNewPosts(String author, LocalDate startDate);
}

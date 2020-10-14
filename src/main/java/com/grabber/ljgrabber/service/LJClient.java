package com.grabber.ljgrabber.service;

import com.grabber.ljgrabber.entity.lj.LJPost;

import java.time.LocalDate;
import java.util.stream.Stream;

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
	Stream<LJPost> loadFromLJ(String author, LocalDate date);

	/**
	 * Выгрузить все публикации автора за год из LJ
	 * @param author
	 * @param year
	 * @return
	 */
	Stream<LJPost> downloadPosts(String author, int year);

	/**
	 * Выгрузить публикации автора с момента последней синхронизации из LJ
	 * @param author
	 * @return
	 */
	Stream<LJPost> downloadNewPosts(String author, LocalDate startDate);
}

package com.grabber.ljgrabber.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@UtilityClass
@Slf4j
public class DateUtils {
	
	public static LocalDate nextDate(LocalDate checkedDate) {
		checkedDate = checkedDate.plusDays(1);
		if (checkedDate.getMonthValue() == 1) {
			checkedDate = checkedDate.plusMonths(1);
			if (checkedDate.getMonthValue() == 1) {
				checkedDate = checkedDate.plusYears(1);
			}
		}
		return checkedDate;
	}

	/**
	 * Возвращает диапазон дат.
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Stream<LocalDate> getDatesBetween(
			LocalDate startDate, LocalDate endDate) {
		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		return IntStream.iterate(0, i -> i + 1)
				.limit(numOfDaysBetween)
				.mapToObj(i -> startDate.plusDays(i));
	}
}

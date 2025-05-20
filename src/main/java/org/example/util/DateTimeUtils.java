package org.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс для преобразования строкового представления даты и времени
 * в объект LocalDateTime
 */
public class DateTimeUtils {

    /**
     * Паттерн для форматирования даты и времени.
     * Формат: "yyyy-MM-dd HH:mm:ss" (пример, "2023-12-31 23:59:59").
     */
    public static final DateTimeFormatter DB_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Преобразует строку с датой и временем в объект LocalDateTime.
     * Ожидается строка в формате "yyyy-MM-dd HH:mm:ss". Если передается null,
     * метод возвращает null.
     *
     * @param dateTimeString строка с датой и временем для парсинга (может быть null)
     * @return объект LocalDateTime
     * @throws IllegalArgumentException если строка имеет неверный формат
     *                                  и не может быть преобразована в LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null) return null;
        try {
            return LocalDateTime.parse(dateTimeString, DB_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime format: " + dateTimeString, e);
        }
    }
}

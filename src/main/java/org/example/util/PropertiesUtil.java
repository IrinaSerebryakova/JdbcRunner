package org.example.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Утилитарный класс предоставляет доступ к свойствам конфигурации приложения.
 * Класс загружает свойства при первом обращении и хранит их в памяти.
 */
public final class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * Приватный конструктор для предотвращения создания экземпляров класса.
     */
    private PropertiesUtil() {
    }

    /**
     * Возвращает значение свойства по указанному ключу.
     *
     * @param key ключ свойства
     * @return значение свойства или null, если свойство не найдено
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Загружает свойства из файла application.properties.
     * Ожидает, что файл application.properties доступен в classpath.
     *
     * @throws RuntimeException если файл не найден или произошла ошибка при чтении файла
     */
    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
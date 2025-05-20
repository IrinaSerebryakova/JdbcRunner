package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключениями к базе данных.
 * Предоставляет метод для установления соединения с базой данных.
 * Класс является final и имеет приватный конструктор, что предотвращает
 * создание экземпляров класса и наследование от него.
 */
public final class ConnectionManager {
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String URL_KEY = "db.url";

    private ConnectionManager() {
    }

    /**
     * Открывает соединение с базой данных.
     * Параметры подключения (URL, имя пользователя и пароль) поставляет класс PropertiesUtil.
     *
     * @return объект Connection, представляющий установленное соединение с БД
     * @throws RuntimeException если происходит ошибка при установлении соединения
     */
    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
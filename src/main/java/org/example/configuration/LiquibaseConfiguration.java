package org.example.configuration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Класс для настройки и выполнения миграций базы данных с помощью Liquibase.
 * Позволяет применять изменения из changelog-файлов к указанному подключению к БД.
 */
public class LiquibaseConfiguration {
    private final String changelogPath;
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(LiquibaseConfiguration.class.getName());

    /**
     * Создает новый экземпляр конфигурации Liquibase.
     *
     * @param changelogPath путь к файлу changelog
     * @param connection    активное подключение к базе данных
     * @throws IllegalArgumentException если changelogPath или connection равны null
     */
    public LiquibaseConfiguration(String changelogPath, Connection connection) {
        this.changelogPath = changelogPath;
        this.connection = connection;
    }

    /**
     * Выполняет обновление базы данных, применяет все изменения из указанного changelog-файла.
     * После выполнения миграций подключение к БД закрывается.
     *
     * @throws RuntimeException если произошла ошибка во время выполнения миграций
     */
    public void updateDatabase() {
        try {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "liquibase/databaseChangeLog.xml",
                    new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось запустить обновление Liquibase", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
    }
}
package org.example.dao;

import org.example.model.Town;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) для работы с сущностью Town в базе данных.
 * Выполняет CRUD операции (create, read, update, delete) для городов.
 * Класс использует JDBC для взаимодействия с базой данных.
 */
public class TownDao {
    private static final Logger logger = Logger.getLogger(TownDao.class.getName());
    private final Connection connection;

    /**
     * Создает новый экземпляр TownDao с указанным подключением к базе данных.
     *
     * @param connection активное подключение к базе данных
     */
    public TownDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создает город в базе данных.
     *
     * @param town объект города для сохранения
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public void create(Town town) throws SQLException {
        String sql = "INSERT INTO town(town_name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, town.getTownName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    town.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Добавление города: " + town.getTownName());
    }

    /**
     * Находит город по его идентификатору.
     *
     * @param id идентификатор города для поиска
     * @return объект Town или null, если город не найден
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public Town read(Long id) throws SQLException {
        String sql = "SELECT * FROM town WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    getTown(resultSet);
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск города по ID: " + id);
        return null;
    }

    /**
     * Создает объект Town из текущей строки ResultSet.
     *
     * @param resultSet набор результатов SQL-запроса
     * @return объект Town
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    private Town getTown(ResultSet resultSet) throws SQLException {
        Town town = new Town();
        if (resultSet.next()) {
            town.setId(resultSet.getLong("id"));
            town.setTownName(resultSet.getString("town_name"));
        }
        return town;
    }

    /**
     * Возвращает список всех городов из базы данных.
     *
     * @return список объектов Town
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public List<Town> readAll() throws SQLException {
        List<Town> towns = new ArrayList<>();
        String sql = "SELECT * FROM town";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                towns.add(getTown(resultSet));
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Список городов: ");
        return towns;
    }

    /**
     * Обновляет данные города в базе данных.
     *
     * @param town объект Town с обновленными данными (должен содержать id и название)
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public void update(Town town) throws SQLException {
        String sql = "UPDATE town SET town_name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, town.getTownName());
            statement.setLong(2, town.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Обновление города: " + town.getTownName());
    }

    /**
     * Удаляет город из базы данных по его идентификатору.
     *
     * @param id идентификатор города для удаления
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM town WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Удаление города по ID: " + id);
    }
}

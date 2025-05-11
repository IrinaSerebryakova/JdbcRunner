package org.example.dao;

import org.example.model.Town;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TownDao {
    private static final Logger logger = Logger.getLogger(TownDao.class.getName());
    private final Connection connection;

    public TownDao(Connection connection) {
        this.connection = connection;
    }

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

    public Town read(Long id) throws SQLException {
        String sql = "SELECT * FROM town WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Town(
                            resultSet.getLong("id"),
                            resultSet.getString("town_name")
                    );
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск города по ID: " + id);
        return null;
    }

    public List<Town> readAll() throws SQLException {
        List<Town> towns = new ArrayList<>();
        String sql = "SELECT * FROM town";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                towns.add(new Town(
                        resultSet.getLong("id"),
                        resultSet.getString("town_name")
                ));
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Список городов: ");
        return towns;
    }

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

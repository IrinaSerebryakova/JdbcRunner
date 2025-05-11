package org.example.dao;

import org.example.model.Passenger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PassengerDao {
    private static final Logger logger = Logger.getLogger(PassengerDao.class.getName());
    private final Connection connection;

    public PassengerDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Passenger passenger) throws SQLException {
        String sql = "INSERT INTO passenger(first_name, last_name) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, passenger.getFirstName());
            statement.setString(2, passenger.getLastName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    passenger.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Добавление пассажира: " + passenger.getFirstName() + " " + passenger.getLastName());
    }

    public Passenger read(Long id) throws SQLException {
        String sql = "SELECT * FROM passenger WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Passenger(
                            resultSet.getObject("id", Long.class),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    );
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск пассажира по ID: " + id);
        return null;
    }

    public List<Passenger> readAll() throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM passenger";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                passengers.add(new Passenger(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                ));
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Список всех пассажиров: ");
        return passengers;
    }

    public void update(Passenger passenger) throws SQLException {
        String sql = "UPDATE passenger SET first_name = ?, last_name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, passenger.getFirstName());
            statement.setString(2, passenger.getLastName());
            statement.setLong(3, passenger.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Обновление пассажира: " + passenger.getFirstName() + " " + passenger.getLastName());
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM public.passenger WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Удаление пассажира по ID: " + id);
    }
}

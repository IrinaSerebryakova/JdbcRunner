package org.example.dao;

import org.example.model.Train;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TrainDao {
    private static final Logger logger = Logger.getLogger(TrainDao.class.getName());
    private final Connection connection;

    public TrainDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Train train) throws SQLException {
        String sql = "INSERT INTO train(number, train_name, town_from, time_out, town_to, time_in) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, train.getTrainName());
            statement.setString(2, train.getNumber());
            statement.setString(3, train.getTownFrom());
            statement.setTimestamp(4, Timestamp.valueOf(train.getTimeOut()));
            statement.setString(5, train.getTownTo());
            statement.setTimestamp(6, Timestamp.valueOf(train.getTimeIn()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    train.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Добавление поезда: " + train.getTrainName() + " " + train.getNumber());
    }

    public Train read(Long id) throws SQLException {
        String sql = "SELECT * FROM train WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Train(
                            resultSet.getLong("id"),
                            resultSet.getString("train_name"),
                            resultSet.getString("number"),
                            resultSet.getString("town_from"),
                            resultSet.getTimestamp("time_out").toLocalDateTime(),
                            resultSet.getString("town_to"),
                            resultSet.getTimestamp("time_in").toLocalDateTime());
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск поезда по ID: " + id);
        return null;
    }

    public List<Train> readAll() throws SQLException {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM train";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                trains.add(new Train(
                        resultSet.getLong("id"),
                        resultSet.getString("train_name"),
                        resultSet.getString("number"),
                        resultSet.getString("town_from"),
                        resultSet.getTimestamp("time_out").toLocalDateTime(),
                        resultSet.getString("town_to"),
                        resultSet.getTimestamp("time_in").toLocalDateTime()
                ));
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Список поездов: ");
        return trains;
    }

    public List<Train> findTrainsBetweenTime(LocalDateTime start, LocalDateTime end) throws SQLException {
        List<Train> listOfTrainsBetween = new ArrayList<>();
        String sql = "SELECT * FROM train WHERE time_out BETWEEN ? AND ? ORDER BY time_out";
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setObject(1, Timestamp.valueOf(start));
            prepareStatement.setObject(2, Timestamp.valueOf(end));

            try(ResultSet resultSet = prepareStatement.executeQuery()){
                while (resultSet.next()) {
                    listOfTrainsBetween.add(new Train(
                            resultSet.getLong("id"),
                            resultSet.getString("train_name"),
                            resultSet.getString("number"),
                            resultSet.getString("town_from"),
                            resultSet.getTimestamp("time_out").toLocalDateTime(),
                            resultSet.getString("town_to"),
                            resultSet.getTimestamp("time_in").toLocalDateTime()
                    ));
                }
            }
            } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск информации о поездах в период с " + start + "по" + end + ": ");
        return listOfTrainsBetween;
    }

    public void update(Train train) throws SQLException {
        String sql = """
                UPDATE train SET train_name = ?, number = ?, town_from = ?, time_out = ?, town_to = ?, time_in = ? WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, train.getTrainName());
            statement.setString(2, train.getNumber());
            statement.setString(3, train.getTownFrom());
            statement.setTimestamp(4, Timestamp.valueOf(train.getTimeOut()));
            statement.setString(5, train.getTownTo());
            statement.setTimestamp(6, Timestamp.valueOf(train.getTimeIn()));
            statement.setLong(7, train.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Обновление информации о поезде: " + train.getTrainName() + " " + train.getNumber());
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM train WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Удаление поезда по ID: " + id);
    }
}










package org.example.dao;

import org.example.model.Train;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.time.LocalDateTime;

/**
 * Data Access Object (DAO) для работы с сущностью Поезд в базе данных.
 * Предоставляет методы для выполнения CRUD операций (Create, Read, Update, Delete)
 */
public class TrainDao {
    private static final Logger logger = Logger.getLogger(TrainDao.class.getName());
    private final Connection connection;

    /**
     * Конструктор класса TrainDao.
     *
     * @param connection соединение с базой данных
     */
    public TrainDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создает новую запись о поезде в базе данных.
     *
     * @param train объект поезда для сохранения в базу данных
     * @throws SQLException если происходит ошибка при работе с базой данных
     */
    public void create(Train train) throws SQLException {
        String sql = "INSERT INTO train(train_name, number, town_from, time_out, town_to, time_in) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParameters(statement, train);
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

    /**
     * Устанавливает параметры PreparedStatement на основе данных объекта Train.
     *
     * @param statement PreparedStatement для установки параметров
     * @param train     объект поезда, из которого берутся данные
     * @throws SQLException если происходит ошибка при установке параметров
     */
    private void setStatementParameters(PreparedStatement statement, Train train) throws SQLException {
        statement.setString(1, train.getTrainName());
        statement.setString(2, train.getNumber());
        statement.setString(3, train.getTownFrom());
        statement.setTimestamp(4, Timestamp.valueOf(train.getTimeOut()));
        statement.setString(5, train.getTownTo());
        statement.setTimestamp(6, Timestamp.valueOf(train.getTimeIn()));
    }

    /**
     * Создает объект Train на основе данных из ResultSet.
     *
     * @param resultSet ResultSet с данными о поезде
     * @return объект Train, созданный на основе данных ResultSet
     * @throws SQLException если происходит ошибка при чтении данных из ResultSet
     */
    private Train getTrain(ResultSet resultSet) throws SQLException {
        Train train = new Train();
        if (resultSet.next()) {
            train.setId(resultSet.getLong("id"));
            train.setTrainName(resultSet.getString("train_name"));
            train.setNumber(resultSet.getString("number"));
            train.setTownFrom(resultSet.getString("town_from"));
            train.setTimeOut(resultSet.getTimestamp("time_out").toLocalDateTime());
            train.setTownTo(resultSet.getString("town_to"));
            train.setTimeIn(resultSet.getTimestamp("time_in").toLocalDateTime());
        }
        return train;
    }

    /**
     * Получает информацию о поезде по его идентификатору
     *
     * @param id идентификатор поезда
     * @return объект Train с информацией о поезде или null, если поезд не найден
     * @throws SQLException если происходит ошибка при работе с базой данных
     */
    public Train read(Long id) throws SQLException {
        String sql = "SELECT * FROM train WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    getTrain(resultSet);
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск поезда по ID: " + id);
        return null;
    }

    /**
     * Получает список всех поездов из базы данных.
     *
     * @return список объектов Train
     * @throws SQLException если происходит ошибка при работе с базой данных
     */
    public List<Train> readAll() throws SQLException {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM train";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                trains.add(getTrain(resultSet));
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Список поездов: ");
        return trains;
    }

    /**
     * Находит поезда, время отправления которых находится в указанном временном промежутке.
     *
     * @param start начало временного интервала
     * @param end   конец временного интервала
     * @return список поездов, отправляющихся в указанный период
     * @throws SQLException если происходит ошибка при работе с базой данных
     */
    public List<Train> findTrainsBetweenTime(LocalDateTime start, LocalDateTime end) throws SQLException {
        List<Train> listOfTrainsBetween = new ArrayList<>();
        String sql = "SELECT * FROM train WHERE time_out BETWEEN ? AND ? ORDER BY time_out";
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setObject(1, Timestamp.valueOf(start));
            prepareStatement.setObject(2, Timestamp.valueOf(end));

            try (ResultSet resultSet = prepareStatement.executeQuery()) {
                while (resultSet.next()) {
                    listOfTrainsBetween.add(getTrain(resultSet));
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск информации о поездах в период с " + start + "по" + end + ": ");
        return listOfTrainsBetween;
    }

    /**
     * Обновляет информацию о поезде в базе данных.
     *
     * @param train объект поезда с обновленными данными
     * @throws SQLException если происходит ошибка при работе с базой данных
     */
    public void update(Train train) throws SQLException {
        String sql = """
                UPDATE train SET train_name = ?, number = ?, town_from = ?, time_out = ?, town_to = ?, time_in = ? WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setStatementParameters(statement, train);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Обновление информации о поезде: " + train.getTrainName() + " " + train.getNumber());
    }

    /**
     * Удаляет информацию о поезде из базы данных по его идентификатору.
     *
     * @param id идентификатор удаляемого поезда
     * @throws SQLException если происходит ошибка при работе с базой данных
     */
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










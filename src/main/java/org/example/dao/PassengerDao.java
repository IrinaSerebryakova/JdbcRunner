package org.example.dao;

import org.example.model.Passenger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) для работы с сущностью Passenger в базе данных.
 * Выполняет CRUD операции (create, read, update, delete) для пассажиров.
 * Класс использует JDBC для взаимодействия с базой данных.
 */
public class PassengerDao {
    private static final Logger logger = Logger.getLogger(PassengerDao.class.getName());
    private final Connection connection;

    /**
     * Создает новый DAO с указанным подключением к базе данных.
     *
     * @param connection активное подключение к базе данных
     */
    public PassengerDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создает нового пассажира в базе данных.
     *
     * @param passenger объект пассажира для создания (должен содержать firstName и lastName)
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public void create(Passenger passenger) throws SQLException {
        String sql = "INSERT INTO passenger(first_name, last_name) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParameters(statement, passenger);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    passenger.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
        }
        logger.info("Добавление пассажира: " + passenger.getFirstName() + " " + passenger.getLastName());
    }

    /**
     * Устанавливает параметры SQL-запроса из объекта Passenger.
     *
     * @param statement подготовленное выражение SQL
     * @param passenger объект пассажира
     * @throws SQLException если произошла ошибка при установке параметров
     */
    private void setStatementParameters(PreparedStatement statement, Passenger passenger) throws SQLException {
        statement.setString(1, passenger.getFirstName());
        statement.setString(2, passenger.getLastName());
    }

    /**
     * Находит пассажира по id.
     *
     * @param id идентификатор пассажира
     * @return объект Passenger или null, если пассажир не найден
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public Passenger read(Long id) throws SQLException {
        String sql = "SELECT * FROM passenger WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                getPassenger(resultSet);
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Поиск пассажира по ID: " + id);
        return null;
    }

    /**
     * Создает объект Passenger из ResultSet.
     *
     * @param resultSet набор результатов SQL-запроса
     * @return объект Passenger или null, если в ResultSet нет данных
     * @throws SQLException если произошла ошибка при чтении данных
     */
    private Passenger getPassenger(ResultSet resultSet) throws SQLException {
        Passenger passenger = new Passenger();
        if (resultSet.next()) {
            passenger.setId(resultSet.getObject("id", Long.class));
            passenger.setFirstName(resultSet.getString("first_name"));
            passenger.setLastName(resultSet.getString("last_name"));
        }
        return passenger;
    }

    /**
     * Возвращает список всех пассажиров.
     *
     * @return список объектов Passenger
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public List<Passenger> readAll() throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM passenger";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                passengers.add(getPassenger(resultSet));
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Список всех пассажиров: ");
        return passengers;
    }

    /**
     * Обновляет данные пассажира в базе данных.
     *
     * @param passenger объект пассажира с обновленными данными (должен содержать id)
     * @throws SQLException             если произошла ошибка при работе с базой данных
     * @throws IllegalArgumentException если пассажир с указанным id не существует
     */
    public void update(Passenger passenger) throws SQLException {
        String sql = "UPDATE passenger SET first_name = ?, last_name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setStatementParameters(statement, passenger);
            statement.setLong(3, passenger.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage());
            throw ex;
        }
        logger.info("Обновление пассажира: " + passenger.getFirstName() + " " + passenger.getLastName());
    }

    /**
     * Удаляет пассажира по его идентификатору.
     *
     * @param id идентификатор пассажира для удаления
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
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

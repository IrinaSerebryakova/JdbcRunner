package org.example;

import org.example.configuration.LiquibaseConfiguration;
import org.example.dao.PassengerDao;
import org.example.dao.TrainDao;
import org.example.model.Passenger;
import org.example.model.Train;
import org.example.dao.TownDao;
import org.example.model.Town;
import org.example.util.ConnectionManager;
import org.example.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;

/**
 * Класс демонстрации работы приложения с базой данных.
 */
public class JdbcRunner {
    private final static String CHANGELOG_PATH = "db.changelogPath";

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectionManager.open();

            try (Connection liquibaseConnection = ConnectionManager.open()) {
                LiquibaseConfiguration liquibaseConfiguration = new LiquibaseConfiguration(CHANGELOG_PATH, connection);
                liquibaseConfiguration.updateDatabase();
                System.out.println("Созданы таблицы в базе данных: ");
                checkMetaData(liquibaseConnection);
                System.out.println("-------------------");
            } catch (SQLException e) {
                e.getMessage();
            }

            Statement statement = connection.createStatement();

            PassengerDao passengerDao = new PassengerDao(connection);

            Passenger newPassenger = new Passenger("Иван", "Иванов");
            passengerDao.create(newPassenger);
            System.out.println("Добавлен пассажир: " + newPassenger.getFirstName() + " " + newPassenger.getLastName());
            System.out.println("-------------------");

            Passenger foundPassenger = passengerDao.read(newPassenger.getId());
            System.out.println("Найден пассажир: " + foundPassenger.getFirstName() + " " + foundPassenger.getLastName());
            System.out.println("-------------------");

            System.out.println("Список всех пассажиров: \n" + passengerDao.readAll());
            System.out.println("-------------------");

            newPassenger.setFirstName("Петр");
            newPassenger.setLastName("Петров");
            passengerDao.update(newPassenger);
            System.out.print("После обновления: " + passengerDao.read(newPassenger.getId()));
            System.out.println("-------------------");

            passengerDao.delete(newPassenger.getId());
            System.out.println("После удаления: " + passengerDao.read(newPassenger.getId()));
            System.out.println("-------------------");

            TrainDao trainDao = new TrainDao(connection);

            Train newTrain = new Train("680A", "Орёл", "Санкт-Петербург", DateTimeUtils.parseDateTime("2025-05-09 06:55:00"),
                    "Москва", DateTimeUtils.parseDateTime("2025-05-09 10:55:00"));
            trainDao.create(newTrain);
            System.out.println("Добавлен поезд с ID " + newTrain.getId() + ": \n" + newTrain.getTrainName() + ", " + newTrain.getNumber() + ", " + newTrain.getTownFrom()
                    + " - " + newTrain.getTownTo() + ", время отбытия: " + newTrain.getTimeOut() + ", время прибытия: " + newTrain.getTimeIn());
            System.out.println("-------------------");

            Train trainById = trainDao.read(newTrain.getId());
            System.out.println("Информация по поезду с ID " + trainById.getId() + ": \n" + trainById.getNumber() + ", " + trainById.getTrainName() + ", " + trainById.getTownFrom() +
                    " - " + trainById.getTownTo() + ", время отбытия: " + trainById.getTimeOut() + ", время прибытия: " + trainById.getTimeIn());
            System.out.println("-------------------");

            System.out.println("Список поездов: \n" + trainDao.readAll());
            System.out.println("-------------------");

            System.out.println("Поиск информации о поездах в указанный период: \n" + trainDao.findTrainsBetweenTime(DateTimeUtils.parseDateTime("2025-05-09 06:50:00"), LocalDateTime.now()));
            System.out.println("-------------------");

            newTrain.setTrainName("Колибри");
            newTrain.setNumber("5У");
            newTrain.setTownFrom("Москва");
            newTrain.setTownTo("Санкт-Петербург");
            newTrain.setTimeOut(DateTimeUtils.parseDateTime("2025-05-10 07:55:00"));
            newTrain.setTimeIn(DateTimeUtils.parseDateTime("2025-05-10 11:55:00"));
            trainDao.update(newTrain);
            System.out.print("После обновления: \n" + trainDao.read(newTrain.getId()));
            System.out.println("-------------------");

            trainDao.delete(newTrain.getId());
            System.out.println("После удаления: \n" + trainDao.read(newTrain.getId()));
            System.out.println("-------------------");

            TownDao townDao = new TownDao(connection);

            Town newTown = new Town("Саранск");
            townDao.create(newTown);
            System.out.println("Добавлен город: " + newTown.getTownName());
            System.out.println("-------------------");

            Town townById = townDao.read(newTown.getId());
            System.out.println("Информация о городе по ID " + townById.getId() + ", " + townById.getTownName());
            System.out.println("-------------------");

            System.out.println("Список городов: " + townDao.readAll());
            System.out.println("-------------------");

            newTown.setTownName("Самара");
            townDao.update(newTown);
            System.out.print("После обновления: " + townDao.read(newTown.getId()));
            System.out.println("-------------------");

            townDao.delete(newTown.getId());
            System.out.println("После удаления: " + townDao.read(newTown.getId()));
            System.out.println("-------------------");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.getMessage();
            }
        }
    }

    /**
     * Выводит метаданные о структуре базы данных.
     * Метод получает и выводит список всех таблиц в схеме "public".
     *
     * @param connection активное соединение с базой данных
     * @throws SQLException если происходит ошибка при получении метаданных
     */
    public static void checkMetaData(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet schemas = null;
        ResultSet tables = null;
        try {
            schemas = metaData.getSchemas();
            while (schemas.next()) {
                String schema = schemas.getString("TABLE_SCHEM");
                if ("public".equals(schema)) {
                    tables = metaData.getTables(null, schema, "%", new String[]{"TABLE"});
                    while (tables.next()) {
                        System.out.println(tables.getString("TABLE_NAME"));
                    }
                }
            }
        } finally {
            if (tables != null) tables.close();
            if (schemas != null) schemas.close();
        }
    }
}

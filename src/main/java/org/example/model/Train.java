package org.example.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Train {
    private Long id;
    private String trainName;
    private String number;
    private String townFrom;
    private LocalDateTime timeOut;
    private String townTo;
    private LocalDateTime timeIn;

    public Train() {
    }

    public Train(String train_name, String number, String townFrom, LocalDateTime timeOut, String townTo, LocalDateTime timeIn) {
        this.trainName = train_name;
        this.number = number;
        this.townFrom = townFrom;
        this.timeOut = timeOut;
        this.townTo = townTo;
        this.timeIn = timeIn;
    }

    public Train(Long id, String trainName, String number, String townFrom, LocalDateTime timeOut, String townTo, LocalDateTime timeIn) {
        this.id = id;
        this.trainName = trainName;
        this.number = number;
        this.townFrom = townFrom;
        this.timeOut = timeOut;
        this.townTo = townTo;
        this.timeIn = timeIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTownFrom() {
        return townFrom;
    }

    public void setTownFrom(String townFrom) {
        this.townFrom = townFrom;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public String getTownTo() {
        return townTo;
    }

    public void setTownTo(String townTo) {
        this.townTo = townTo;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Train train)) return false;
        return Objects.equals(id, train.id) && Objects.equals(trainName, train.trainName) && Objects.equals(number, train.number) && Objects.equals(townFrom, train.townFrom) && Objects.equals(timeOut, train.timeOut) && Objects.equals(townTo, train.townTo) && Objects.equals(timeIn, train.timeIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainName, number, townFrom, timeOut, townTo, timeIn);
    }

    @Override
    public String toString() {
        return "Train{" +
                "id= " + id +
                ", train_name='" + trainName +
                ", number='" + number +
                ", town_from='" + townFrom +
                ", time_out='" + timeOut +
                ", town_to='" + townTo +
                ", time_in='" + timeIn +
                "'}";
    }
}

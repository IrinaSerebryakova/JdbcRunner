package org.example.model;

import java.util.Objects;

public class Town {
    private Long id;
    private String townName;

    public Town() {
    }

    public Town(String townName) {
        this.townName = townName;
    }

    public Town(Long id, String townName) {
        this.id = id;
        this.townName = townName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Town town)) return false;
        return Objects.equals(id, town.id) && Objects.equals(townName, town.townName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, townName);
    }

    @Override
    public String toString() {
        return "Town{id=" + id + ", townName='" + townName + "'}";
    }
}

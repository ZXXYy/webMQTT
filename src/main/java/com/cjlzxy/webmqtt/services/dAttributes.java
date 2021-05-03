package com.cjlzxy.webmqtt.services;

import java.util.Objects;

public class dAttributes {
    private String id;
    private String name;
    private String type;
    private String value;
    private int key;


    public dAttributes(String id, String name, String type, String value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        key = hashCode();
    }

    public dAttributes(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        key = hashCode();
    }

    @Override
    public String toString() {
        return "dAttributes{" +
                "identifier='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        dAttributes that = (dAttributes) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, value);
    }
}

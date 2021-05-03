package com.cjlzxy.webmqtt.services;

public class dAttributes {
    private String identifier;
    private String name;
    private String type;
    private String value;

    public dAttributes(String identifier, String name, String type, String value) {
        this.identifier = identifier;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public dAttributes(String identifier, String name, String type) {
        this.identifier = identifier;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "dAttributes{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

package com.cjlzxy.webmqtt.services;

import java.util.ArrayList;

public class DeviceAttribute {
    private ArrayList<dAttributes> a = new ArrayList<dAttributes>();

    public void attrAdd(dAttributes dA) {
        a.add(dA);
    }

    public void clearData() {
        a.clear();
    }

    public ArrayList<dAttributes> getA() {
        return a;
    }

    @Override
    public String toString() {
        return "DeviceAttribute{" +
                "a=" + a +
                '}';
    }
}

package com.cjlzxy.webmqtt.services;

import java.util.ArrayList;

public class DeviceAttribute {
    private ArrayList<dAttributes> a;

    public void attrAdd(dAttributes dA) {
        a.add(dA);
    }

    @Override
    public String toString() {
        return "DeviceAttribute{" +
                "a=" + a +
                '}';
    }
}

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

    public String removeById(String id) {
        for(int i=0; i<a.size(); ++i) {
            if(a.get(i).getId().equals(id)) {
                a.remove(i);
                return "Success";
            }
        }
        return "Fail";
    }

    public String modifyByKey(int key, String id, String name, String type) {
        for(int i=0; i<a.size(); ++i) {
            if(a.get(i).getKey() == key) {
                a.get(i).setId(id);
                a.get(i).setName(name);
                a.get(i).setType(type);
                return "Success";
            }
        }
        return "Fail";
    }

    public int getIndexById(String id) {
        for(int i=0; i<a.size(); ++i) {
            if(a.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "DeviceAttribute{" +
                "a=" + a +
                '}';
    }

}

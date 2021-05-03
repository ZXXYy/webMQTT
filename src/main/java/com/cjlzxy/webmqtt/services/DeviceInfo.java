package com.cjlzxy.webmqtt.services;

public class DeviceInfo {
    private String port;
    private String productKey;
    private String productSecret;
    private String deviceName;
    private String deviceSecret;
    private String broker;
    private boolean connected = false;

    public void setParams(String port, String pk, String ps, String dn, String ds) {
        this.port = port;
        this.productKey = pk;
        this.productSecret = ps;
        this.deviceName = dn;
        this.deviceSecret = ds;
        this.broker = "ssl://" + pk + ".iot-as-mqtt.cn-shanghai.aliyuncs.com" + ":" + port;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "port='" + port + '\'' +
                ", productKey='" + productKey + '\'' +
                ", productSecret='" + productSecret + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceSecret='" + deviceSecret + '\'' +
                ", broker='" + broker + '\'' +
                '}';
    }

    public String getPort() {
        return port;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public String getBroker() {
        return broker;
    }
}

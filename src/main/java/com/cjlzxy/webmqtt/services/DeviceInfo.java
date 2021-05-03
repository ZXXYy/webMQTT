package com.cjlzxy.webmqtt.services;

public class DeviceInfo {
    private String port;
    private String productKey;
    private String productSecret;
    private String deviceName;
    private String deviceSecret;
    private String broker;

    public void setParams(String port, String pk, String ps, String dn, String ds) {
        this.port = port;
        this.productKey = pk;
        this.productSecret = ps;
        this.deviceName = dn;
        this.deviceSecret = ds;
        this.broker = "ssl://" + pk + ".iot-as-mqtt.cn-shanghai.aliyuncs.com" + ":" + port;
    }

}

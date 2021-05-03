package com.cjlzxy.webmqtt.controller;

import com.cjlzxy.webmqtt.services.DeviceInfo;
import com.cjlzxy.webmqtt.services.MqttSign;
import org.springframework.web.bind.annotation.*;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.eclipse.paho.mqttv5.common.packet.UserProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Mqtt5PostPropertyMessageListener implements IMqttMessageListener {

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("reply topic  : " + topic);
        System.out.println("reply payload: " + message.toString());
    }
}

@RestController
public class TestController {

    /*
     * GET, POST, PUT, DELETE
     *
     * /user?id=1
     * restful 风格: /user/1
     * @RequestMapping四种访问方式都可以
     * @GetMapping 只支持get
     * @PostMapping, @PutMapping, @DeleteMapping
     * @RequestMapping(value = "/user/1", method = RequestMethod.GET)
     * Response Code: 405 Method Not allowed
     *
     * */
    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    DeviceInfo deviceInfo;
    MqttSign sign;
    MqttClient sampleClient;

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connect(@RequestBody Map<String, String> params) {
        //接入物联网平台的域名。
        String port = params.get("port");
        String dn = params.get("deviceName");
        String ds = params.get("deviceSecret");
        String pk = params.get("productKey");
        String ps = params.get("productSecret");
        String broker = "ssl://" + pk + ".iot-as-mqtt.cn-shanghai.aliyuncs.com" + ":" + port;

        System.out.println("dn: " + dn);

        deviceInfo = new com.cjlzxy.webmqtt.services.DeviceInfo();
        deviceInfo.setParams(port, pk, ps, dn, ds);

        sign = new MqttSign();
        sign.calculate(pk, dn, ds);

        System.out.println("username: " + sign.getUsername());
        System.out.println("password: " + sign.getPassword());
        System.out.println("clientid: " + sign.getClientid());

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            //Paho Mqtt 客户端
            sampleClient = new MqttClient(broker, sign.getClientid(), persistence);

            //Paho Mqtt 连接参数
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            connOpts.setKeepAliveInterval(180);
            connOpts.setUserName(sign.getUsername());
            connOpts.setPassword(sign.getPassword().getBytes());
            //Paho Mqtt5 设备上报QoS1&QoS2消息流控
            connOpts.setReceiveMaximum(5);
            //Paho Mqtt5 设备接收的最大报文长度
            connOpts.setMaximumPacketSize(1024L);

            IMqttToken iMqttToken = sampleClient.connectWithResult(connOpts);
            System.out.println("broker: " + broker + " Connected");
            return "Success";

            //Paho Mqtt5 服务端可用能力列表
//            System.out.println("broker available ability, retain is available: " + iMqttToken.getResponseProperties().isRetainAvailable());
//            System.out.println("broker available ability, shared subscription is available: " + iMqttToken.getResponseProperties().isSharedSubscriptionAvailable());

        } catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
            return "Fail";
        }
    }

    @RequestMapping(value = "/topic", method = RequestMethod.POST)
    public String subtopic(@RequestBody Map<String, String> params) {
        String tid = params.get("id");
        System.out.println(tid);
        String topicReply = "";
        if(tid.equals("/sys/${ProductKey}/${deviceName}/thing/event/property/post")){
            topicReply = "/sys/" + deviceInfo.getProductKey() + "/" + deviceInfo.getDeviceName() + "/thing/event/property/post";
        }
        else if(tid.equals("/sys/${ProductKey}/${deviceName}/thing/event/property/post_reply")){
            topicReply = "/sys/" + deviceInfo.getProductKey() + "/" + deviceInfo.getDeviceName() + "/thing/event/property/post_reply";
        }
        else if(tid.equals("/sys/${ProductKey}/${deviceName}/thing/service/property/set")) {
            topicReply = "/sys/" + deviceInfo.getProductKey() + "/" + deviceInfo.getDeviceName() + "/thing/service/property/set";
        }

        if(subscribe(topicReply)) {
            return "Success";
        }
        return "Fail";
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    public String disconnect() {
        try {
            sampleClient.disconnect();
            System.out.println("Disconnected");
            return "Success";
        } catch (MqttException e) {
            e.printStackTrace();
            return "Fail";
        }
    }



    public boolean subscribe(String topicReply) {
        MqttSubscription[] subscriptions = new MqttSubscription[] {
                new MqttSubscription(topicReply)};
        IMqttMessageListener[] subscriptionListeners = new IMqttMessageListener[] {
                new Mqtt5PostPropertyMessageListener()};
        try {
            sampleClient.subscribe(subscriptions, subscriptionListeners);
            System.out.println("subscribe: " + topicReply);
            return true;
        } catch (MqttException e) {
            System.out.println("subscribe error");
            e.printStackTrace();
            return false;
        }
    }




}

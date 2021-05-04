package com.cjlzxy.webmqtt.controller;

import ch.qos.logback.core.util.CachingDateFormatter;
import com.cjlzxy.webmqtt.services.DeviceAttribute;
import com.cjlzxy.webmqtt.services.DeviceInfo;
import com.cjlzxy.webmqtt.services.MqttSign;
import com.cjlzxy.webmqtt.services.dAttributes;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.Attribute;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Mqtt5PostPropertyMessageListener implements IMqttMessageListener {
    @Override

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("reply topic  : " + topic);
        System.out.println("reply payload: " + message.toString());
        // is set method
        int index = topic.lastIndexOf('/');
        String isSet = topic.substring(index+1);
        if(!isSet.equals("set")) {
            return;
        }

        // get params
        JsonObject jsonObject = JsonParser.parseString(message.toString()).getAsJsonObject();
        JsonObject paramsJson = jsonObject.get("params").getAsJsonObject();

        Map<String, String> map = new Gson().fromJson(paramsJson, new TypeToken<Map<String, String>>() {}.getType());
        //System.out.println(map.size());

        // set deviceAttribute
        for(Map.Entry<String, String> entry: map.entrySet()) {
            String id = entry.getKey();
            String value = entry.getValue();
//            System.out.println("id:" + id);
//            System.out.println("value:" + value);

            for (int i = 0; i < TestController.deviceAttribute.getA().size(); ++i) {
                //System.out.println(TestController.deviceAttribute.getA().get(i).getId());
                if (TestController.deviceAttribute.getA().get(i).getId().equals(id)) {
                    TestController.deviceAttribute.getA().get(i).setValue(value);
                    SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    TestController.deviceAttribute.getA().get(i).setDate(tempDate.format(new java.util.Date()));
                }
            }
        }
        System.out.println("Value update success!");
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

    public static DeviceInfo deviceInfo;
    private MqttSign sign;
    private MqttClient sampleClient;
    private IMqttToken iMqttToken;
    public static DeviceAttribute deviceAttribute = new DeviceAttribute();
    private SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private IMqttMessageListener[] subscriptionListeners;

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

            iMqttToken = sampleClient.connectWithResult(connOpts);
            deviceInfo.setConnected(true);
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
            deviceInfo.setConnected(false);
            return "Fail";
        }
    }

    @GetMapping("/data")
    public String keepData() {
        Gson gs = new Gson();
        return gs.toJson(deviceInfo);
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

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importJSON(@RequestBody MultipartFile file) {
        deviceAttribute.clearData();
        try (InputStream stream = file.getInputStream()) {
            // convert stream to string
            String contents = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            // print string
            System.out.println(contents);

            JsonObject jsonObject = JsonParser.parseString(contents).getAsJsonObject();
            JsonArray props = jsonObject.getAsJsonArray("properties");

            for (int i = 0; i < props.size(); i++) {
                JsonObject tjo = props.get(i).getAsJsonObject();
                JsonElement identifier = tjo.get("identifier");
                JsonElement name = tjo.get("name");
                JsonElement type = tjo.getAsJsonObject("dataType").get("type");

                dAttributes dA = new dAttributes(identifier.getAsString(), name.getAsString(), type.getAsString());

                deviceAttribute.attrAdd(dA);
            }
            return "Success";
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Fail";
        }
    }

    @GetMapping(value = "/attribute")
    public String returnAttribute() {
        Gson gs = new Gson();
        // System.out.println(gs.toJson(deviceAttribute));
        return gs.toJson(deviceAttribute);
    }

    @RequestMapping(value = "/attribute/delete", method = RequestMethod.POST)
    public String attributeDelete(@RequestBody Map<String, String> params) {
        String identifier = params.get("id");
        return deviceAttribute.removeById(identifier);
    }

    @RequestMapping(value = "/attribute/add", method = RequestMethod.POST)
    public String attributeAdd(@RequestBody Map<String, String> params) {
        String identifier = params.get("id");
        String name = params.get("name");
        String type = params.get("type");

        dAttributes da = new dAttributes(identifier, name, type);
        deviceAttribute.attrAdd(da);
        return "Success";
    }

    @RequestMapping(value = "/attribute/edit", method = RequestMethod.POST)
    public String attributeEdit(@RequestBody Map<String, String> params) {
        int key = Integer.parseInt(params.get("key"));
        String id = params.get("id");
        String name = params.get("name");
        String type = params.get("type");

        return deviceAttribute.modifyByKey(key, id, name, type);
    }

    @RequestMapping(value = "/model", method = RequestMethod.POST)
    public String uploadData(@RequestBody Map<String, String> params) {
        String identifier = params.get("id");
        String value = params.get("value");
        //Paho Mqtt 消息发布
        return upload(identifier, value);
    }

    @RequestMapping(value = "/timer", method = RequestMethod.POST)
    public String UploadByTime(@RequestBody Map<String, String> params) {
        int interval = Integer.parseInt(params.get("interval"));
        String id = params.get("id");
        String ck = params.get("checked");

        int index = deviceAttribute.getIndexById(id);
        if(index==-1) return "Fail";
        if(ck.equals("false")) {
            deviceAttribute.getA().get(index).setInterval(0);
            return "Fail";
        }
        String value = deviceAttribute.getA().get(index).getValue();
        String type = deviceAttribute.getA().get(index).getType();
        deviceAttribute.getA().get(index).setChecked(ck);
        deviceAttribute.getA().get(index).setInterval(interval);

        if(type.equals("double") || type.equals("float")) {
            Float val = Float.parseFloat(value);
            val += (int) (Math.random()*10-5);
            value = String.valueOf(val);
        }
        else if(type.equals("int32") || type.equals("int")) {
            int val = Integer.parseInt(value);
            val += (int) (Math.random()*10-5);
            value = String.valueOf(val);
        }
        int cnt = 0;
        int maxcnt = 50;

        while(cnt<maxcnt) {
            String res = upload(id, value);
            if(res.equals("Fail")) break;
            cnt++;
            try {
                Thread.currentThread().sleep(1000*interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        deviceAttribute.getA().get(index).setChecked("false");
        deviceAttribute.getA().get(index).setInterval(0);
        if(cnt>=maxcnt) return "Success";
        return "Fail";
    }

    public boolean subscribe(String topicReply) {
        MqttSubscription[] subscriptions = new MqttSubscription[] {
                new MqttSubscription(topicReply)};
        subscriptionListeners = new IMqttMessageListener[] {
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

    public String upload(String identifier, String value) {
        String topic = "/sys/" + deviceInfo.getProductKey() + "/" + deviceInfo.getDeviceName() + "/thing/event/property/post";
        String content = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"" + identifier + "\":" + value + "}}";
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(0);
        try {
            sampleClient.publish(topic, message);
            System.out.println("publish: " + content);
            ArrayList<dAttributes> dAList = deviceAttribute.getA();
            for (int i=0; i<dAList.size(); ++i) {
                dAttributes da = dAList.get(i);
                if(da.getId().equals(identifier)){
                    da.setValue(value);
                    da.setDate(tempDate.format(new java.util.Date()));
                    break;
                }
            }
            return "Success";
        } catch (MqttException e) {
            e.printStackTrace();
            return "Fail";
        }
    }




}

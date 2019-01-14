package com.giotec.tesis;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Utils {
    private static String user = "Raspberry";
    private static String password = "yrrebpsaR";
    private static String topicSimple1= "/Alarm1/Alarm1/Alarm1";
    private static String topicSimple2= "/Alarm2/Alarm2/Alarm2";
    private static String topicSimple3= "/Alarm3/Alarm3/Alarm3";
    private static String topicSimple4= "/Alarm4/Alarm4/Alarm4";
    private static MqttClient clientLocal ;
    private static MqttClient clientGlobal ;

    public static boolean DetecTemperatureAlarm(double val1,double val2){
        return ( val1>30 && val2>val1) ? true: false;
    }
    public static boolean DetecCO2Alarm(double val1,double val2){
        return ( val1>460 && val2>val1) ? true: false;
    }
    public static boolean DetecUVAlarm(double val1,double val2){
        return ( val1>13 && val2>val1) ? true: false;
    }
    public static boolean DetecO3Alarm(double val1,double val2){
        return ( val1>250 && val2>val1) ? true: false;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static void ConnectClient_Local(String serverURI){
        try {
            clientLocal = new MqttClient(serverURI, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setCleanSession(false);
            options.setUserName(getUser());
            options.setPassword(getPassword().toCharArray());
            clientLocal.connect(options);
        } catch (MqttException e) {
            System.out.println("Excepcion Connectando Local "+e.toString());
        }
    }

    public static void PublicarLocal(String nameAlerta, String mensaje){
        MqttMessage messageMQTT = new MqttMessage();
        messageMQTT.setPayload(mensaje.getBytes());
        if(clientLocal!=null){
            try {
                clientLocal.publish(nameAlerta, messageMQTT);
            } catch (MqttException e) {
                System.out.println("Excepcion PublicarLocal  "+e.toString());
                try {
                    if(!clientLocal.isConnected()) clientLocal.reconnect();
                    clientLocal.publish(nameAlerta, messageMQTT);
                } catch (MqttException e1) {
                    e1.printStackTrace();
                }
            }
        }else {
            ConnectClient_Local("tcp://localhost");
            try {
                clientLocal.publish(nameAlerta, messageMQTT);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public static void ConnectClient_Global(String serverURI) {
        try {
            clientGlobal = new MqttClient(serverURI, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setCleanSession(false);
            options.setUserName(getUser());
            options.setPassword(getPassword().toCharArray());
            clientGlobal.connect(options);
        } catch (MqttException e) {
            System.out.println("Excepcion Connectando Local "+e.toString());
        }

    }

    public static void PublicarGlobal(String nameAlerta, String mensaje){
        MqttMessage messageMQTT = new MqttMessage();
        messageMQTT.setPayload(mensaje.getBytes());
        if(clientGlobal!=null){
            try {
                clientGlobal.publish(nameAlerta, messageMQTT);
            } catch (MqttException e) {
                System.out.println("Excepcion PublicarGlobal "+e.toString());
                try {
                    if(!clientGlobal.isConnected())clientGlobal.reconnect();
                    clientGlobal.publish(nameAlerta, messageMQTT);
                } catch (MqttException e1) {
                    e1.printStackTrace();
                }
            }
        }else {
            Utils.ConnectClient_Global("tcp://190.119.192.232");
            try {
                clientGlobal.publish(nameAlerta, messageMQTT);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTopicSimple1() {
        return topicSimple1;
    }

    public static String getTopicSimple2() {
        return topicSimple2;
    }

    public static String getTopicSimple4() {
        return topicSimple4;
    }
}

package com.giotec.tesis;

import org.eclipse.paho.client.mqttv3.*;

public class Utils {
    private static String user = "Raspberry";
    private static String password = "yrrebpsaR";
    private static String topicSimple1= "Alarm1/Alarm1/Alarm1";
    private static String topicSimple2= "Alarm2/Alarm2/Alarm2";
    private static String topicSimple3= "Alarm3/Alarm3/Alarm3";
    private static String topicSimple4= "Alarm4/Alarm4/Alarm4";
    private static int maxihilos = 50;
    private static int maxiAlarms = 200;
    private static MqttAsyncClient clientLocal[] = new MqttAsyncClient[50];
    private static MqttAsyncClient clientGlobal[] = new MqttAsyncClient[50];

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


    public static int getMaxihilos() {
        return maxihilos;
    }

    public static int getMaxiAlarms() {
        return maxiAlarms;
    }

    public static void ConnectClient_Local(int hilo, String serverURI){
        try {
            clientLocal[hilo] = new MqttAsyncClient(serverURI, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setCleanSession(true);
            options.setUserName(getUser());
            options.setPassword(getPassword().toCharArray());
            options.setAutomaticReconnect(true);
            options.setMaxInflight(getMaxiAlarms());
            clientLocal[hilo].connect(options);
            System.out.println("MQTT clientLocal conectando");
        } catch (MqttException e) {
            System.out.println("Excepcion Connectando Local "+e.toString());
        }
    }

    public static void PublicarLocal(int hilo, String nameAlerta, String mensaje){
        MqttMessage messageMQTT = new MqttMessage();
        messageMQTT.setPayload(mensaje.getBytes());
        //System.out.println("publicando PublicarLocal "+mensaje);
        if(clientLocal[hilo]!=null){
            //System.out.println("publicando"+clientLocal[hilo].isConnected()+" PublicarLocal "+mensaje);
            try {
                clientLocal[hilo].publish(nameAlerta, messageMQTT);
            } catch (MqttException e) {
                System.out.println("Excepcion PublicarLocal  "+e.toString());
                try {
                    if(!clientLocal[hilo].isConnected()) Thread.sleep(700+(int)Math.random()*400);
                    clientLocal[hilo].publish(nameAlerta, messageMQTT);
                } catch (MqttException e1) {
                    System.out.println("Excepcion MqttException  "+e1.toString());
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    System.out.println("Excepcion InterruptedException  "+e1.toString());
                    e1.printStackTrace();
                }
            }
        }else {
            System.out.println("Excepcion Conectando!!  ");
            ConnectClient_Local(hilo,"tcp://localhost");
            try {
                Thread.sleep(700+(int)Math.random()*400);
                clientLocal[hilo].publish(nameAlerta, messageMQTT);
                //System.out.println("Excepcion publish1  ");
            } catch (MqttException e) {
                System.out.println("Excepcion MqttException PublicarLocal  "+e.toString());
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Excepcion InterruptedException PublicarLocal  "+e.toString());
                e.printStackTrace();
            }
        }
    }

    public static void ConnectClient_Global(int hilo,String serverURI){
        try {
            clientGlobal[hilo] = new MqttAsyncClient(serverURI, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setCleanSession(true);
            options.setUserName(getUser());
            options.setPassword(getPassword().toCharArray());
            options.setAutomaticReconnect(true);
            options.setMaxInflight(getMaxiAlarms());
            clientGlobal[hilo].connect(options);
            System.out.println("MQTT cliente global conectando");
        } catch (MqttException e) {
            System.out.println("Excepcion Connectando Global "+e.toString());
        }
    }

    public static void PublicarGlobal(int hilo, String nameAlerta, String mensaje){
        MqttMessage messageMQTT = new MqttMessage();
        messageMQTT.setPayload(mensaje.getBytes());
        //System.out.println("publicando PublicarLocal "+mensaje);
        if(clientGlobal[hilo]!=null){
            //System.out.println("publicando"+clientGlobal[hilo].isConnected()+" PublicarGlobal "+mensaje);
            try {
                clientGlobal[hilo].publish(nameAlerta, messageMQTT);
            } catch (MqttException e) {
                System.out.println("Excepcion PublicarGlobal  "+e.toString());
                try {
                    if(!clientGlobal[hilo].isConnected()) Thread.sleep(700+(int)Math.random()*400);
                    clientGlobal[hilo].publish(nameAlerta, messageMQTT);
                } catch (MqttException e1) {
                    System.out.println("Excepcion MqttException  "+e1.toString());
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    System.out.println("Excepcion InterruptedException  "+e1.toString());
                    e1.printStackTrace();
                }
            }
        }else {
            //System.out.println("Excepcion Conectando!!  ");
            ConnectClient_Global(hilo,"tcp://190.119.192.232");
            try {
                Thread.sleep(700+(int)Math.random()*400);
                clientGlobal[hilo].publish(nameAlerta, messageMQTT);
                //System.out.println("Excepcion publish1  ");
            } catch (MqttException e) {
                System.out.println("Excepcion MqttException PublicarGlobal "+e.toString());
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Excepcion InterruptedException PublicarGlobal  "+e.toString());
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

package com.giotec.tesis;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class Alarma {
    private String topic;
    private static int hilo=0;
    //private static Semaphore semaphore = new Semaphore(230);

    public Alarma() {
    }

    public Alarma(String topic, EventSensed eventSensed,EventSensed eventSensed2) {
        //Publicar(topic,getJson(eventSensed,eventSensed2),"tcp://localhost");
        int mhilo=getCurrentHilo();
        String mjson=getJson(eventSensed,eventSensed2);
        Utils.PublicarLocal(mhilo,topic,mjson);
        Utils.PublicarGlobal(mhilo,topic,mjson);
	//Publicar(topic,mjson,"tcp://localhost");
        //Publicar(topic,mjson,"tcp://190.119.192.232");
    }

    public synchronized int getCurrentHilo(){
        hilo=(hilo+1)%Utils.getMaxihilos();
        return hilo;
    }

    public String getJson(EventSensed eventSensed,EventSensed eventSensed2){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timestamp1",eventSensed.getTimestamp());
            jsonObject.put("timestamp2",eventSensed2.getTimestamp());
            jsonObject.put("Hora", ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()));
            jsonObject.put("origintopic",eventSensed.getTopic());
            jsonObject.put("val1",eventSensed.getValue());
            jsonObject.put("Hpaso21",eventSensed.getHpaso2());
            jsonObject.put("Hpaso22",eventSensed2.getHpaso2());
        } catch (JSONException e) {  System.out.println("Error formando el JSON de alarma " +e.toString());  }
        return jsonObject.toString();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    /*public void Publicar(String topic,String mensaje,String server){
        MqttClient client = null;
        try {
            client = new MqttClient(server, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setUserName(Utils.getUser());
            options.setPassword(Utils.getPassword().toCharArray());

            MqttMessage messageMQTT = new MqttMessage();
            messageMQTT.setPayload(mensaje.getBytes());
            client.connect(options);
            //Aca iria el semaforo
            semaphore.acquire();
            client.publish(topic, messageMQTT);
            semaphore.release();
            client.disconnect();
        } catch (MqttException e) {
            Publicar(topic,mensaje,server);
            System.out.println("Excepcion Alerta "+e.toString());
        } catch (InterruptedException e) {
            semaphore.release();
            e.printStackTrace();
        }
    }*/
}


package com.giotec.tesis;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Alarma {
    private String topic;
    public Alarma() {
    }

    public Alarma(String topic, EventSensed eventSensed,EventSensed eventSensed2) {
        Utils.PublicarLocal(topic,getJson(eventSensed,eventSensed2));
        //Utils.PublicarGlobal(topic,getJson());
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
}

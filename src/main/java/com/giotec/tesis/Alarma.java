package com.giotec.tesis;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Alarma {

    String timestamp1;
    Double val1, val2;
    String origintopic;

    public Alarma() {
    }

    public Alarma(String timestamp1, Double val1, Double val2, String origintopic, String topic) {
        this.timestamp1 = timestamp1;
        this.val1 = val1;
        this.val2 = val2;
        this.origintopic = origintopic;
        Utils.PublicarLocal(topic,getJson());
        //Utils.PublicarGlobal(topic,getJson());
    }

    public String getJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timestamp1",timestamp1);
            jsonObject.put("Hora", ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()));
            jsonObject.put("origintopic",origintopic);
            jsonObject.put("val1",val1);
            jsonObject.put("val2",val2);
        } catch (JSONException e) {  System.out.println("Error formando el JSON de alarma " +e.toString());  }
        return jsonObject.toString();
    }
}

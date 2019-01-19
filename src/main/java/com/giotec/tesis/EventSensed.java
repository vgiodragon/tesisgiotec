package com.giotec.tesis;

public class EventSensed {

    private double value;
    private String timestamp;
    private String Hpaso2;
    private String topic;
    private String parameter;

    public EventSensed() {
    }

    public EventSensed(double value, String timestamp, String topic, String parameter) {
        this.value = value;
        this.timestamp = timestamp;
        this.topic = topic;
        this.parameter = parameter;
    }


    public double getValue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTopic() {
        return topic;
    }

    public String getParameter() {
        return parameter;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHpaso2() {
        return Hpaso2;
    }

    public void setHpaso2(String hpaso2) {
        Hpaso2 = hpaso2;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}

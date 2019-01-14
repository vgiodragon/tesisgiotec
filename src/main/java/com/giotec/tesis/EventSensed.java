package com.giotec.tesis;

public class EventSensed {

    private double value;
    private String timestamp;
    private String topic;
    private String parameter;

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
}

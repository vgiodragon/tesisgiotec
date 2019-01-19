package com.giotec.tesis.Data;

import com.giotec.tesis.EventSensed;

public class TemperatureSensed extends EventSensed {
    public TemperatureSensed(double value, String timestamp, String topic, String parameter) {
        super(value, timestamp, topic, parameter);
    }
}

package com.giotec.tesis;

import com.giotec.tesis.Data.TemperatureSensed;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class CEPMonitor {

    public static final int timeSentSec = 20;

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();


        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);


        DataStream<EventSensed> sensadoInput0 = env.addSource(new MiMQTTSource())
                .keyBy((event) -> event.getTopic())
                ;
        //AfterMatchSkipStrategy skipStrategy = AfterMatchSkipStrategy.skipPastLastEvent();
        Pattern<EventSensed,?> TempSI = Pattern.<EventSensed>
                begin("FirstSensadoEvent")//,skipStrategy)
                .subtype(TemperatureSensed.class)
                .where(new SimpleCondition<TemperatureSensed>() {
                    @Override
                    public boolean filter(TemperatureSensed temperatureSensed) throws Exception {
                        return temperatureSensed.getValue()>30;
                    }
                }).next("SecondSensadoEvent")
                .subtype(TemperatureSensed.class)
                .where(new SimpleCondition<TemperatureSensed>() {
                    @Override
                    public boolean filter(TemperatureSensed eventSensed) throws Exception {
                        return eventSensed.getValue()>30;
                    }
                })
                .within(Time.seconds(timeSentSec*3/2));

        PatternStream<EventSensed> patternStreamClose = CEP.pattern(
                sensadoInput0
                , TempSI);
        DataStream<Alarma> alertsClose = patternStreamClose.select(new PatternSelectFunction<EventSensed, Alarma>() {
            @Override
            public Alarma select(Map<String, List<EventSensed>> map) throws Exception {
                EventSensed sensedEvent= map.get("FirstSensadoEvent").get(0);
                EventSensed sensedEvent2 = map.get("SecondSensadoEvent").get(0);
                //System.out.println("Alarma generada " + sensedEvent.getTopic());
                //String timestamp1, Double val1, Double val2, String origintopic, String topic)
                return new Alarma(Utils.getTopicSimple1(), sensedEvent,sensedEvent2);
            }
        }).setParallelism(75);
        System.out.println("Ready!");
        //Utils.ConnectClient_Local("tcp://localhost");

        env.execute("GioTec SAC - Tesis - Smartcity ");

    }

    public static class MiMQTTSource implements SourceFunction<EventSensed> {
        private String user = "CTIC-SMARTCITY";
        private String password = "YTICTRAMS-CITC";
        private volatile boolean isRunning = true;
        @Override
        public void run(SourceContext<EventSensed> ctx) throws Exception {
            MqttClient client;
            try {
                client = new MqttClient("tcp://localhost", MqttClient.generateClientId());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
                options.setUserName(user);
                options.setPassword(password.toCharArray());
                options.setCleanSession(false);
                options.setAutomaticReconnect(true);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable throwable) {
                        System.out.println("Desconectado MQTT del Socket! "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    }
                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                        //System.out.println("topic: "+topic);
                        String re_type[] = topic.split("/");
                        try {
                            JSONObject jsonObject = new JSONObject(new String(mqttMessage.getPayload()));
                            String timestamp = jsonObject.getString("timestamp");
                            Double value= jsonObject.getDouble("value");
                            if(re_type.length>2 && !timestamp.equals("")){
                                ctx.collect(new TemperatureSensed(value,timestamp,topic,re_type[2]));
                            }
                        }catch (JSONException e) {
                            System.out.println("No es JSON:"+new String(mqttMessage.getPayload())+"_"+e.toString() );
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    }
                });
                client.connect(options);
                client.subscribe("+/+/temperature");
            } catch (MqttException e) {
                System.out.println("Expcecion hilo "+e.toString());
            }
            System.out.println("Conectando.... ");
            while (!Thread.currentThread().isInterrupted() && isRunning) {
                try {
                    sleep(Integer.MAX_VALUE);
                }catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

    public static class MiMQTTSource2 extends RichParallelSourceFunction<EventSensed> {
        private String user = "CTIC-SMARTCITY";
        private String password = "YTICTRAMS-CITC";
        private volatile boolean isRunning = true;

        @Override
        public void run(SourceContext<EventSensed> sourceContext) throws Exception {

            MqttClient client;
            try {
                client = new MqttClient("tcp://localhost", MqttClient.generateClientId());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
                options.setUserName(user);
                options.setPassword(password.toCharArray());
                options.setCleanSession(false);
                options.setAutomaticReconnect(true);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable throwable) {
                        System.out.println("Desconectado MQTT del Socket! "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    }
                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                        //System.out.println("topic: "+topic);
                        String re_type[] = topic.split("/");
                        try {
                            JSONObject jsonObject = new JSONObject(new String(mqttMessage.getPayload()));
                            String timestamp = jsonObject.getString("timestamp");
                            Double value= jsonObject.getDouble("value");
                            if(re_type.length>2 && !timestamp.equals("")){
                                sourceContext.collect(new EventSensed(value,timestamp,topic,re_type[2]));
                            }
                        }catch (JSONException e) {
                            System.out.println("No es JSON:"+new String(mqttMessage.getPayload())+"_"+e.toString() );
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    }
                });
                client.connect(options);
                client.subscribe("+/+/temperature");
            } catch (MqttException e) {
                System.out.println("Expcecion hilo "+e.toString());
            }
            System.out.println("Conectando.... "+getRuntimeContext().getIndexOfThisSubtask());
            while (!Thread.currentThread().isInterrupted() && isRunning) {
                try {
                    sleep(Integer.MAX_VALUE);
                }catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

    public static long StringDatetoLong(String mdate){
        Date date=new Date();
        try {                                 //20180631000250 //yyyy:MMddHHmmss
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mdate);
        } catch (ParseException e) {
            System.out.println("Error parsing DATE "+e.toString());
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp.getTime();
    }

}
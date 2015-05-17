package com.client.thera.theroid.domain;

import org.joda.time.DateTime;
import java.io.Serializable;
import java.util.UUID;


/**
 * Created by Fer on 17/03/2015.
 */
public class Message implements Serializable{

    public static class Content{
        private int temperature;
        private int health;
        private int voltage;

        public int getTemperature() {
            return temperature;
        }
        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getVoltage() {
            return voltage;
        }
        public void setVoltage(int voltage) {
            this.voltage = voltage;
        }

        public int getHealth() {
            return health;
        }
        public void setHealth(int health) {
            this.health = health;
        }
    }

    private int _id;
    private UUID deviceID;
    private String status;//Pending - Sent - NotSent TODO: Convert it into an 'enum'
    private DateTime eventTime;
    private Content content;

    //Old
    public Message(int temperature,int health, int voltage){
        setContent(new Content());
        getContent().setTemperature(temperature);
        getContent().setHealth(health);
        getContent().setVoltage(voltage);
        setStatus("Pending");
    }
    //New (DateTime for the moment of creation)
    public Message(int temperature,int health, int voltage, DateTime eventTime){
        setContent(new Content());
        getContent().setTemperature(temperature);
        getContent().setHealth(health);
        getContent().setVoltage(voltage);
        setStatus("Pending");
        setEventTime(eventTime);
    }


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }
    public DateTime getEventTime(){return this.eventTime;}

    public UUID getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(UUID deviceID) {
        this.deviceID = deviceID;
    }

    public Content getContent() {
        return content;
    }
    public void setContent(Content content) {
        this.content = content;
    }

}

package com.client.thera.theroid.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


/**
 * Created by Fer on 17/03/2015.
 */
//TODO: eliminado status, se necesita para ListActivity
// _id is not parsed to JSON because it doesn't have a Getter method
public class Message implements Serializable{

    public enum Status{PENDING, SENT_OK, SENT_ERROR, TIMEOUT}

    public static class Content{
        private int temperature;
        private int health;
        private int voltage;

        @JsonSerialize(using=ToStringSerializer.class)
        public int getTemperature() {
            return temperature;
        }
        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }
        @JsonSerialize(using=ToStringSerializer.class)
        public int getVoltage() {
            return voltage;
        }
        public void setVoltage(int voltage) {
            this.voltage = voltage;
        }
        @JsonSerialize(using=ToStringSerializer.class)
        public int getHealth() {
            return health;
        }
        public void setHealth(int health) {
            this.health = health;
        }
    }

    private int _id; //Used internally by the ListActivity
    private UUID deviceID;
    private Date eventTime;
    private Content content;
    private Status status;

    //Old
    public Message(int temperature,int health, int voltage){
        setContent(new Content());
        getContent().setTemperature(temperature);
        getContent().setHealth(health);
        getContent().setVoltage(voltage);
        setStatus(Status.PENDING);
    }
    //New (DateTime for the moment of creation)
    public Message(int temperature,int health, int voltage, Date eventTime){
        setContent(new Content());
        getContent().setTemperature(temperature);
        getContent().setHealth(health);
        getContent().setVoltage(voltage);
        setEventTime(eventTime);
        setStatus(Status.PENDING);
    }



    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
    public Date getEventTime(){return this.eventTime;}

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
    @JsonIgnore
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}

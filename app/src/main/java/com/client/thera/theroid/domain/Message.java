package com.client.thera.theroid.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fer on 17/03/2015.
 */
public class Message implements Serializable{

    private int _id;
    private int temperature;
    private int health;
    private int voltage;
    private String status;//Pending - Sent - NotSent
    private Date eventTime;

    public Message(int temperature,int health, int voltage){
        setTemperature(temperature);
        setHealth(health);
        setVoltage(voltage);
        setStatus("Pending");
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Date getEventTime(){return this.eventTime;}


}

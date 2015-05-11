package com.client.thera.theroid.domain;

import org.joda.time.DateTime;
import java.io.Serializable;


/**
 * Created by Fer on 17/03/2015.
 */
public class Message implements Serializable{

    private int _id;
    private int temperature;
    private int health;
    private int voltage;
    private String status;//Pending - Sent - NotSent
    private DateTime eventTime;

    //Old
    public Message(int temperature,int health, int voltage){
        setTemperature(temperature);
        setHealth(health);
        setVoltage(voltage);
        setStatus("Pending");
    }
    //New (DateTime form moment of creation)
    public Message(int temperature,int health, int voltage, DateTime eventTime){
        setTemperature(temperature);
        setHealth(health);
        setVoltage(voltage);
        setStatus("Pending");
        setEventTime(eventTime);
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

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    public DateTime getEventTime(){return this.eventTime;}


}

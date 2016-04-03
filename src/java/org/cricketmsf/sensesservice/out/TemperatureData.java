/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cricketmsf.sensesservice.out;

import java.util.Date;

/**
 *
 * @author greg
 */
public class TemperatureData {
    
    private Date date;
    private Double temperature;
    private String sensorName;

    
    public String toPostParams(){
        return "sn="+getSensorName()+"&d="+getDate()+"&t="+getTemperature().toString();
    }
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the temperature
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    /**
     * @return the sensorName
     */
    public String getSensorName() {
        return sensorName;
    }

    /**
     * @param sensorName the sensorName to set
     */
    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }
    
}

/*
 * Copyright 2016 Grzegorz Skorupa <g.skorupa at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    private String clientIp;

    public TemperatureData(){
    }
    
    public TemperatureData(String sensor, String clientIp, String date, String temperature){
        this.clientIp=clientIp;
        sensorName=sensor;
        this.date=new Date();
        this.temperature = Double.valueOf(temperature);
    }
    
    /*public String toPostParams(){
        return "sn="+getSensorName()+"&d="+getDate()+"&t="+getTemperature().toString();
    }
    */
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

    /**
     * @return the clientIp
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @param clientIp the clientIp to set
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
}

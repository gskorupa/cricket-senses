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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.cricketmsf.sensesstation.out.TemperatureReaderException;

/**
 *
 * @author greg
 */
public class SensorData {

    private Date date;
    private Double value;
    private String sensorName;
    private String stationIp;
    private String stationName;
    private String unitName;

    public SensorData() {
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
    
    public void setDate(String date) throws TemperatureReaderException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:kk:mm:ss Z");
        try {
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            throw new TemperatureReaderException(
                    TemperatureReaderException.DATE_FORMAT,
                    "wrong date format: "+date);
        }
    }

    /**
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }
    
    public void setTemperature(String temperature) throws NumberFormatException{
        this.value = Double.valueOf(temperature);
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
     * @return the stationIp
     */
    public String getStationIp() {
        return stationIp;
    }

    /**
     * @param stationIp the stationIp to set
     */
    public void setStationIp(String stationIp) {
        this.stationIp = stationIp;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:kk:mm:ss Z");
        StringBuffer sb = new StringBuffer();
        sb.append(getStationName());
        sb.append(",");
        sb.append(getSensorName());
        sb.append(",");
        sb.append(sdf.format(getDate()));
        sb.append(",");
        sb.append(getValue());
        sb.append(",");
        sb.append(getUnitName());
        return sb.toString();
    }

    /**
     * @return the stationName
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * @param stationName the stationName to set
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    /**
     * @return the unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName the unitName to set
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

}

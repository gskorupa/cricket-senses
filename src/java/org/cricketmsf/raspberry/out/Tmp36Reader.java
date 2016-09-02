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
package org.cricketmsf.raspberry.out;

import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.cricketmsf.Adapter;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.out.OutboundAdapter;
import org.cricketmsf.sensesservice.out.SensorData;
import org.cricketmsf.raspberry.GpioConfiguration;
import org.cricketmsf.sensesstation.out.TemperatureReaderException;
import org.cricketmsf.sensesstation.out.TemperatureReaderIface;

/**
 *
 * @author greg
 */
public class Tmp36Reader extends OutboundAdapter implements TemperatureReaderIface, Adapter {

    HashMap<String, GpioConfiguration> sensors;

    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        //apiURL = properties.get("url");
        //System.out.println("url: " + apiURL);
        String sensorsConfig=properties.get("sensors");
        configureSensors(sensorsConfig);
        System.out.println("sensors: "+sensorsConfig);
    }

    /**
     * Stores sensor configurations for later use
     * 
     * @param configString must be in format "sensorName1,SPI_no1,PINno1;sensorName2,SPIno2,PINno2"
     * example: "tempReader1,0,0;tempReader2,0,1"
     */
    @Override
    public void configureSensors(String configString) {
        String[] sensorsConf = configString.split(";");
        String[] params;
        sensors = new HashMap<>();
        for (int i = 0; i < sensorsConf.length; i++) {
            params = sensorsConf[i].split(",");
            try {
                sensors.put(params[0], new GpioConfiguration(Integer.parseInt(params[1]), Integer.parseInt(params[2])));
            } catch (NumberFormatException e) {
                Kernel.handle(Event.logWarning("TemperatureReader", "Malformed sensor configuration"));
            }
        }
    }

    /**
     * Read analog data from all configured sensors
     * 
     * @param stationName
     * @return list of SensorData objects
     */
    @Override
    public ArrayList<SensorData> readAll(String stationName) {
        ArrayList<SensorData> list = new ArrayList<>();
        sensors.keySet().stream().forEach((key) -> {
            try {
                list.add(read(stationName, key, sensors.get(key)));
            } catch (TemperatureReaderException e) {
                Kernel.handle(Event.logWarning(this.getClass().getSimpleName(), e.getMessage()));
            }
        });
        return list;
    }

    /**
     * Read analog value from configured sensor
     * 
     * @param stationName
     * @param sensorName
     * @param config
     * @return SensorData object
     * @throws TemperatureReaderException 
     */
    @Override
    public SensorData read(String stationName, String sensorName, GpioConfiguration config) throws TemperatureReaderException {
        MCP3008GpioProvider provider;
        double analogValue = -1;
        try {
            provider = new MCP3008GpioProvider(config.spi);
            analogValue = provider.getValue(MCP3008Pin.createAnalogInputPin(config.pin));
        } catch (IOException e) {
            throw new TemperatureReaderException(TemperatureReaderException.IOEXCEPTION, "");
        }
        SensorData td = new SensorData();
        td.setStationName(stationName);
        td.setDate(new Date());
        td.setSensorName(sensorName);
        td.setTemperature(""+recalculate(analogValue));
        td.setUnitName("Celsius");
        Kernel.handle(Event.logInfo("TemperatureReader", "sensor " + sensorName));
        return td;
    }
    
    private double recalculate(double value){
        return ((value * 3300 / 1024) - 500) / 10;
    }

}

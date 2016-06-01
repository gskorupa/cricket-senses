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
package org.cricketmsf.sensesstation.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import org.cricketmsf.Adapter;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.out.OutboundAdapter;
import org.cricketmsf.sensesservice.out.SensorData;

/**
 *
 * @author greg
 */
public class MockTemperatureReader extends OutboundAdapter implements TemperatureReaderIface, Adapter {

    HashMap<String, String> sensors;

    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        //apiURL = properties.get("url");
        //System.out.println("url: " + apiURL);
        configureSensors();
    }

    @Override
    public void configureSensors() {
        sensors = new HashMap<>();
        sensors.put("t1", "");
        sensors.put("t2", "");
    }

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

    public SensorData read(String stationName, String sensorName, String config) throws TemperatureReaderException {
        Random rand = new Random();
        SensorData sed = new SensorData();
        sed.setDate(new Date());
        sed.setStationName(stationName);
        sed.setSensorName(sensorName);
        sed.setValue(Double.valueOf("20.0")*rand.nextDouble());
        sed.setUnitName("C");
        Kernel.handle(Event.logInfo("MockTemperatureReader", "sensor " + sensorName));
        return sed;
    }

}

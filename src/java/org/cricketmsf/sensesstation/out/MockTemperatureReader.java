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
import org.cricketmsf.Adapter;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.out.OutboundAdapter;
import org.cricketmsf.sensesservice.out.TemperatureData;

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
        sensors.put("s1", "");
    }

    @Override
    public ArrayList<TemperatureData> readAll() {
        ArrayList<TemperatureData> list = new ArrayList<>();
        sensors.keySet().stream().forEach((key) -> {
            try {
                list.add(read(key, sensors.get(key)));
            } catch (TemperatureReaderException e) {
                Kernel.handle(Event.logWarning(this.getClass().getSimpleName(), e.getMessage()));
            }
        });
        return list;
    }

    @Override
    public TemperatureData read(String sensorName, String config) throws TemperatureReaderException {
        TemperatureData td = new TemperatureData();
        td.setDate(new Date());
        td.setSensorName("s1");
        td.setTemperature(Double.valueOf("21.5"));
        Kernel.handle(Event.logInfo("MockTemperatureReader", "sensor " + sensorName));
        return td;
    }

}

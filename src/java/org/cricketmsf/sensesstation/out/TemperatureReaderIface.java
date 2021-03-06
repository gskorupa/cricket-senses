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
import org.cricketmsf.raspberry.GpioConfiguration;
import org.cricketmsf.sensesservice.out.SensorData;

/**
 *
 * @author greg
 */
public interface TemperatureReaderIface {

    public void configureSensors(String config);
    public SensorData read(String stationName, String sensorName, GpioConfiguration configuration) throws TemperatureReaderException;
    public ArrayList<SensorData> readAll(String stationName);
}

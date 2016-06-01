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
package org.cricketmsf.sensesstation;

import java.util.ArrayList;
import org.cricketmsf.Event;
import org.cricketmsf.EventHook;
import org.cricketmsf.HttpAdapterHook;
import org.cricketmsf.Kernel;
import org.cricketmsf.RequestObject;
import org.cricketmsf.in.http.HttpAdapter;
import org.cricketmsf.in.http.HttpAdapterIface;
import org.cricketmsf.in.http.StandardResult;
import org.cricketmsf.in.scheduler.SchedulerIface;
import org.cricketmsf.out.db.KeyValueCacheAdapterIface;
import org.cricketmsf.out.log.LoggerAdapterIface;
import org.cricketmsf.sensesservice.out.SensorData;
import org.cricketmsf.out.OutbondHttpAdapterIface;
import org.cricketmsf.sensesstation.out.TemperatureReaderIface;

/**
 * EchoService
 *
 * @author greg
 */
public class Station extends Kernel {

    // adapterClasses
    LoggerAdapterIface logAdapter = null;
    HttpAdapterIface httpAdapter = null;
    KeyValueCacheAdapterIface localDatabase = null;
    SchedulerIface scheduler = null;
    // 
    OutbondHttpAdapterIface storeClient = null;
    TemperatureReaderIface temperatureReader = null;

    @Override
    public void getAdapters() {
        logAdapter = (LoggerAdapterIface) getRegistered("LoggerAdapterIface");
        httpAdapter = (HttpAdapterIface) getRegistered("EchoHttpAdapterIface");
        localDatabase = (KeyValueCacheAdapterIface) getRegistered("KeyValueCacheAdapterIface");
        scheduler = (SchedulerIface) getRegistered("SchedulerIface");
        storeClient = (OutbondHttpAdapterIface) getRegistered("StoreClient");
        temperatureReader = (TemperatureReaderIface) getRegistered("TemperatureReader");
    }

    /**
     * Initialize the scheduling system by creating initial event
     */
    @Override
    public void runInitTasks() {
        if (!scheduler.isRestored()) {
            String delay = "+10s";
            scheduler.handleEvent(new Event("SensesStation", "CheckSensors", "", delay, ""));
        }
    }

    /**
     * Reads temperature data from sensors using temperatureReader adapter
     *
     * @param event
     */
    @EventHook(eventCategory = "CheckSensors")
    public void checkSensors(Event event) {

        // if adapter is not defined then stop. There wont'be more iterations
        if (temperatureReader == null) {
            handle(Event.logWarning("SensesStore", "temperature reader not configured"));
            return;
        }
        // if event must be delayed then scheduler should handle
        if (scheduler.handleEvent(event)) {
            return;
        }

        // read data
        ArrayList<SensorData> data = temperatureReader.readAll("stationName");

        // create event to store data
        Event ev = new Event("SensesStation", "SendData", "", null, data);
        handle(ev);

        // schedule next run
        handle(new Event("SensesStation", "CheckSensors", "", "+10s", ""));
    }

    @EventHook(eventCategory = "SendData")
    public void sendToStore(Event ev) {
        // if adapter is not defined then stop. There wont'be more iterations
        if (storeClient == null) {
            handle(Event.logWarning("SensesStore", "store client not configured"));
            return;
        }
        // if event must be delayed then scheduler should handle
        if (scheduler.handleEvent(ev)) {
            return;
        }
        // process event
        ArrayList<SensorData> list = (ArrayList<SensorData>) ev.getPayload();
        storeClient.setContentType("text/csv");
        storeClient.setRequestMethod("POST");
        StandardResult result = (StandardResult)storeClient.send(list);
        // in case of error we should store the data in local database
        // to be able to resend it later
        if (result.getCode() != HttpAdapter.SC_CREATED) {
            localDatabase.put(""+ev.getId(), list);
            handle(Event.logWarning("SensesStation", "client error: "+result.getCode() +
                    " "+ result.getPayload()
            + " storing the data locally"));
        } else {
            handle(Event.logInfo("SensesStation", "data sended"));
        }
    }

    @Override
    public void runOnce() {
        super.runOnce();
        System.out.println("Hello from Senses Station");
    }

    @HttpAdapterHook(adapterName = "EchoHttpAdapterIface", requestMethod = "GET")
    public Object doGetEcho(Event requestEvent) {
        return sendEcho((RequestObject) requestEvent.getPayload());
    }

    @EventHook(eventCategory = "LOG")
    public void logEvent(Event event) {
        logAdapter.log(event);
    }

    @EventHook(eventCategory = "HTTPLOG")
    public void logHttpEvent(Event event) {
        logAdapter.log(event);
    }

    /**
     * Handles all event types not handled by other hooks
     *
     * @param event
     */
    @EventHook(eventCategory = "*")
    public void processEvent(Event event) {
        if (event.getTimePoint() != null) {
            scheduler.handleEvent(event);
        } else {
            handle(Event.logInfo("Service", "don't know how to handle event category: " + event.getCategory() + " :" + event.getPayload().toString()));
        }
    }

    public Object sendEcho(RequestObject request) {

        StandardResult r = new StandardResult();
        return r;
    }

}

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
package org.cricketmsf.sensesservice;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import org.cricketmsf.Event;
import org.cricketmsf.EventHook;
import org.cricketmsf.HttpAdapterHook;
import org.cricketmsf.Kernel;
import org.cricketmsf.RequestObject;
import java.util.HashMap;
import java.util.Map;
import org.cricketmsf.in.http.EchoHttpAdapterIface;
import org.cricketmsf.in.http.HtmlGenAdapterIface;
import org.cricketmsf.in.http.HttpAdapter;
import org.cricketmsf.in.http.StandardResult;
import org.cricketmsf.in.scheduler.SchedulerIface;
import org.cricketmsf.out.db.KeyValueCacheAdapterIface;
import org.cricketmsf.out.html.HtmlReaderAdapterIface;
import org.cricketmsf.out.log.LoggerAdapterIface;
import org.cricketmsf.sensesservice.out.TemperatureData;

/**
 * EchoService
 *
 * @author greg
 */
public class Service extends Kernel {

    // adapterClasses
    LoggerAdapterIface logAdapter = null;
    EchoHttpAdapterIface httpAdapter = null;
    KeyValueCacheAdapterIface dataStore = null;
    SchedulerIface scheduler = null;
    HtmlGenAdapterIface htmlAdapter = null;
    HtmlReaderAdapterIface htmlReaderAdapter = null;
    // 
    EchoHttpAdapterIface dataApi = null;

    @Override
    public void getAdapters() {
        logAdapter = (LoggerAdapterIface) getRegistered("LoggerAdapterIface");
        httpAdapter = (EchoHttpAdapterIface) getRegistered("EchoHttpAdapterIface");
        dataStore = (KeyValueCacheAdapterIface) getRegistered("KeyValueCacheAdapterIface");
        scheduler = (SchedulerIface) getRegistered("SchedulerIface");
        htmlAdapter = (HtmlGenAdapterIface) getRegistered("HtmlGenAdapterIface");
        htmlReaderAdapter = (HtmlReaderAdapterIface) getRegistered("HtmlReaderAdapterIface");
        dataApi = (EchoHttpAdapterIface) getRegistered("DataAPI");
    }

    /**
     * Initialize the scheduling system by creating initial event
     */
    @Override
    public void runInitTasks() {
        // not required
    }

    @Override
    public void runOnce() {
        super.runOnce();
        System.out.println("Hello from Senses Service");
    }

    @HttpAdapterHook(adapterName = "HtmlGenAdapterIface", requestMethod = "GET")
    public Object doGet(Event event) {
        RequestObject request = (RequestObject) event.getPayload();
        return htmlReaderAdapter.getFile(request);
    }

    @HttpAdapterHook(adapterName = "EchoHttpAdapterIface", requestMethod = "GET")
    public Object doGetEcho(Event requestEvent) {
        return sendEcho((RequestObject) requestEvent.getPayload());
    }

    /**
     * You can test this with:
     * curl -X POST -d @filename.txt http://service:port/api --header "Content-Type:text/xml"
     * @param requestEvent
     * @return 
     */
    @HttpAdapterHook(adapterName = "DataAPI", requestMethod = "POST")
    public Object doStoreData(Event requestEvent) {
        // log event
        handle(Event.logInfo("SensesStore", "message received"));
        
        StandardResult result = new StandardResult();
        System.out.println((String)requestEvent.getRequestParameter("data"));
        try {
            ArrayList<TemperatureData> tDataList = 
                    parsePostData(
                            (String)requestEvent.getRequestParameter("data"),
                            ((RequestObject)requestEvent.getPayload()).clientIp);
            for (int i = 0; i < tDataList.size(); i++) {
                dataStore.put("" + requestEvent.getId(), tDataList.get(i));
            }
            result.setCode(HttpAdapter.SC_CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpAdapter.SC_INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @HttpAdapterHook(adapterName = "DataAPI", requestMethod = "GET")
    public Object doGetData(Event requestEvent) {
        
                
        //get search query param
        String query = requestEvent.getRequestParameter("query");
        if(query!=null){
            handle(Event.logWarning("ServiceStation", "query parameter not implemented"));
        }
        StandardResult result = new StandardResult();
        try {
            RequestObject request = (RequestObject) requestEvent.getPayload();
            Map data = dataStore.getAll();
            String format = request.headers.getFirst("Accept").toLowerCase();
            switch (format) {
                case "text/csv":
                    result.setData(toArray(data));
                    break;
                default:
                    result.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpAdapter.SC_INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * Create List of Lists from Map. Used when text/csv response is required
     * @param data
     * @return List of Lists
     */
    ArrayList toArray(Map<String, TemperatureData> data) {
        ArrayList list = new ArrayList();
        ArrayList row = new ArrayList();
        row.add("sensor");
        row.add("date");
        row.add("temperature");
        list.add(row);
        TemperatureData d;
        for (Map.Entry<String, TemperatureData> tempEntry : data.entrySet()) {
            row = new ArrayList();
            //System.out.println(tempEntry.getValue().getSensorName());
            try {
                d = tempEntry.getValue();
                row.add(d.getSensorName());
                row.add(d.getDate());
                row.add(d.getTemperature());
                list.add(row);
            } catch (ClassCastException e) {
                //db entry is not TemperatureData
            }
        }
        return list;
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

    private ArrayList<TemperatureData> parsePostData(String postData, String clientIp) {
        ArrayList<TemperatureData> list = new ArrayList<>();
        BufferedReader bufReader = new BufferedReader(new StringReader(postData));
        String line = null;
        String[] arr;
        TemperatureData td;
        try {
            while ((line = bufReader.readLine()) != null) {
                line = line.trim();
                if (!(line.isEmpty() || line.startsWith("#"))) {
                    arr = line.split(",");
                    td = new TemperatureData(arr[0], clientIp, arr[1], arr[2]);
                    list.add(td);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Object sendEcho(RequestObject request) {

        StandardResult r = new StandardResult();
        r.setCode(HttpAdapter.SC_OK);
        if (!httpAdapter.isSilent()) {
            HashMap<String, Object> data = new HashMap<String, Object>(request.parameters);
            data.put("service.uuid", getUuid().toString());
            data.put("request.method", request.method);
            data.put("request.pathExt", request.pathExt);
            data.put("echo.counter", dataStore.get("counter"));
            if (data.containsKey("error")) {
                int errCode = HttpAdapter.SC_INTERNAL_SERVER_ERROR;
                try {
                    errCode = Integer.parseInt((String) data.get("error"));
                } catch (Exception e) {
                }
                r.setCode(errCode);
                data.put("error", "error forced by request");
            }
            r.setData(data);
        }
        return r;
    }

}

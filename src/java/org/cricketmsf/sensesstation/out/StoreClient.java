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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.cricketmsf.Adapter;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.out.OutboundAdapter;
import org.cricketmsf.sensesservice.out.StoreResult;
import org.cricketmsf.sensesservice.out.TemperatureData;

/**
 *
 * @author greg
 */
public class StoreClient extends OutboundAdapter implements StoreClientIface, Adapter {

    private final String USER_AGENT = "Mozilla/5.0";
    private String apiURL;

    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        apiURL = properties.get("url");
        System.out.println("url: " + apiURL);
    }

    @Override
    public StoreResult sendData(ArrayList<TemperatureData> data) {
        
        //create csv text
        String postParameters = translateToCsv(data);
        StoreResult result=new StoreResult();
        result.setCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
        result.setContent("");
        result.setContentLength(0);
        result.setResponseTime(-1);
        try {

            Kernel.handle(Event.logFine(this.getClass().getSimpleName(), "sending to " + apiURL));

            long startPoint = System.currentTimeMillis();
            URL obj = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "text/csv");
            con.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(con.getOutputStream());
            out.print(postParameters);
            out.close();
            con.connect();
            result.setCode(con.getResponseCode());
            System.out.println(result.getCode());
            result.setResponseTime(System.currentTimeMillis() - startPoint);
            if (result.getCode() == HttpURLConnection.HTTP_CREATED) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result.setContentLength(response.length());
                result.setContent(response.toString());
            } else {
                result.setContentLength(0);
                result.setContent("");
            }
        } catch (Exception e) {
            Kernel.handle(Event.logWarning("StoreClient", e.getMessage()));
            result.setCode(500);
            return result;
        }
        return result;
    }

    private String translateToCsv(ArrayList<TemperatureData> data){
        // we must translate Array of TemperatureData to Array of Arrays
        //return CsvFormatter.getInstance().format(data);
        
        //or maybe simpler
        TemperatureData td;
        StringBuffer sb=new StringBuffer("#sensor,datetime,temperature\r\n");
        for(int i=0; i<data.size(); i++){
            td=data.get(i);
            sb.append(td.getSensorName());
            sb.append(",");
            sb.append(td.getDate());
            sb.append(",");
            sb.append(td.getTemperature());
            sb.append("\r\n");
        }
        return sb.toString();
    }
}

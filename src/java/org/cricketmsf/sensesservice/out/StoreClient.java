/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cricketmsf.sensesservice.out;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.cricketmsf.Adapter;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.out.OutboundAdapter;

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
    public StoreResult sendData(String postParameters) {
        StoreResult result=new StoreResult();
        result.setCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
        result.setContent("");
        result.setContentLength(0);
        result.setResponseTime(-1);
        try {

            Kernel.getInstance().handleEvent(new Event(this.getClass().getSimpleName(), Event.CATEGORY_LOG, Event.LOG_FINE, null, "sending to " + apiURL));

            long startPoint = System.currentTimeMillis();
            URL obj = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(con.getOutputStream());
            out.print(postParameters);
            out.close();
            con.connect();
            result.setCode(con.getResponseCode());
            result.setResponseTime(System.currentTimeMillis() - startPoint);
            if (result.getCode() == HttpURLConnection.HTTP_OK) { // success
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
            //e.printStackTrace();
            result.setCode(500);
            return result;
        }
        return result;
    }

}

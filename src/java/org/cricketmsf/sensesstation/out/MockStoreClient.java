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

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import org.cricketmsf.Adapter;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.in.http.StandardResult;
import org.cricketmsf.out.OutboundAdapter;
import org.cricketmsf.sensesservice.out.SensorData;

/**
 *
 * @author greg
 */
public class MockStoreClient extends OutboundAdapter implements StoreClientIface, Adapter {

    private final String USER_AGENT = "Mozilla/5.0";
    private String apiURL;

    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        apiURL = properties.get("url");
        System.out.println("url: " + apiURL);
    }

    @Override
    public StandardResult sendData(ArrayList<SensorData> data) {
        long startPoint = System.currentTimeMillis();
        StandardResult result=new StandardResult();
        result.setCode(HttpURLConnection.HTTP_OK);
        //result.setContent("");
        //result.setContentLength(0);
        //result.setResponseTime(System.currentTimeMillis() - startPoint);
        Kernel.handle(Event.logInfo("MockStoreClient", data.toString()));
        return result;
    }

}

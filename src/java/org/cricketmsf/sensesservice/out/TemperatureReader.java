/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cricketmsf.sensesservice.out;

import java.util.Date;
import java.util.HashMap;
import org.cricketmsf.Adapter;
import org.cricketmsf.out.OutboundAdapter;

/**
 *
 * @author greg
 */
public class TemperatureReader extends OutboundAdapter implements TemperatureReaderIface, Adapter {

    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        //apiURL = properties.get("url");
        //System.out.println("url: " + apiURL);
    }

    @Override
    public TemperatureData read(){
        TemperatureData td=new TemperatureData();
        td.setDate(new Date());
        td.setSensorName("s1");
        td.setTemperature(Double.valueOf("21.5"));
        return td;
    }

}

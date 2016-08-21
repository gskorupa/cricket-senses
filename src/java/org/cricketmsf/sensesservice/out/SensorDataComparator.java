/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cricketmsf.sensesservice.out;

import org.cricketmsf.out.db.ComparatorIface;


/**
 *
 * @author greg
 */
public class SensorDataComparator implements ComparatorIface{
    
    @Override
    public int compare(Object value, Object pattern){
        try{
            if(((SensorData)value).getStationName().equalsIgnoreCase(((SensorData)pattern).getStationName())){
                return 0;
            }
        }catch(Exception e){}
        return -2;
    }
    
}

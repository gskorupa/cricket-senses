
package org.cricketmsf.sensesstation.out;

import java.util.ArrayList;
import org.cricketmsf.in.http.Result;
import org.cricketmsf.sensesservice.out.SensorData;

/**
 *
 * @author greg
 */
public interface StoreClientIface {

    public Result sendData(ArrayList<SensorData> data);
    
}

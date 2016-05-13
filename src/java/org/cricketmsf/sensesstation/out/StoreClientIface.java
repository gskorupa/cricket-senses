
package org.cricketmsf.sensesstation.out;

import java.util.ArrayList;
import org.cricketmsf.in.http.Result;
import org.cricketmsf.sensesservice.out.TemperatureData;

/**
 *
 * @author greg
 */
public interface StoreClientIface {

    public Result sendData(ArrayList<TemperatureData> data);
    
}

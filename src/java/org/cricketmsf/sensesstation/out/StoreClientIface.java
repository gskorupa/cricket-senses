
package org.cricketmsf.sensesstation.out;

import java.util.ArrayList;
import org.cricketmsf.sensesservice.out.StoreResult;
import org.cricketmsf.sensesservice.out.TemperatureData;

/**
 *
 * @author greg
 */
public interface StoreClientIface {

    public StoreResult sendData(ArrayList<TemperatureData> data);
    
}

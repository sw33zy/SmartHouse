import EnergySupplier.EnergySupplier;
import SmartDevices.SmartBulb;
import SmartDevices.SmartDevice;
import SmartHome.SmartHome;

import java.util.Date;
import java.util.Map;

public class SmartManagerFacade {
    private Date currentDate;
    private Map<String, SmartHome> smarthomes;
    private Map<String, SmartHome> changedsmarthomes;
    private Map<Integer, EnergySupplier> supplier;
    private Map<Integer, EnergySupplier> changedsupplier;

    public float calculateConsumption(String smarthome, int days){
        SmartHome sh = smarthomes.get(smarthome);
        float consumption = 0F;
        if(sh != null){
            Map<Integer, SmartDevice> devices = sh.getDevicesAll();
            for(Map.Entry<Integer, SmartDevice> entry : devices.entrySet()){
                if(entry.getValue() instanceof SmartBulb){
                    //toneConsumption =
                    consumption += (((SmartBulb) entry.getValue()).getBaseConsume()*days);
                }
            }
        }
        return 0;
    }
}

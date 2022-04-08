package SmartHome;

import Exceptions.InvalidDeviceException;
import Exceptions.InvalidRoomException;
import SmartDevices.SmartDevice;

import java.util.List;
import java.util.Map;

public class SmartHome {
    private Map<Integer, SmartDevice> devicesAll;
    private Map<String, List<SmartDevice>> devicesRoom;
    private String ownerName;
    private String ownerNIF;

    public SmartHome(Map<Integer, SmartDevice> devicesAll, Map<String, List<SmartDevice>> devicesRoom, String ownerName, String ownerNIF) {
        this.devicesAll = devicesAll;
        this.devicesRoom = devicesRoom;
        this.ownerName = ownerName;
        this.ownerNIF = ownerNIF;
    }

    public void toggleRoomDevices(String room, int value) throws InvalidRoomException {
        List<SmartDevice> devices = devicesRoom.get(room);
        if (devices!=null){
            for(SmartDevice sd : devices) {
                if (value == 1) {
                    sd.setOn(true);
                }
                if (value == 0) {
                    sd.setOn(false);
                }
            }
        } else throw new InvalidRoomException("Room: " + room +" doesn't exist.");
    }

    public void toggleDevice(int idDevice) throws InvalidDeviceException {
        SmartDevice sd = devicesAll.get(idDevice);
        if(sd!=null){
            sd.setOn(!sd.isOn());
        } else throw new InvalidDeviceException("Device: " + idDevice + " doesn't exist.");
    }

    public Map<Integer, SmartDevice> getDevicesAll() {
        return devicesAll;
    }
}

package SmartHome;

import Exceptions.InvalidDeviceException;
import Exceptions.InvalidRoomException;
import SmartDevices.SmartBulb;
import SmartDevices.SmartCamera;
import SmartDevices.SmartDevice;
import SmartDevices.SmartSpeaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartHome implements Serializable {
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
                    SmartDevice s = devicesAll.get(sd.getId());
                    s.setOn(true);
                }
                if (value == 0) {
                    sd.setOn(false);
                    SmartDevice s = devicesAll.get(sd.getId());
                    s.setOn(false);
                }
            }
        } else throw new InvalidRoomException("Room: " + room +" doesn't exist.");
    }

    public int toggleDevice(int idDevice) throws InvalidDeviceException {
        int value = -1;

        SmartDevice sd = devicesAll.get(idDevice);
        if(sd!=null){
            sd.setOn(!sd.isOn());
            value = sd.isOn() ? 1 : 0;

            for(Map.Entry<String, List<SmartDevice>> entry: devicesRoom.entrySet()){
                for(SmartDevice s : entry.getValue()){
                    if(s.getId()== sd.getId())
                        s.setOn(sd.isOn());
                }
            }
        } else throw new InvalidDeviceException("Device: " + idDevice + " doesn't exist.");

        return value;
    }

    public Map<Integer, SmartDevice> getDevicesAll() {
        return devicesAll;
    }

    public void setDevicesAll(Map<Integer, SmartDevice> devicesAll) {
        this.devicesAll = devicesAll;
    }

    public Map<String, List<SmartDevice>> getDevicesRoom() {
        return devicesRoom;
    }

    public void setDevicesRoom(Map<String, List<SmartDevice>> devicesRoom) {
        this.devicesRoom = devicesRoom;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerNIF() {
        return ownerNIF;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SmartHome{\n");
        sb.append("\townerName=").append(ownerName).append('\n');
        sb.append("\townerNIF=").append(ownerNIF).append('\n');
        for(Map.Entry<String, List<SmartDevice>> entry : devicesRoom.entrySet()){
            sb.append("\t").append(entry.getKey()).append("{\n");
            for(SmartDevice sd : entry.getValue()){
                sb.append("\t\t").append(sd.toString());
            }
            sb.append("\t}\n");
        }
        sb.append('}');
        return sb.toString();
    }

    public Map<Integer, SmartDevice> copyDevicesAll(){
        Map<Integer, SmartDevice> copy = new HashMap<>();
        for(Map.Entry<Integer, SmartDevice> entry : this.devicesAll.entrySet()){
            if(entry.getValue() instanceof SmartBulb){
                copy.put(entry.getKey(), new SmartBulb((SmartBulb) entry.getValue()));
            }
            if(entry.getValue() instanceof SmartSpeaker){
                copy.put(entry.getKey(), new SmartSpeaker((SmartSpeaker) entry.getValue()));
            }
            if(entry.getValue() instanceof SmartCamera){
                copy.put(entry.getKey(), new SmartCamera((SmartCamera) entry.getValue()));
            }
        }
        return copy;
    }

    public Map<String, List<SmartDevice>> copyDevicesRoom(){
        Map<String, List<SmartDevice>> copy2 = new HashMap<>();

        for(Map.Entry<String, List<SmartDevice>> entry : this.devicesRoom.entrySet()){
            List<SmartDevice> listcopy = new ArrayList<>();
            for(SmartDevice sd : entry.getValue()){
                if(sd instanceof SmartBulb){
                    listcopy.add(new SmartBulb((SmartBulb) sd));
                }
                if(sd instanceof SmartSpeaker){
                    listcopy.add(new SmartSpeaker((SmartSpeaker) sd));
                }
                if(sd instanceof SmartCamera){
                    listcopy.add(new SmartCamera((SmartCamera) sd));
                }
            }
            copy2.put(entry.getKey(), listcopy);
        }
        return copy2;
    }

    public SmartHome(SmartHome that){
        this(that.copyDevicesAll(), that.copyDevicesRoom(), that.getOwnerName(), that.getOwnerNIF());
    }
}

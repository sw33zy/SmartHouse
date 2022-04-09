package SmartDevices;

import java.io.Serializable;

public abstract class SmartDevice implements Serializable {
    private int id;
    private boolean isOn;

    public SmartDevice(int id, boolean isOn) {
        this.id = id;
        this.isOn = isOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    @Override
    public String toString() {
        return "SmartDevice{" +
                "\n\t\t\t\tid=" + id +
                ",\n\t\t\t\tisOn=" + isOn +
                "\n\t\t\t}";
    }

}

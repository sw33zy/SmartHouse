package SmartDevices;

public abstract class SmartDevice {
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
}

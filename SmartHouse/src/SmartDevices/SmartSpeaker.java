package SmartDevices;

public class SmartSpeaker extends SmartDevice{
    private int volume;
    private float station;
    private String brand;
    private float baseConsume;
    private float volumeConsume;

    public SmartSpeaker(int id, boolean isOn, int volume, float station, String brand, float baseConsume, float volumeConsume) {
        super(id, isOn);
        this.volume = volume;
        this.station = station;
        this.brand = brand;
        this.baseConsume = baseConsume;
        this.volumeConsume = volumeConsume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getStation() {
        return station;
    }

    public void setStation(float station) {
        this.station = station;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getBaseConsume() {
        return baseConsume;
    }

    public void setBaseConsume(float baseConsume) {
        this.baseConsume = baseConsume;
    }

    public float getVolumeConsume() {
        return volumeConsume;
    }

    public void setVolumeConsume(float volumeConsume) {
        this.volumeConsume = volumeConsume;
    }
}

package SmartDevices;

import java.io.Serializable;

public class SmartSpeaker extends SmartDevice implements Serializable {
    private int volume;
    private String station;
    private String brand;
    private float baseConsume;
    private float volumeConsume;

    public SmartSpeaker(int id, boolean isOn, int volume, String station, String brand, float baseConsume, float volumeConsume) {
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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
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

    @Override
    public String toString() {
        return "SmartSpeaker{\n" +
                "\t\t\tvolume=" + volume + "dB" +
                ",\n\t\t\tstation=" + station +
                ",\n\t\t\tbrand='" + brand + '\'' +
                ",\n\t\t\tbaseConsume=" + baseConsume + "kWday" +
                ",\n\t\t\tvolumeConsume=" + volumeConsume + "kWday" +
                ",\n\t\t\t" + super.toString() +
                "\n\t\t}\n";
    }

    public SmartSpeaker(SmartSpeaker that){
        this(that.getId(), that.isOn(), that.getVolume(), that.getStation(), that.getBrand(), that.getBaseConsume(), that.getVolumeConsume());
    }
}

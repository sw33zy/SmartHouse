package SmartDevices;

import java.io.Serializable;

public class SmartBulb extends SmartDevice implements Serializable {
    private Tone tone;
    private float dimension;
    private float baseConsume;
    private float toneConsume;


    public SmartBulb(int id, boolean isOn, Tone tone, float dimension, float baseConsume, float toneConsume) {
        super(id, isOn);
        this.tone = tone;
        this.dimension = dimension;
        this.baseConsume = baseConsume;
        this.toneConsume = toneConsume;
    }

    public Tone getTone() {
        return tone;
    }

    public void setTone(Tone tone) {
        this.tone = tone;
    }

    public float getDimension() {
        return dimension;
    }

    public void setDimension(float dimension) {
        this.dimension = dimension;
    }

    public float getBaseConsume() {
        return baseConsume;
    }

    public void setBaseConsume(float baseConsume) {
        this.baseConsume = baseConsume;
    }

    public float getToneConsume() {
        return toneConsume;
    }

    public void setToneConsume(float toneConsume) {
        this.toneConsume = toneConsume;
    }

    @Override
    public String toString() {
        return "SmartBulb{\n" +
                "\t\t\ttone=" + tone +
                ",\n\t\t\tdimension=" + dimension + "mm" +
                ",\n\t\t\tbaseConsume=" + baseConsume + "kWday" +
                ",\n\t\t\ttoneConsume=" + toneConsume + "kWday" +
                ",\n\t\t\t" + super.toString() +
                "\n\t\t}\n";
    }

    public SmartBulb(SmartBulb that){
        this(that.getId(), that.isOn(), that.getTone(), that.getDimension(), that.getBaseConsume(), that.getToneConsume());
    }

}

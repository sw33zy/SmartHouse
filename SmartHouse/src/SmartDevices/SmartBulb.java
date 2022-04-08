package SmartDevices;

enum Tone {
    NEUTRAL,
    WARM,
    COLD
}

public class SmartBulb extends SmartDevice{
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
}

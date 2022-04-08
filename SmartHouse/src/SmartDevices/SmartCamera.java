package SmartDevices;

public class SmartCamera extends SmartDevice{
    private int resolution;
    private float fileSize;
    private float fileSizeConsume;

    public SmartCamera(int id, boolean isOn, int resolution, float fileSize, float fileSizeConsume) {
        super(id, isOn);
        this.resolution = resolution;
        this.fileSize = fileSize;
        this.fileSizeConsume = fileSizeConsume;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public float getFileSize() {
        return fileSize;
    }

    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }

    public float getFileSizeConsume() {
        return fileSizeConsume;
    }

    public void setFileSizeConsume(float fileSizeConsume) {
        this.fileSizeConsume = fileSizeConsume;
    }
}

package EnergySupplier;

import java.io.Serializable;
import java.time.LocalDate;

public class Invoice implements Serializable {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String client;
    private float consumed;
    private float payed;

    public Invoice(LocalDate dateStart, LocalDate dateEnd, String client, float consumed, float payed) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.client = client;
        this.consumed = consumed;
        this.payed = payed;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public float getConsumed() {
        return consumed;
    }

    public void setConsumed(float consumed) {
        this.consumed = consumed;
    }

    public float getPayed() {
        return payed;
    }

    public void setPayed(float payed) {
        this.payed = payed;
    }

    @Override
    public String toString() {
        return "Invoice{\n" +
                "\tdateStart=" + dateStart +
                ",\n\tdateEnd=" + dateEnd +
                ",\n\tclient='" + client + '\'' +
                ",\n\tconsumed=" + consumed +
                ",\n\tpayed=" + payed +
                "\n}";
    }
}

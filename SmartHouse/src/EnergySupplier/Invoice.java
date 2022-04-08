package EnergySupplier;

import java.util.Date;

public class Invoice {
    private Date dateStart;
    private Date dateEnd;
    private String client;
    private float consumed;
    private float payed;

    public Invoice(Date dateStart, Date dateEnd, String client, float consumed, float payed) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.client = client;
        this.consumed = consumed;
        this.payed = payed;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
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
}

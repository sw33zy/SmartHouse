package EnergySupplier;

import java.time.LocalDate;
import java.util.*;

public class EnergySupplier{
    private String name;
    private List<String> clients;
    private Map<String, Invoice> invoices;
    private float basePriceRate;
    private float tax;

    public EnergySupplier(String name, List<String> clients, Map<String, Invoice> invoices, float basePriceRate, float tax) {
        this.name = name;
        this.clients = clients;
        this.invoices = invoices;
        this.basePriceRate = basePriceRate;
        this.tax = tax;
    }

    
    public AbstractMap.Entry<String, Float> topHousePeriod(LocalDate from, LocalDate till){
        String topHome = null;
        float payedMax = 0;
        for(Map.Entry<String, Invoice> entry : invoices.entrySet()){
            if(entry.getValue().getDateStart().compareTo(from)==0 && entry.getValue().getDateEnd().compareTo(till)==0){
                if(entry.getValue().getPayed() > payedMax){
                    payedMax = entry.getValue().getPayed();
                    topHome = entry.getValue().getClient();
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(topHome, payedMax);
    }

    public float totalMade(){
        int totalMade=0;
        for(Map.Entry<String, Invoice> entry : invoices.entrySet()){
            totalMade+=entry.getValue().getPayed();
        }
        return totalMade;
    }

    public Map<String, Invoice> getInvoices() {
        return invoices;
    }

    public void addInvoice(Invoice invoice){
        this.invoices.put(UUID.randomUUID().toString(), invoice);
    }

    public Map<String, Float> topConsumers(LocalDate from, LocalDate till){
        Map<String, Float> topConsumers = new HashMap<>();
        List<Invoice> inv = new ArrayList<Invoice>();
        for(Map.Entry<String, Invoice> entry : invoices.entrySet()){
            if(entry.getValue().getDateStart().compareTo(from)==0 && entry.getValue().getDateEnd().compareTo(till)==0){
                inv.add(entry.getValue());
            }
        }

        inv.sort((i1, i2) -> Float.compare(i1.getConsumed(), i2.getConsumed()));
        inv.forEach(i -> topConsumers.put(i.getClient(), i.getPayed()));
        return topConsumers;
    }

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    public float getBasePriceRate() {
        return basePriceRate;
    }

    public float getTax() {
        return tax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

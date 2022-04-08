package EnergySupplier;

import java.util.*;

public class EnergySupplier{
    private int id;
    private List<String> clients;
    private Map<Integer, Invoice> invoices;
    private float basePriceRate;
    private float tax;

    public EnergySupplier(int id, List<String> clients, Map<Integer, Invoice> invoices, float basePriceRate, float tax) {
        this.id = id;
        this.clients = clients;
        this.invoices = invoices;
        this.basePriceRate = basePriceRate;
        this.tax = tax;
    }

    
    public String topHousePeriod(Date from, Date till){
        String topHome = null;
        float payedMax = 0;
        for(Map.Entry<Integer, Invoice> entry : invoices.entrySet()){
            if(entry.getValue().getDateStart().compareTo(from)==0 && entry.getValue().getDateEnd().compareTo(till)==0){
                if(entry.getValue().getPayed() > payedMax){
                    payedMax = entry.getValue().getPayed();
                    topHome = entry.getValue().getClient();
                }
            }
        }
        return topHome;
    }

    public float totalMade(){
        int totalMade=0;
        for(Map.Entry<Integer, Invoice> entry : invoices.entrySet()){
            totalMade+=entry.getValue().getPayed();
        }
        return totalMade;
    }

    public Map<Integer, Invoice> getInvoices() {
        return invoices;
    }

    public List<String> topConsumers(Date from, Date till){
        List<String> topConsumers = new ArrayList<>();
        List<Invoice> inv = new ArrayList<Invoice>(invoices.values());
        inv.sort((i1, i2) -> Float.compare(i1.getConsumed(), i2.getConsumed()));
        inv.forEach(i -> topConsumers.add(i.getClient()));
        return topConsumers;
    }
}

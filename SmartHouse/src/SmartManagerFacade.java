import EnergySupplier.EnergySupplier;
import EnergySupplier.Invoice;
import Exceptions.InvalidDeviceException;
import Exceptions.InvalidHomeException;
import Exceptions.InvalidRoomException;
import SmartDevices.*;
import SmartHome.SmartHome;

import java.time.LocalDate;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SmartManagerFacade {
    private LocalDate currentDate;
    private Map<String, SmartHome> smarthomes;
    private Map<String, SmartHome> changedsmarthomes;
    private Map<Integer, EnergySupplier> suppliers;
    private Map<Integer, EnergySupplier> changedsupplier;

    public void setSmarthomes(Map<String, SmartHome> smarthomes) {
        this.smarthomes = smarthomes;
    }

    public void setSuppliers(Map<Integer, EnergySupplier> suppliers) {
        this.suppliers = suppliers;
    }

    public float calculateConsumption(String smarthome, int days) throws InvalidHomeException {
        SmartHome sh = smarthomes.get(smarthome);
        float consumption = 0F;
        if(sh != null){
            Map<Integer, SmartDevice> devices = sh.getDevicesAll();
            for(Map.Entry<Integer, SmartDevice> entry : devices.entrySet()){
                if(entry.getValue().isOn()) {
                    if (entry.getValue() instanceof SmartBulb) {
                        float toneFactor = 0;
                        if (((SmartBulb) entry.getValue()).getTone() == Tone.NEUTRAL) toneFactor = 1;
                        if (((SmartBulb) entry.getValue()).getTone() == Tone.WARM) toneFactor = 0.8F;
                        if (((SmartBulb) entry.getValue()).getTone() == Tone.COLD) toneFactor = 1.2F;
                        consumption += (((SmartBulb) entry.getValue()).getBaseConsume() * days +
                                ((SmartBulb) entry.getValue()).getToneConsume() * toneFactor);
                    }
                    if (entry.getValue() instanceof SmartSpeaker) {
                        consumption += (((SmartSpeaker) entry.getValue()).getBaseConsume() * days +
                                ((SmartSpeaker) entry.getValue()).getVolumeConsume() * ((SmartSpeaker) entry.getValue()).getVolume());
                    }
                    if (entry.getValue() instanceof SmartCamera) {
                        consumption += (((SmartCamera) entry.getValue()).getFileSizeConsume()
                                * ((SmartCamera) entry.getValue()).getResolution() * days);
                    }
                }
            }
        } else throw new InvalidHomeException("SmartHome: " + smarthome + " doesn't exist.");
        return consumption;
    }

    private Integer clientSupplier(String smarthome){
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet()){
            if(entry.getValue().getClients().contains(smarthome)){
                return entry.getKey();
            }
        }
        return -1;
    }

    public float calculatePrice(String smarthome, float consumption) throws InvalidHomeException {
        SmartHome sh = smarthomes.get(smarthome);
        float price = 0F;
        float basePriceRate = 0F;
        float tax = 0F;
        if(sh != null){
            int devices = sh.getDevicesAll().size();
            EnergySupplier es = suppliers.get(clientSupplier(smarthome));
            tax = es.getTax();
            basePriceRate = es.getBasePriceRate();

            price = (float) (devices > 10 ? (basePriceRate * consumption * (1 + tax))*0.9 :
                    (basePriceRate * consumption * (1 + tax))*0.75);

        } else throw new InvalidHomeException("SmartHome: " + smarthome + " doesn't exist.");
        return price;
    }

    public void generateInvoice(String smarthome, float consumption, float payed, LocalDate start, LocalDate end){
        Invoice invoice = new Invoice(start, end, smarthome, consumption, payed);
        EnergySupplier es = suppliers.get(clientSupplier(smarthome));
        es.addInvoice(invoice);
    }

    public void changeSupplier(String smarthome, int supplier){
        EnergySupplier es = suppliers.get(clientSupplier(smarthome));
        List<String> newclients = new ArrayList<>(es.getClients());
        newclients.remove(smarthome);
        changedsupplier.put(clientSupplier(smarthome), new EnergySupplier(es.getName(), newclients, es.getInvoices(), es.getBasePriceRate(), es.getTax()));

        EnergySupplier esNewClient = suppliers.get(supplier);
        List<String> newclients2 = new ArrayList<>(esNewClient.getClients());
        newclients2.add(smarthome);
        changedsupplier.put(supplier, new EnergySupplier(esNewClient.getName(), newclients2, esNewClient.getInvoices(), esNewClient.getBasePriceRate(), esNewClient.getTax()));

    }

    public void toggleRoomDevices(String smarthome, String room, int value) throws InvalidRoomException {
        SmartHome sh = smarthomes.get(smarthome);
        SmartHome shchanged = new SmartHome(sh.getDevicesAll(), sh.getDevicesRoom(), sh.getOwnerName(), sh.getOwnerNIF());
        shchanged.toggleRoomDevices(room, value);

        changedsmarthomes.put(shchanged.getOwnerNIF(), shchanged);
    }

    public void toggleDevice(String smarthome, int idDevice) throws InvalidDeviceException {
        SmartHome sh = smarthomes.get(smarthome);
        SmartHome shchanged = new SmartHome(sh.getDevicesAll(), sh.getDevicesRoom(), sh.getOwnerName(), sh.getOwnerNIF());
        shchanged.toggleDevice(idDevice);

        changedsmarthomes.put(shchanged.getOwnerNIF(), shchanged);
    }

    public void changeSupplierTax(int supplier, float tax){
        EnergySupplier es = suppliers.get(supplier);
        changedsupplier.put(supplier, new EnergySupplier(es.getName(), es.getClients(), es.getInvoices(), es.getBasePriceRate(), tax));
    }

    public void changeSupplierBaseRate(int supplier, float baseRate){
        EnergySupplier es = suppliers.get(supplier);
        changedsupplier.put(supplier, new EnergySupplier(es.getName(), es.getClients(), es.getInvoices(), baseRate, es.getTax()));
    }

    public void applyChanges(){
        smarthomes.putAll(changedsmarthomes);
        suppliers.putAll(changedsupplier);
        changedsmarthomes = new HashMap<String, SmartHome>();
        changedsupplier = new HashMap<Integer, EnergySupplier>();
    }

    public void passTime(int days) throws InvalidHomeException {
        LocalDate endLocalDate = currentDate.plusDays(days);
        for(Map.Entry<String,SmartHome> entry : smarthomes.entrySet()){
            float consumption = calculateConsumption(entry.getKey(), days);
            generateInvoice(entry.getKey(), consumption , calculatePrice(entry.getKey(), consumption), currentDate, endLocalDate);
        }
        applyChanges();
        this.currentDate = endLocalDate;
    }

    public AbstractMap.Entry<String, Float> topHousePeriod(LocalDate from, LocalDate till){
        String topHouse = null;
        float topPayed = 0F;
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet()){
            if(entry.getValue().topHousePeriod(from, till).getValue() > topPayed){
                topPayed = entry.getValue().topHousePeriod(from, till).getValue();
                topHouse = entry.getValue().topHousePeriod(from, till).getKey();
            }
        }
        return new AbstractMap.SimpleEntry<>(topHouse,topPayed);
    }

    public AbstractMap.Entry<String, Float> topHousePeriod(){
        String name = null;
        float topMade = 0F;
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet()){
            if(entry.getValue().totalMade() > topMade){
                topMade = entry.getValue().totalMade();
                name  = entry.getValue().getName();
            }
        }
        return new AbstractMap.SimpleEntry<>(name ,topMade);
    }

    public Map<String, Invoice> listInvoices(int supplier){
        return suppliers.get(supplier).getInvoices();
    }

    public Map<String, Float> topConsumers(LocalDate from, LocalDate till){
        Map<String, Float> topConsumers = new HashMap<>();
        
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet()){
            entry.getValue().topConsumers(from, till).forEach((key, value) -> topConsumers.put(smarthomes.get(key).getOwnerName(), value));
        }

        return topConsumers.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
    }
}

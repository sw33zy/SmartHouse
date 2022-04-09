package Model;

import EnergySupplier.EnergySupplier;
import EnergySupplier.Invoice;
import Exceptions.*;
import SmartDevices.*;
import SmartHome.SmartHome;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;


public class SmartManagerFacade implements Serializable {
    private LocalDate currentDate;
    private Map<String, SmartHome> smarthomes;
    private Map<String, SmartHome> changedsmarthomes;
    private Map<Integer, EnergySupplier> suppliers;
    private Map<Integer, EnergySupplier> changedsupplier;

    public SmartManagerFacade() {
        this.smarthomes = new HashMap<>();
        this.changedsmarthomes = new HashMap<>();
        this.suppliers = new HashMap<>();
        this.changedsupplier = new HashMap<>();
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

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
                                * ((SmartCamera) entry.getValue()).getResolution()/1080 * days);
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
        float price;
        float basePriceRate;
        float tax;
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

    public void changeSupplier(String smarthome, int supplier) throws InvalidHomeException, InvalidSupplierException, SameSupplierException {
        if(clientSupplier(smarthome)==supplier) throw new SameSupplierException("SmartHouse: " + smarthome + " is already a client of: " + supplier + "!");
        EnergySupplier es = suppliers.get(clientSupplier(smarthome));
        if(es!=null) {
            List<String> newclients = new ArrayList<>(es.getClients());
            newclients.remove(smarthome);

            EnergySupplier esNewClient = suppliers.get(supplier);
            if(esNewClient!=null) {
                List<String> newclients2 = new ArrayList<>(esNewClient.getClients());
                newclients2.add(smarthome);

                changedsupplier.put(clientSupplier(smarthome), new EnergySupplier(es.getName(), newclients, es.getInvoices(), es.getBasePriceRate(), es.getTax()));
                changedsupplier.put(supplier, new EnergySupplier(esNewClient.getName(), newclients2, esNewClient.getInvoices(), esNewClient.getBasePriceRate(), esNewClient.getTax()));
            } else throw new InvalidSupplierException("Supplier: " + supplier + " doesn't exist.");
        } else throw new InvalidHomeException("SmartHome: " + smarthome + " doesn't exist.");
    }

    public void toggleRoomDevices(String smarthome, String room, int value) throws InvalidRoomException {
        SmartHome sh = smarthomes.get(smarthome);
        SmartHome shchanged = new SmartHome(sh);
        shchanged.toggleRoomDevices(room, value);

        changedsmarthomes.put(shchanged.getOwnerNIF(), shchanged);
    }

    public int toggleDevice(String smarthome, int idDevice) throws InvalidDeviceException {
        int value;
        SmartHome sh = smarthomes.get(smarthome);
        SmartHome shchanged = new SmartHome(sh);

        value = shchanged.toggleDevice(idDevice);

        changedsmarthomes.put(shchanged.getOwnerNIF(), shchanged);

        return value;
    }

    public void changeSupplierTax(int supplier, float tax) throws InvalidSupplierException {
        EnergySupplier es = suppliers.get(supplier);
        if(es==null) throw new InvalidSupplierException("Supplier: " + supplier + " doesn't exist.");
        else {
            changedsupplier.put(supplier, new EnergySupplier(es.getName(), es.getClients(), es.getInvoices(), es.getBasePriceRate(), tax));
        }
    }

    public void changeSupplierBaseRate(int supplier, float baseRate) throws InvalidSupplierException {
        EnergySupplier es = suppliers.get(supplier);
        if(es==null) throw new InvalidSupplierException("Supplier: " + supplier + " doesn't exist.");
        else {
            changedsupplier.put(supplier, new EnergySupplier(es.getName(), es.getClients(), es.getInvoices(), baseRate, es.getTax()));
        }
    }

    public void applyChanges(){
        if(changedsmarthomes.size() > 0) {

            smarthomes.putAll(changedsmarthomes);
            changedsupplier = new HashMap<>();
        }
        if(changedsupplier.size() > 0) {
            suppliers.putAll(changedsupplier);
            changedsmarthomes = new HashMap<>();
        }
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

    public AbstractMap.Entry<String, Float> topHousePeriod(LocalDate from, LocalDate till) throws InexistentInvoices {
        String topHouse = null;
        float topPayed = 0F;
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet()){
            if(entry.getValue().topHousePeriod(from, till).getValue() > topPayed){
                topPayed = entry.getValue().topHousePeriod(from, till).getValue();
                topHouse = entry.getValue().topHousePeriod(from, till).getKey();
            }
        }
        if(topHouse == null) throw new InexistentInvoices("There aren´t invoices in the period: " + from + " to " +till);
        return new AbstractMap.SimpleEntry<>(smarthomes.get(topHouse).getOwnerName(),topPayed);
    }

    public AbstractMap.Entry<String, Float> topSupplier() throws InexistentInvoices {
        String name = null;
        float topMade = 0F;
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet()){
            if(entry.getValue().totalMade() > topMade){
                topMade = entry.getValue().totalMade();
                name  = entry.getValue().getName();
            }
        }
        if(name == null) throw new InexistentInvoices("There haven't been generated any invoices!");

        return new AbstractMap.SimpleEntry<>(name ,topMade);
    }

    public Map<String, String> listInvoices(int supplier) throws InvalidSupplierException {
        EnergySupplier es = suppliers.get(supplier);
        Map<String, String> inv;
        if(es == null) throw new InvalidSupplierException("Supplier: " + supplier + " doesn't exist.");
        else{
             inv = es.getInvoicesString();
        }
        return inv;
    }

    public Map<Integer, String> supplierList(){
        Map<Integer, String> s = new HashMap<>();
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet())
            s.put(entry.getKey(),entry.getValue().getName());
        return s;
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

    public List<String> smartHomeCurrentState(){
        List<String> smarthomeslist = new ArrayList<>();
        for(Map.Entry<String,SmartHome> entry : smarthomes.entrySet()){
            smarthomeslist.add(entry.getValue().toString());
        }
        return smarthomeslist;
    }
    public String smartHomeStateNIF(String id) throws InvalidHomeException {
        String smarthomenif;
        SmartHome sh = smarthomes.get(id);
        if(sh == null) throw new InvalidHomeException("SmartHome: " + id + " doesn't exist.");
        else {
            smarthomenif = sh.toString();
        }
        return smarthomenif;
    }

    public List<String> supplierCurrentState(){
        List<String> supplierstate = new ArrayList<>();
        for(Map.Entry<Integer,EnergySupplier> entry : suppliers.entrySet()){
            supplierstate.add(entry.getValue().toString(entry.getKey()));
        }
        return supplierstate;
    }

    public String supplierCurrentStateID(int id) throws InvalidSupplierException {
        String supplierid;
        EnergySupplier es = suppliers.get(id);
        if(es == null) throw new InvalidSupplierException("Supplier: " + id + " doesn't exist.");
        else {
            supplierid = es.toString(id);
        }
        return supplierid;
    }

    public Map<String,String> smartHomeDescription(){
        Map<String,String> details = new HashMap<>();
        for(Map.Entry<String,SmartHome> entry : smarthomes.entrySet()){
            details.put(entry.getKey(), entry.getValue().getOwnerName());
        }
        return details;
    }

    public void save(String fName, SmartManagerFacade smf) throws IOException {
        FileOutputStream a = new FileOutputStream(fName);
        ObjectOutputStream r = new ObjectOutputStream(a);
        r.writeObject(smf);
        r.flush();
        r.close();
    }

    public SmartManagerFacade load(String fName) throws IOException, ClassNotFoundException {
        ObjectInputStream a = new ObjectInputStream(Files.newInputStream(Paths.get(fName)));
        SmartManagerFacade u = (SmartManagerFacade) a.readObject();
        a.close();
        return u;
    }
}

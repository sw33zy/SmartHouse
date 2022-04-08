package utils;

import EnergySupplier.EnergySupplier;
import EnergySupplier.Invoice;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonEnergySuppliersDataReader {

    public static Map<Integer, EnergySupplier> parserSuppliers(){
        Map<Integer, EnergySupplier> suppliers = new HashMap<>();
        int i = 1;
        JSONParser parser = new JSONParser();

        try{
            JSONArray a = (JSONArray) parser.parse(new FileReader("EnergySuppliers.txt"));

            for (Object o : a) {
                JSONObject supplier = (JSONObject) o;

                String name = (String) supplier.get("name");
                float basePriceRate = Float.parseFloat((String) supplier.get("basePriceRate"));
                float tax = Float.parseFloat((String) supplier.get("tax"));

                JSONArray clients = (JSONArray) supplier.get("clients");

                List<String> clientsAll = new ArrayList<>();
                Map<String, Invoice> invoices = new HashMap<>();

                for (Object d : clients) {
                    clientsAll.add((String) d);
                }

                EnergySupplier es = new EnergySupplier(name, clientsAll, invoices, basePriceRate, tax);
                suppliers.put(i++,es);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}

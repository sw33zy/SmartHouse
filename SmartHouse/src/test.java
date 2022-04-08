import EnergySupplier.EnergySupplier;
import SmartHome.SmartHome;
import utils.JsonEnergySuppliersDataReader;
import utils.JsonSmartHomesDataReader;

import java.util.Map;

public class test {
    public static void main(String[] args){
        Map<Integer, EnergySupplier> bruhh = JsonEnergySuppliersDataReader.parserSuppliers();
        for(Map.Entry<Integer, EnergySupplier> entry : bruhh.entrySet())
            System.out.println(entry.getKey());

    }
}

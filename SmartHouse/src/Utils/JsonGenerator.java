package Utils;

import com.github.javafaker.Faker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JsonGenerator {
    private static FileWriter file;
    private static Set<Integer> ids = new LinkedHashSet<>();


    public static Map.Entry<String,JSONObject> smartBulbGenerator(){
        JSONObject bulb = new JSONObject();
        Random rng = new Random();
        int size =ids.size();
        int next = 0;
        while (ids.size() != size+1)
        {
            next = rng.nextInt(10000 - 1) + 1;
            ids.add(next);
        }
        bulb.put("type", "SmartBulb");
        bulb.put("id", String.valueOf(next));
        if((next & 1) == 0){
            bulb.put("isOn", "false");
        } else{
            bulb.put("isOn", "true");
        }

        if(next < 3333){
            bulb.put("tone", "WARM");
        } else if (next>=3333 && next<=6666){
            bulb.put("tone", "COLD");
        } else if (next<=10000){
            bulb.put("tone", "NEUTRAL");
        }

        bulb.put("dimension", String.valueOf(rng.nextInt(80 - 40) + 40));
        bulb.put("baseConsume", String.valueOf(rng.nextFloat(0.07f - 0.03f) + 0.03f));
        bulb.put("toneConsume", "0.016");
        return new AbstractMap.SimpleEntry<>(String.valueOf(next),bulb);
    }

    public static Map.Entry<String,JSONObject> smartSpeakerGenerator(){
        JSONObject speaker = new JSONObject();
        Random rng = new Random();
        int size =ids.size();
        int next = 0;
        while (ids.size() != size+1)
        {
            next = rng.nextInt(10000 - 1) + 1;
            ids.add(next);
        }
        speaker.put("type", "SmartSpeaker");
        speaker.put("id", String.valueOf(next));

        List<String> stations = Arrays.asList("RTP Antena 1", "RTP Antena 2", "RTP Antena 3", "MEGA HITS", "Rádio Comercial", "RFM", "Rádio Renascença", "TSF", "M80");

        List<String> brands = Arrays.asList("Bose", "Sennheiser", "Sony", "Philips", "Dynaudio", "Bang & Olufsen", "Pioneer", "Yamaha", "JBL");

        if((next & 1) == 0){
            speaker.put("isOn", "false");
        } else{
            speaker.put("isOn", "true");
        }

        speaker.put("volume", String.valueOf(rng.nextInt(120)));
        speaker.put("station", stations.get(rng.nextInt(stations.size())));
        speaker.put("brand", brands.get(rng.nextInt(brands.size())));
        speaker.put("baseConsume", String.valueOf(rng.nextFloat(2.00f - 1.00f) + 1.00f));
        speaker.put("volumeConsume", "0.01");
        return new AbstractMap.SimpleEntry<>(String.valueOf(next),speaker);
    }

    public static Map.Entry<String,JSONObject> smartCameraGenerator(){
        JSONObject camera = new JSONObject();
        Random rng = new Random();
        int size =ids.size();
        int next = 0;
        while (ids.size() != size+1)
        {
            next = rng.nextInt(10000 - 1) + 1;
            ids.add(next);
        }
        camera.put("type", "SmartCamera");
        camera.put("id", String.valueOf(next));
        if((next & 1) == 0){
            camera.put("isOn", "false");
        } else{
            camera.put("isOn", "true");
        }

        List<String> res = Arrays.asList("360", "480", "720", "1080", "1440", "2160");

        List<String> fsize = Arrays.asList("0.5", "1.0", "1.5", "2", "2.5");

        camera.put("resolution", res.get(rng.nextInt(res.size())));
        camera.put("fileSize", fsize.get(rng.nextInt(fsize.size())));
        camera.put("fileSizeConsume", String.valueOf(rng.nextFloat(1.20f - 0.80f) + 0.80f));
        return new AbstractMap.SimpleEntry<>(String.valueOf(next),camera);
    }

    public static JSONArray roomsGenerator(List<String> houseIds){
        JSONArray rooms = new JSONArray();
        List<String> roomName = Arrays.asList("bedroom", "kitchen", "hall", "living-room", "office", "garden", "garage");
        Random rng = new Random();
        int nrrooms = rng.nextInt(houseIds.size() - 3 ) + 3;
        List<String> houseRooms = new ArrayList<String>();

        for(int i=0; i < nrrooms ; i++){
            String r = roomName.get(rng.nextInt(roomName.size()));

            int freq=0;
            for (String s : houseRooms)
                if(s.contains(r))
                    freq++;

            if(freq != 0){
                houseRooms.add(r + "-" + (freq+1));
            }
            else{
                houseRooms.add(r);
            }
        }

        Collections.shuffle(houseIds);

        List<JSONArray> idsPerRoom = new ArrayList<JSONArray>();
        for(String r : houseRooms){
            JSONArray ids = new JSONArray();
            idsPerRoom.add(ids);
        }

        for(int i = 0; i< houseRooms.size(); i++){
            idsPerRoom.get(i).add(houseIds.get(i));
        }

        int chosen = houseRooms.size();
        while(chosen<houseIds.size()){
            int j = rng.nextInt(idsPerRoom.size());
            int more = rng.nextInt((houseIds.size()-chosen)+1);
            for(int i = 0; i<more; i++){
                idsPerRoom.get(j).add(houseIds.get(i+chosen));
            }
            chosen+=more;
        }
        for(int i=0; i<houseRooms.size(); i++){
            JSONObject rm = new JSONObject();
            rm.put(houseRooms.get(i), idsPerRoom.get(i));
            rooms.add(rm);
        }
        return rooms;
    }

    public static Set<Integer> smartHomesGenerator() {
        Faker faker = new Faker();

        Random rng = new Random();

        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < 200)
        {
            Integer next = rng.nextInt(999999999 - 100000000) + 100000000;
            generated.add(next);
        }

        Iterator<Integer> iterator = generated.iterator();
        JSONArray obj = new JSONArray();
        for (int i = 0; i<200; i++){
            JSONObject house = new JSONObject();
            house.put("ownerName", faker.name().firstName() + " " + faker.name().lastName());
            house.put("ownerNIF", String.valueOf(iterator.next()));
            JSONArray devices = new JSONArray();
            Integer nrdevices = rng.nextInt(25 - 5) + 5;
            List<String> houseIds = new ArrayList<String>();
            for(int j = 0; j<nrdevices; j++ ){
                int type = rng.nextInt(3);
                if(type==0) {
                    Map.Entry<String, JSONObject> sb = smartBulbGenerator();
                    devices.add(sb.getValue());
                    houseIds.add(sb.getKey());
                } else if(type==1){
                    Map.Entry<String, JSONObject> ss = smartSpeakerGenerator();
                    devices.add(ss.getValue());
                    houseIds.add(ss.getKey());
                } else if(type==2){
                    Map.Entry<String, JSONObject> sc = smartCameraGenerator();
                    devices.add(sc.getValue());
                    houseIds.add(sc.getKey());
                }
            }
            house.put("devices", devices);
            house.put("rooms", roomsGenerator(houseIds));
            obj.add(house);
        }
        try {
            file = new FileWriter("./SmartHomes.txt");
            file.write(obj.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return generated;
    }

    public static void energySuppliersGenerator(Set<Integer> nifs){
        JSONArray obj = new JSONArray();
        Random rng = new Random();
        Set<Integer> nifss = new HashSet<>(nifs);
        List<Integer> nifsList = new ArrayList<>(nifss);
        Collections.shuffle(nifsList);
        List<String> suppliers = Arrays.asList("EDP Comercial", "Galp Energia", "Iberdrola", "Endesa", "Gold Energy", "Coopernico", "Enat", "YIce", "MEO Energia", "Muon", "Luzboa", "Energia Simples", "SU Electricidade", "EDA");


        for(int i = 0; i< suppliers.size(); i++){
            JSONObject es = new JSONObject();
            es.put("name", suppliers.get(i));
            es.put("basePriceRate", String.valueOf(rng.nextFloat(1.000f - 0.500f) + 0.500f));
            es.put("tax", String.valueOf(rng.nextFloat(0.20f - 0.07f) + 0.07f));

            JSONArray clients = new JSONArray();
            int inserted=0;
            while(inserted<nifsList.size()/suppliers.size() + 1){
                if(i*(nifsList.size()/suppliers.size() + 1) + inserted == nifsList.size())
                    break;
                clients.add(String.valueOf(nifsList.get(i*(nifsList.size()/suppliers.size() + 1) + inserted)));
                inserted++;
            }
            es.put("clients", clients);
            obj.add(es);
        }

        try {
            file = new FileWriter("./EnergySuppliers.txt");
            file.write(obj.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args){
        energySuppliersGenerator(smartHomesGenerator());
    }
}

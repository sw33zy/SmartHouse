package Utils;
import SmartDevices.*;
import SmartHome.SmartHome;
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

public class JsonSmartHomesDataReader {

    public static Map<String, SmartHome> parserHomes(){
        Map<String, SmartHome> smarthomes = new HashMap<>();
        JSONParser parser = new JSONParser();

        try{
            JSONArray a = (JSONArray) parser.parse(new FileReader("SmartHomes.txt"));

            for (Object o : a){
                JSONObject house = (JSONObject) o;

                String ownerName = (String) house.get("ownerName");
                String ownerNIF = (String) house.get("ownerNIF");

                JSONArray devices = (JSONArray) house.get("devices");

                Map<Integer, SmartDevice> devicesAll = new HashMap<>();
                Map<String, List<Integer>> devicesRoom = new HashMap<>();

                for (Object d : devices) {
                    JSONObject deviceinfo = (JSONObject) d;



                    String type = (String) deviceinfo.get("type");

                    if(type.equals("SmartBulb")){
                        int id = Integer.parseInt((String)deviceinfo.get("id"));
                        boolean isOn =  deviceinfo.get("isOn").equals("true");
                        Tone tone = deviceinfo.get("tone").equals("WARM") ?
                                Tone.WARM : deviceinfo.get("tone").equals("COLD") ? Tone.COLD : Tone.NEUTRAL;
                        float dimension = Float.parseFloat((String)deviceinfo.get("dimension"));
                        float baseConsume = Float.parseFloat((String)deviceinfo.get("baseConsume"));
                        float toneConsume = Float.parseFloat((String)deviceinfo.get("toneConsume"));

                        devicesAll.put(id, new SmartBulb(id, isOn, tone, dimension, baseConsume, toneConsume));
                    }

                    if(type.equals("SmartSpeaker")){
                        int id = Integer.parseInt((String)deviceinfo.get("id"));
                        boolean isOn =  deviceinfo.get("isOn").equals("true");
                        int volume = Integer.parseInt((String) deviceinfo.get("volume"));
                        String station = (String)deviceinfo.get("station");
                        String brand = (String) deviceinfo.get("brand");
                        float baseConsume = Float.parseFloat((String)deviceinfo.get("baseConsume"));
                        float volumeConsume = Float.parseFloat((String)deviceinfo.get("volumeConsume"));

                        devicesAll.put(id, new SmartSpeaker(id, isOn, volume, station, brand, baseConsume, volumeConsume));
                    }

                    if(type.equals("SmartCamera")){
                        int id = Integer.parseInt((String)deviceinfo.get("id"));
                        boolean isOn =  deviceinfo.get("isOn").equals("true");
                        int resolution = Integer.parseInt((String) deviceinfo.get("resolution"));
                        float fileSize = Float.parseFloat((String)deviceinfo.get("fileSize"));
                        float fileSizeConsume = Float.parseFloat((String)deviceinfo.get("fileSizeConsume"));

                        devicesAll.put(id, new SmartCamera(id, isOn, resolution, fileSize, fileSizeConsume));
                    }

                }

                JSONArray rooms = (JSONArray) house.get("rooms");

                for (Object r : rooms) {
                    JSONObject roominfo = (JSONObject) r;
                    List<Object> ids = List.of(roominfo.values().toArray());
                    for(Object rm : roominfo.keySet()) {
                        List<Integer> devicesList = new ArrayList<>();
                        for (Object value : ids) {
                            List idlist = (List) value;
                            for (Object id : idlist) {
                                devicesList.add(Integer.parseInt((String) id));
                                devicesRoom.put((String) rm, devicesList);
                            }
                        }
                    }
                }

                SmartHome sh = new SmartHome(devicesAll, devicesRoom, ownerName, ownerNIF);
                smarthomes.put(ownerNIF,sh);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return smarthomes;
    }

}

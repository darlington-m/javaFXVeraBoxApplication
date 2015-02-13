import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Data {

	JsonArray devices;
    JsonArray rooms;


    @Override
    public String toString() {
        return "Devices: \n " + devices + " \n Rooms: \n" + rooms + "\n";
    }
    
    public JsonArray getDevices(){
    	return devices;
    }
    public JsonArray getRooms(){
    	return rooms;
    }
  
    
    
}


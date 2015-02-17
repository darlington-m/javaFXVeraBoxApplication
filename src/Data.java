import com.google.gson.JsonArray;

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


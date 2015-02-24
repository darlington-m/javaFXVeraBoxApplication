package DataRetrival;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;

import Devices.DanfossRadiator;
import Devices.Data;
import Devices.DataMining;
import Devices.Device;
import Devices.FourInOne;
import Devices.HumiditySensor;
import Devices.LightSensor;
import Devices.Room;
import Devices.TemperatureSensor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;



public class JsonToJava {
	static ArrayList<Device> devices = new ArrayList<Device>();
	static ArrayList<Room> roomList = new ArrayList<Room>();
	static MySQLConnect conn;
	
	/**
	 * This calls the timer method and creates a new Reading timer.
	 * this calls the get data method and retrieves the data and pj
	 *
	 */
	public static void main(String[] args) throws IOException {
		Timer timer = new Timer();
		timer.schedule(new ReadingTimer(), 0, 360000);
	}
	
	
	public static void getData() throws UnsupportedEncodingException, MalformedURLException, IOException {
		try{
			conn = new MySQLConnect();//https://vera-us-oem-relay31.mios.com/https://vera-us-oem-relay31.mios.com/relay/relay/relay/device/35111004/session/016BBB17C64CC3B95EF174BDA508B777CE8E16/port_3480/data_request?id=user_data&rand=0.9910120870918036
			System.out.println("Attempting url");
			String url = "https://vera-us-oem-relay31.mios.com/relay/relay/relay/device/35111004/session/016FAD729649A31F425F9B2751ED8ED4D0744C/port_3480/data_request?id=user_data&rand=0.7669941254425794";
			Reader reader = new InputStreamReader(new URL(url).openStream(), "UTF-8");
			Gson gson = new Gson();
			//creates a class Data Object Holds 2 arrays: devices and rooms.
			Data d = gson.fromJson(reader, Data.class);
			// for every device in the devices array
			for(JsonElement x : d.getDevices()){
				// cast the element to an object
				JsonObject object = x.getAsJsonObject();
				// put to an object
				int xa = validateInt(object.get("id").toString());
				switch(xa){
				case 11 :
					FourInOne four = new FourInOne();
					TemperatureSensor temperaturesensor = gson.fromJson(object, TemperatureSensor.class);
					HumiditySensor humiditysensor = gson.fromJson(object, HumiditySensor.class);
					LightSensor lightsensor = gson.fromJson(object, LightSensor.class);

					four = gson.fromJson(object, FourInOne.class);

					four.setHumidity(humiditysensor);
					four.setLight(lightsensor);
					four.setTemp(temperaturesensor);
					
					conn.insertRow(four.readingToSQL());

					
					devices.add(four);

					break;
				case 9 :
					DanfossRadiator radiator = new DanfossRadiator();
					radiator =  gson.fromJson(object, DanfossRadiator.class);
					conn.insertRow(radiator.readingToSQL());
					devices.add(radiator);
					break;
				case 16 :
					DataMining datamining = new DataMining();
					datamining = gson.fromJson(object, DataMining.class);
					devices.add(datamining);
					break;
				case 14 :
					HumiditySensor humiditySensor = new HumiditySensor();
					humiditySensor = gson.fromJson(object, HumiditySensor.class);
					conn.insertRow(humiditySensor.readingToSQL());
					devices.add(humiditySensor);
					break;
				case 13 :
					LightSensor lightSensor = new LightSensor();
					lightSensor = gson.fromJson(object, LightSensor.class);
					conn.insertRow(lightSensor.readingToSQL());
					devices.add(lightSensor);
					break;
				case 12 :
					TemperatureSensor temperatureSensor = new TemperatureSensor();
					temperatureSensor = gson.fromJson(object, TemperatureSensor.class);
					conn.insertRow(temperatureSensor.readingToSQL());
					devices.add(temperatureSensor);
					break;       			
				}
			}
			
			// creates rooms and search through the devices and adds the correct device to the correct room.
			
			for(JsonElement rooms : d.getRooms()){
				JsonObject object = rooms.getAsJsonObject();
				Room room = gson.fromJson(object, Room.class);
				roomList.add(room);
					for(Device device : devices){
						if(device.getRoom() == room.getId()){
							room.addDeviceToRoom(device);
						}
					}
				}
		}catch (SocketException se){
			System.out.println("You are not connected to the internet");
		}
	}

	public static int validateInt(String string){
		if(string.matches("[0-9]*")){
			return Integer.parseInt(string);
		}else{
			string = string.replaceAll("\\D+","");
			return Integer.parseInt(string);
		}
	}
	public static ArrayList<Device> getDevices(){
		return devices;
	}
}


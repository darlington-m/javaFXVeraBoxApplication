package DataRetrival;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Devices.DanfossRadiator;
import Devices.Device;
import Devices.FourInOne;
import Devices.HumiditySensor;
import Devices.LightSensor;
import Devices.Room;
import Devices.TemperatureSensor;

public class CurrentReadings {
	MySQLConnect conn = new MySQLConnect();
	
	ArrayList<Room> rooms = new ArrayList<Room>();
	
	
	public ArrayList<Room> getCurrentReadings(){
		try {
			ArrayList<Device> devices = new ArrayList<Device>();
			ResultSet resultSetReadings = conn.getRows("SELECT * FROM Reading ORDER BY reading_date DESC LIMIT 5");
			resultSetReadings.first();
			for (int i = 0; i < 5; i++){
				//Check for heat and not humidity to tell the difference between 4in1 sensor and radiator
				if (resultSetReadings.getString("heat") != null && resultSetReadings.getString("humidity") == null ) {
					devices.add(new DanfossRadiator(
							resultSetReadings.getString("reading_device_name"),
							resultSetReadings.getInt("id"),
							resultSetReadings.getString("altid"),
							resultSetReadings.getInt("category"),
							resultSetReadings.getInt("subcategory"),
							resultSetReadings.getInt("room"),
							resultSetReadings.getInt("parent"),
							resultSetReadings.getInt("heat")));
					System.out.println("radiator created");
				//Check for light and not humidity to tell the difference between 4in1 sensor and light sensor
				} else if (resultSetReadings.getString("light") != null && resultSetReadings.getString("humidity") == null ) {
					devices.add(new LightSensor(
							resultSetReadings.getString("reading_device_name"),
							resultSetReadings.getInt("id"),
							resultSetReadings.getString("altid"),
							resultSetReadings.getInt("category"),
							resultSetReadings.getInt("subcategory"),
							resultSetReadings.getInt("room"),
							resultSetReadings.getInt("parent"),
							resultSetReadings.getInt("light")));
					System.out.println("light sensor created");

				}
				//Check for humidity and not heat to tell the difference between 4in1 sensor and humidity sensor
				else if (resultSetReadings.getString("humidity") != null && resultSetReadings.getString("heat") == null ) {
					devices.add(new HumiditySensor(
							resultSetReadings.getString("reading_device_name"),
							resultSetReadings.getInt("id"),
							resultSetReadings.getString("altid"),
							resultSetReadings.getInt("category"),
							resultSetReadings.getInt("subcategory"),
							resultSetReadings.getInt("room"),
							resultSetReadings.getInt("parent"),
							resultSetReadings.getInt("humidity")));
					System.out.println("humidity sensor created");

				}
				//Check for temperature and not humidity to tell the difference between 4in1 sensor and temperature sensor
				else if (resultSetReadings.getString("temperature") != null && resultSetReadings.getString("humidity") == null ) {
					devices.add(new TemperatureSensor(
							resultSetReadings.getString("reading_device_name"),
							resultSetReadings.getInt("id"),
							resultSetReadings.getString("altid"),
							resultSetReadings.getInt("category"),
							resultSetReadings.getInt("subcategory"),
							resultSetReadings.getInt("room"),
							resultSetReadings.getInt("parent"),
							resultSetReadings.getInt("temperature")));
					System.out.println("temperature sensor created");

				}
				//Check for temperature and humidity to tell that their is a 4in1 sensor
				else if (resultSetReadings.getString("temperature") != null && resultSetReadings.getString("humidity") != null ) {
					devices.add(new FourInOne(
							resultSetReadings.getString("reading_device_name"),
							resultSetReadings.getInt("id"),
							resultSetReadings.getString("altid"),
							resultSetReadings.getInt("category"),
							resultSetReadings.getInt("subcategory"),
							resultSetReadings.getInt("room"),
							resultSetReadings.getInt("parent"),
							resultSetReadings.getInt("temperature"),
							resultSetReadings.getInt("light"),
							resultSetReadings.getInt("humidity")));
					System.out.println("4in1 created");

				}
				resultSetReadings.next();
			}
			
			rooms = new ArrayList<Room>();
			ResultSet resultSetRooms = conn.getRows("SELECT * FROM Room");
			
			resultSetRooms.first();
			
			for (int i = 0; i < 3; i++){
				rooms.add(new Room(resultSetRooms.getString("room_name"),
							resultSetRooms.getInt("room_id")));
				resultSetReadings.next();
			}
			
			
			// Create an array list of rooms
		for(Room currentRoom: rooms){
			for(Device currentDevice: devices){
				if(currentDevice.getRoom() == currentRoom.getId()){
					currentRoom.addDeviceToRoom(currentDevice);
				}
			}
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rooms; // return rooms
		
	}
}

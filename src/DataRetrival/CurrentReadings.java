package DataRetrival;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import Devices.DanfossRadiator;
import Devices.Device;
import Devices.FourInOne;
import Devices.HumiditySensor;
import Devices.LightSensor;
import Devices.Room;
import Devices.TemperatureSensor;

public class CurrentReadings {
	MySQLConnect conn;
	ArrayList<Device> devices;
	ArrayList<Room> rooms;

	public CurrentReadings() {

		this.conn = new MySQLConnect();
		this.devices = new ArrayList<Device>();
		this.rooms = new ArrayList<Room>();
		getCurrentReadings();
	}

	private void getCurrentReadings() {

		try {
			
			ResultSet tempCount = conn.getRows("SELECT COUNT(*) AS deviceCount FROM Device");
			tempCount.next();
			ResultSet resultSetReadings = conn
					.getRows("SELECT * FROM Reading ORDER BY reading_date DESC LIMIT " + tempCount.getInt("deviceCount"));

			while (resultSetReadings.next()) {
				// Check for heat and not humidity to tell the difference
				// between 4in1 sensor and radiator
				if (resultSetReadings.getInt("category") == 5) {
					devices.add(new DanfossRadiator(resultSetReadings
							.getString("reading_device_name"),
							resultSetReadings.getInt("id"), resultSetReadings
									.getString("altid"), resultSetReadings
									.getInt("category"), resultSetReadings
									.getInt("subcategory"), resultSetReadings
									.getInt("room"), resultSetReadings
									.getInt("parent"), resultSetReadings
									.getInt("heat"), resultSetReadings
									.getInt("setpoint"), resultSetReadings
									.getInt("heat"), resultSetReadings
									.getInt("cool"), resultSetReadings
									.getString("commands"), resultSetReadings
									.getInt("batterylevel"), resultSetReadings
									.getString("reading_mode"), resultSetReadings
									.getInt("state"), resultSetReadings
									.getString("reading_comment")		
							));
					// Check for light and not humidity to tell the difference
					// between 4in1 sensor and light sensor
				} else if (resultSetReadings.getInt("category") == 18) {
					devices.add(new LightSensor(resultSetReadings
							.getString("reading_device_name"),
							resultSetReadings.getInt("id"), resultSetReadings
									.getString("altid"), resultSetReadings
									.getInt("category"), resultSetReadings
									.getInt("subcategory"), resultSetReadings
									.getInt("room"), resultSetReadings
									.getInt("parent"), resultSetReadings
									.getInt("light")));
				}
				// Check for humidity and not heat to tell the difference
				// between 4in1 sensor and humidity sensor
				else if (resultSetReadings.getInt("category") == 16){
					devices.add(new HumiditySensor(resultSetReadings
							.getString("reading_device_name"),
							resultSetReadings.getInt("id"), resultSetReadings
									.getString("altid"), resultSetReadings
									.getInt("category"), resultSetReadings
									.getInt("subcategory"), resultSetReadings
									.getInt("room"), resultSetReadings
									.getInt("parent"), resultSetReadings
									.getInt("humidity")));

				}
				// Check for temperature and not humidity to tell the difference
				// between 4in1 sensor and temperature sensor
				else if (resultSetReadings.getInt("category") == 17) {
					devices.add(new TemperatureSensor(resultSetReadings
							.getString("reading_device_name"),
							resultSetReadings.getInt("id"), resultSetReadings
									.getString("altid"), resultSetReadings
									.getInt("category"), resultSetReadings
									.getInt("subcategory"), resultSetReadings
									.getInt("room"), resultSetReadings
									.getInt("parent"), resultSetReadings
									.getInt("temperature")));
				}
				// Check for temperature and humidity to tell that their is a
				// 4in1 sensor
				else if (resultSetReadings.getInt("category") == 4) {
					devices.add(new FourInOne(resultSetReadings
							.getString("reading_device_name"),
							resultSetReadings.getInt("id"), resultSetReadings
									.getString("altid"), resultSetReadings
									.getInt("category"), resultSetReadings
									.getInt("subcategory"), resultSetReadings
									.getInt("room"), resultSetReadings
									.getInt("parent"), resultSetReadings
									.getInt("temperature"), resultSetReadings
									.getInt("light"), resultSetReadings
									.getInt("humidity"), resultSetReadings
									.getInt("armedtripped"),resultSetReadings
									.getInt("batterylevel")));
				}
			}

			ResultSet resultSetRooms = conn.getRows("SELECT * FROM Room");

			while (resultSetRooms.next()) {

				rooms.add(new Room(resultSetRooms.getString("room_name"),
						resultSetRooms.getInt("room_id")));
			}

			// Create an array list of rooms
			for (Room currentRoom : rooms) {
				for (Device currentDevice : devices) {
					if (currentDevice.getRoom() == currentRoom.getId()) {
						currentRoom.addDeviceToRoom(currentDevice);
					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}
	

	public ArrayList<Device> getAllDevices() {
		return devices;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}

}

package Devices;

import java.util.ArrayList;

public class Room {
	private String name;
	private int id;
	private int section;
	private ArrayList<Device> devices = new ArrayList<Device>();

	Room(String name, int id) {
		this.name = name;
		this.id = id;
	}

	Room() {

	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getSection() {
		return section;
	}

	public void addDeviceToRoom(Device device) {
		devices.add(device);
	}

	public void removeDeviceFromRoom(Device device) {
		devices.remove(device);
	}

	public ArrayList<Device> getDevices() {
		return devices;
	}
}
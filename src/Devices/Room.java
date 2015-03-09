package Devices;

import java.sql.SQLException;
import java.util.ArrayList;

import GUI.VeraGUI;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
	
	public Pane getPane(double length){
		Pane roomPane = new Pane();
		roomPane.setId("roomPane");
		roomPane.setPrefWidth(length);
		
		Label roomName = new Label("Room : " + getName());
		roomName.setLayoutX(20);
		roomName.setLayoutY(15);
		roomName.setId("roomName");
		
		roomPane.getChildren().addAll(roomName);
		return roomPane;
	}
}
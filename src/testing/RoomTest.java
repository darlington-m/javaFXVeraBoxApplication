package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javafx.scene.layout.Pane;

import org.junit.Test;

import devices.Device;
import devices.LightSensor;
import devices.Room;

public class RoomTest {

	Room testRoom;
	Device testDevice = new LightSensor("name", 1, "altid", 2, 3, 4, 5, 3);
	@Test
	public void testRoomStringInt() {
		testRoom = new Room("Room",1);
		assertEquals(testRoom.getName(),"Room");
		assertEquals(testRoom.getId(),1);
		
	}

	@Test
	public void testGetName() {
		assertEquals(testRoom.getName(),"Room");
	}

	@Test
	public void testGetId() {
		assertEquals(testRoom.getId(),1);
	}

	@Test
	public void testGetSection() {
		assertEquals(testRoom.getSection(),null);
	}

	@Test
	public void testAddDeviceToRoom() {
		assertTrue(testRoom.getDevices().isEmpty());
		
		testRoom.addDeviceToRoom(testDevice);
		
		assertFalse(testRoom.getDevices().isEmpty());
		assertTrue(testRoom.getDevices().contains(testDevice));
	}

	@Test
	public void testGetDevices() {
		assertTrue(testRoom.getDevices() instanceof ArrayList);
	}
	@Test
	public void testRemoveDeviceFromRoom() {
		assertFalse(testRoom.getDevices().isEmpty());
		
		testRoom.removeDeviceFromRoom(testDevice);
		
		assertFalse(testRoom.getDevices().contains(testDevice));
		assertTrue(testRoom.getDevices().isEmpty());
		assertFalse(testRoom.getDevices().contains(testDevice));
	}

	

	@Test
	public void testGetPane() {
		assertTrue(testRoom.getPane(19)!=null);
		assertTrue(testRoom.getPane(19) instanceof Pane);
	}

	@Test
	public void testGetDetailsPane() {
		assertTrue(testRoom.getDetailsPane()!=null);
		assertTrue(testRoom.getDetailsPane() instanceof Pane);
	}

}

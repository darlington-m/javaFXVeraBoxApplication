package testing;

import static org.junit.Assert.*;
import Devices.Device;
import Devices.HumiditySensor;
import Devices.Room;

import org.junit.Test;

public class RoomTest {

	Room testRoom;
	Device device  = new HumiditySensor("name", 1, "altid", 2,
			3, 4, 1, 23);
	@Test
	public void testRoomStringInt() {
		testRoom= new Room(new String("Name"), 4);
		assertEquals(testRoom.getName(),"Name");
		assertEquals(testRoom.getId(),4);
		
	}

	@Test
	public void testRoom() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		assertEquals(testRoom.getName(),"Name");
	}

	@Test
	public void testGetId() {
		assertEquals(testRoom.getId(),4);
	}

	@Test
	public void testSetSection() {
		//testRoom.setSection(1);
		//assertEquals(testRoom.getSection(),1);
	}
	@Test
	public void testGetSection() {
		assertEquals(testRoom.getSection(),1);
	}

	@Test
	public void testAddDeviceToRoom() {
		testRoom.addDeviceToRoom(device);
		assertTrue(testRoom.getDevices().contains(device));
	}

	@Test
	public void testRemoveDeviceFromRoom() {
		testRoom.removeDeviceFromRoom(device);
		assertFalse(testRoom.getDevices().contains(device));
	}

	@Test
	public void testGetDevices() {
		assertTrue(testRoom.getDevices()!=null);
		assertTrue(testRoom.getDevices().isEmpty());
		testRoom.addDeviceToRoom(device);
		assertFalse(testRoom.getDevices().isEmpty());
		testRoom.removeDeviceFromRoom(device);
		assertFalse(testRoom.getDevices().isEmpty());
	}

	@Test
	public void testGetPane() {
		assertTrue(testRoom.getPane(13.5)!=null);
	}

	@Test
	public void testGetDetailsPane() {
		assertTrue(testRoom.getDetailsPane()!=null);
	}


}

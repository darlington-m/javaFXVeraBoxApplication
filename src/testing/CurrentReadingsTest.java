package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import dataretrival.CurrentReadings;
import devices.Room;

public class CurrentReadingsTest {

	CurrentReadings testReadings;
	@Test
	public void testCurrentReadings() {
		testReadings  = new CurrentReadings();
		assertTrue(testReadings.getRooms()!=null);
		assertTrue(testReadings.getAllDevices()!=null);
	}

	@Test
	public void testGetRooms() {
		assertTrue(testReadings.getRooms()!=null);
	}

	@Test
	public void RoomSetRooms() {
		ArrayList<Room> testArray  = new ArrayList<Room>();
		Room testRoom = new Room("room",1);
		testArray.add(testRoom);
		testReadings.setRooms(testArray);
		assertEquals(testReadings.getRooms().size(),1);
		assertTrue(testReadings.getRooms().contains(testRoom));
	}

}

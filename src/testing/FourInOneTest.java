package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Devices.FourInOne;

/**
 * 
 * @author Remus
 *
 */
public class FourInOneTest {

	@Test
	public void testToString() {
		FourInOne four = new FourInOne("name", 1, "altid", 2, 3, 5, 1, 30, 60, 23, 1, 78);
		String actualToString = four.toString();
		String expectedToString = "Name: name" + " Id: " +  1 + " AltID: " + "altid" + " Category: " + 2 + " Subcategory: " + 3 + " Room: " +
				5 + " Parent: " + 1 + 
				" Battery Level: " + 78
				+ " Temperature: " + 30
				+ " Light: " + 60 + " Humidity: "
				+ 23 + " ArmedTripped: "
				+ 1 + " Armed: " + 0 + " State: " + 0
				+ " Comment: " + null + " Tripped: " + 0
				+ " LastTrip: " + 0;

		assertEquals(expectedToString, actualToString);
	}

	@Test
	public void testGetDetails() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67); 
		String actualDetails = four.getDetails();

		String expectedDetails =  "Name: " + "name" + "\nId: " + 2 + "\nAltID: " + "altid"
				+ "\nCategory: " + 1 + "\nSubcategory: " + 3
				+ "\nRoom: " + 5 + "\nParent: " + 4 + "\nTemperature: "
				+ 20 + "\nLight: "
				+ 70 + "\nHumidity: "
				+ 30; 

		assertEquals(expectedDetails, actualDetails);
	}

	@Test
	public void testReadingFromSQLLongLong() {
		FourInOne four = new FourInOne();
		String actualReading = four.readingFromSQL(12345, 56789);
		String expectedReading = "SELECT humidity,light,heat, reading_date FROM Reading WHERE id =  '"
				+ four.getId() + "' AND reading_date >='" + 12345
				+ "' AND reading_date <='" + 56789 + "'";

		assertEquals(expectedReading, actualReading);
	}

	@Test
	public void testGetPane() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBatterylevel() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67); 
		int actualBattery = four.getBatterylevel(), expectedBattery = 67;
		assertEquals(expectedBattery, actualBattery);
	}

	@Test
	public void testFourInOne() {
		FourInOne four = new FourInOne();

		assertEquals(four.getImage(), "4in1.png");
		assertTrue(four instanceof FourInOne);
		assertEquals(0, four.getArmed());
		assertEquals(null, four.getName());
	}

	@Test
	public void testFourInOneStringIntStringIntIntIntIntIntIntIntIntInt() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);

		assertEquals(four.getName(), "name");
		assertEquals(four.getId(), 2);
		assertEquals(four.getAltid(), "altid");
		assertEquals(four.getCategory(), 1);
		assertEquals(four.getSubcategory(), 3);
		assertEquals(four.getRoom(), 5);
		assertEquals(four.getParent(), 4);
		assertEquals(four.getBatterylevel(), 67);
		assertEquals(four.getState(), 0);
		assertEquals(four.getComment(), null);
		assertEquals(four.getArmed(), 0);
		assertEquals(four.getArmedtripped(), 7);
		assertEquals(four.getHumidity(), 30);
		assertEquals(four.getTemperature(), 20);
		assertEquals(four.getLasttrip(), 0);
		assertEquals(four.getLight(), 70);
		assertEquals(four.getReadingName(), "");
	}

	@Test
	public void testReadingFromSQLStringLongLong() {
		FourInOne four = new FourInOne();
		String actualReading = four.readingFromSQL("readingTest", 12345, 56789);
		String expectedReading = "SELECT " + "readingTest" + ", reading_date FROM Reading WHERE id =  '"
				+ four.getId() + "' AND reading_date >='" + 12345
				+ "' AND reading_date <='" + 56789 + "'";

		assertEquals(expectedReading, actualReading);
	}

	@Test
	public void testGetTemperature() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualTemp = four.getTemperature(), expectedTemp = 20;

		assertEquals(expectedTemp, actualTemp);
	}

	@Test
	public void testGetLight() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualLight = four.getLight(), expectedLight = 70;

		assertEquals(expectedLight, actualLight);
	}

	@Test
	public void testGetHumidity() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualHumidity = four.getHumidity(), expectedHumidity = 30;

		assertEquals(expectedHumidity, actualHumidity);
	}

	@Test
	public void testGetArmedtripped() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualArmedTripped = four.getArmedtripped(), expectedArmedTripped = 7;

		assertEquals(expectedArmedTripped, actualArmedTripped);
	}

	@Test
	public void testGetArmed() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualArmed = four.getArmed(), expectedArmed = 0;

		assertEquals(expectedArmed, actualArmed);
	}

	@Test
	public void testGetState() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualState = four.getState(), expectedTemp = 0;

		assertEquals(expectedTemp, actualState);
	}

	@Test
	public void testGetComment() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		String actualComment = four.getComment(), expectedComment = null;

		assertEquals(expectedComment, actualComment);
	}

	@Test
	public void testGetTripped() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualTripped = four.getTripped(), expectedTripped = 0;

		assertEquals(expectedTripped, actualTripped);
	}

	@Test
	public void testGetLasttrip() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		int actualLastTrip = four.getLasttrip(), expectedLastTrip = 0;

		assertEquals(expectedLastTrip, actualLastTrip);
	}

	@Test
	public void testSetBatterylevel() {
		FourInOne four = new FourInOne();
		four.setBatterylevel(99);
		int actualBattery = four.getBatterylevel(), expectedBattery = 99;

		assertEquals(expectedBattery, actualBattery);
	}

	@Test
	public void testSetTemperature() {
		FourInOne four = new FourInOne();
		four.setTemperature(26);
		int actualTemp = four.getTemperature(), expectedTemp = 26;

		assertEquals(expectedTemp, actualTemp);
	}

	@Test
	public void testSetLight() {
		FourInOne four = new FourInOne();
		four.setLight(89);
		int actualLight = four.getLight(), expectedLight = 89;

		assertEquals(expectedLight, actualLight);
	}

	@Test
	public void testSetHumidity() {
		FourInOne four = new FourInOne();
		four.setHumidity(35);
		int actualHumidity = four.getHumidity(), expectedHumidity = 35;

		assertEquals(expectedHumidity, actualHumidity);
	}

	@Test
	public void testSetArmedtripped() {
		FourInOne four = new FourInOne();
		four.setBatterylevel(99);
		int actualBattery = four.getBatterylevel(), expectedBattery = 99;

		assertEquals(expectedBattery, actualBattery);
	}

	@Test
	public void testSetArmed() {
		FourInOne four = new FourInOne();
		four.setArmed(1);
		int actualArmed = four.getArmed(), expectedArmed = 1;

		assertEquals(expectedArmed, actualArmed);
	}

	@Test
	public void testSetState() {
		FourInOne four = new FourInOne();
		four.setState(2);
		int actualState = four.getState(), expectedState = 2;

		assertEquals(expectedState, actualState);
	}

	@Test
	public void testSetComment() {
		FourInOne four = new FourInOne();
		four.setComment("test");
		String actualComment = four.getComment(), expectedComment = "test";

		assertEquals(expectedComment, actualComment);
	}

	@Test
	public void testSetTripped() {
		FourInOne four = new FourInOne();
		four.setTripped(10);
		int actualTripped = four.getTripped(), expectedTripped = 10;

		assertEquals(expectedTripped, actualTripped);
	}

	@Test
	public void testSetLasttrip() {
		FourInOne four = new FourInOne();
		four.setLasttrip(11);
		int actualLastTrip = four.getLasttrip(), expectedLastTrip = 11;

		assertEquals(expectedLastTrip, actualLastTrip);
	}

	@Test
	public void testSetReadingName() {
		FourInOne four = new FourInOne();
		four.setReadingName("test");
		String actualReadingName = four.getReadingName(), expectedReadingName = "test";

		assertEquals(expectedReadingName, actualReadingName);
	}

	@Test
	public void testGetReadingNames() {
		FourInOne four = new FourInOne("name", 2, "altid", 1, 3, 5, 4, 20, 70, 30, 7, 67);
		ArrayList<String> actualReadingNames = four.getReadingNames();
		ArrayList<String> expectedReadingNames = new ArrayList<String>();

		expectedReadingNames.add("temperature");
		expectedReadingNames.add("humidity");
		expectedReadingNames.add("light");
		expectedReadingNames.add("armedtripped");

		assertEquals(expectedReadingNames, actualReadingNames);
	}

}

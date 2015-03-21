package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import devices.HumiditySensor;

/**
 * 
 * @author Remus
 *
 */
public class HumiditySensorTest {

	@Test
	public void testToString() {

		HumiditySensor humidity = new HumiditySensor("name", 1, "altid", 2, 3, 4, 5, 12345);
		String actualToString = humidity.toString();
		String expectedToString = "Name: " + "name" + " Id: " + 1 + " AltID: " + "altid"
				+ " Category: " + 2 + " Subcategory: " + 3
				+ " Room: " + 4 + " Parent: " + 5 + " Humidity Reading: " + 0;

		assertEquals(expectedToString, actualToString);
	}

	@Test
	public void testGetDetails() {
		HumiditySensor humidity = new HumiditySensor("name", 1, "altid", 2, 3, 4, 5, 3);
		String actualDetails = humidity.getDetails();

		String expectedDetails =  "Name: " + "name" + "\nId: " + 1 + "\nAltID: " + "altid"
				+ "\nCategory: " + 2 + "\nSubcategory: " + 3
				+ "\nRoom: " + 4 + "\nParent: " + 5 + "\nHumidity: " + 0;

		assertEquals(expectedDetails, actualDetails);
	}

	@Test
	public void testGetName() {
		HumiditySensor humidity = new HumiditySensor("name11111111111", 1, "altid", 2, 3, 4, 5, 3);
		String actualName = humidity.getName(), expectedName = "name11111111111";

		assertEquals(expectedName, actualName);
	}

	@Test
	public void testReadingFromSQL() {
		HumiditySensor humidity = new HumiditySensor();
		String actualReading = humidity.readingFromSQL(12345, 56789);
		String expectedReading = "SELECT humidity, reading_date FROM Reading WHERE id =  '"
				+ humidity.getId() + "' AND reading_date >='" + 12345
				+ "' AND reading_date <='" + 56789 + "'";

		assertEquals(expectedReading, actualReading);
	}

	@Test
	public void testGetPane() {
		fail("Not yet implemented");
	}

	@Test
	public void testHumiditySensor() {
		HumiditySensor humidity = new HumiditySensor();

		assertEquals(humidity.getImage(), "humidity.jpg");
		assertTrue(humidity instanceof HumiditySensor);
		assertEquals(0, humidity.getBatterylevel());
		assertEquals(null, humidity.getAltid());

	}

	@Test
	public void testHumiditySensorStringIntStringIntIntIntIntInt() {
		HumiditySensor humidity = new HumiditySensor("name11111111111", 1, "altid", 2, 3, 4, 5, 3);

		assertEquals(humidity.getName(), "name11111111111");
		assertEquals(humidity.getId(), 1);
		assertEquals(humidity.getAltid(), "altid");
		assertEquals(humidity.getCategory(), 2);
		assertEquals(humidity.getSubcategory(), 3);
		assertEquals(humidity.getRoom(), 4);
		assertEquals(humidity.getParent(), 5);
		assertEquals(humidity.getBatterylevel(), 0);

	}

	@Test
	public void testGetReading() {
		HumiditySensor humidity = new HumiditySensor("name", 1, "altid", 2, 3, 4, 5, 3);
		int actualReading = humidity.getReading(), expectedReading = 3;

		assertEquals(expectedReading, actualReading);
	}

}
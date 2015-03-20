package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import Devices.LightSensor;

/**
 * 
 * @author Remus
 *
 */
public class LightSensorTest {

	@Test
	public void testToString() {
		LightSensor light = new LightSensor("name", 1, "altid", 2, 3, 4, 5, 3);
		String actualToString = light.toString();
		String expectedToString = "Name: " + "name" + " Id: " + 1 + " AltID: " + "altid"
				+ " Category: " + 2 + " Subcategory: " + 3
				+ " Room: " + 4 + " Parent: " + 5 + " Light: " + 0 + "Image: " + "bulb.png";

		assertEquals(expectedToString, actualToString);
	}

	@Test
	public void testGetDetails() {
		LightSensor light = new LightSensor("name", 1, "altid", 2, 3, 4, 5, 3);
		String actualDetails = light.getDetails();

		String expectedDetails =  "Name: " + "name" + "\nId: " + 1 + "\nAltID: " + "altid"
				+ "\nCategory: " + 2 + "\nSubcategory: " + 3
				+ "\nRoom: " + 4 + "\nParent: " + 5 + "\nLight: " + 0;

		assertEquals(expectedDetails, actualDetails);
	}

	@Test
	public void testReadingFromSQL() {
		LightSensor light = new LightSensor();
		String actualReading = light.readingFromSQL(12345, 56789);
		String expectedReading = "SELECT light, reading_date FROM Reading WHERE id =  '"
				+ light.getId() + "' AND reading_date >='" + 12345
				+ "' AND reading_date <='" + 56789 + "'";

		assertEquals(expectedReading, actualReading);
	}

	@Test
	public void testGetPane() {
		fail("Not yet implemented");
	}

	@Test
	public void testLightSensor() {
		LightSensor light = new LightSensor();

		assertEquals(light.getImage(), "bulb.png");
		assertTrue(light instanceof LightSensor);
		assertEquals(0, light.getBatterylevel());
		assertEquals(null, light.getAltid());
	}

	@Test
	public void testLightSensorStringIntStringIntIntIntIntInt() {
		LightSensor light = new LightSensor("name", 1, "altid", 2, 3, 4, 5, 3);

		assertEquals(light.getName(), "name");
		assertEquals(light.getId(), 1);
		assertEquals(light.getAltid(), "altid");
		assertEquals(light.getCategory(), 2);
		assertEquals(light.getSubcategory(), 3);
		assertEquals(light.getRoom(), 4);
		assertEquals(light.getParent(), 5);
		assertEquals(light.getReading(), 3);
		assertEquals(light.getImage(), "bulb.png");
		assertEquals(light.getBatterylevel(), 0);

	}

	@Test
	public void testGetReading() {
		LightSensor light = new LightSensor("name", 1, "altid", 2, 3, 4, 5, 3);
		int actualReading = light.getReading(), expectedReading = 3;

		assertEquals(expectedReading, actualReading);
	}

}
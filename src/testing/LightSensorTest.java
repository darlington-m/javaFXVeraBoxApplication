package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import Devices.LightSensor;

public class LightSensorTest {

	LightSensor testLightSensor;
	@Test
	public void testLightSensor() {
		testLightSensor = new LightSensor();
		assertTrue(testLightSensor.getImage()!=null);
		assertTrue(testLightSensor.getReadingName()!=null);
		assertTrue(testLightSensor.getReadingName().equals("light"));
		testLightSensor=null;
	}
	
	@Test
	public void testLightSensorStringIntStringIntIntIntIntInt() {
		testLightSensor = new LightSensor("LightSensor",1,"AltId", 2,3,2,3,5);
		assertTrue(testLightSensor.getImage()!=null);
		assertTrue(testLightSensor.getReadingName()!=null);
		assertTrue(testLightSensor.getImage().equals("bulb.png"));
		assertTrue(testLightSensor.getReadingName().equals("light"));
		assertEquals(testLightSensor.getName(),"LightSensor");
		assertEquals(testLightSensor.getId(),1);
		assertEquals(testLightSensor.getAltid(),"AltId");
		assertEquals(testLightSensor.getCategory(),2);
		assertEquals(testLightSensor.getSubcategory(),3);
		assertEquals(testLightSensor.getRoom(),2);
		assertEquals(testLightSensor.getParent(),3);
		assertEquals(testLightSensor.getReading(),5);
		
	}

	@Test
	public void testGetDetails() {
		assertTrue(testLightSensor.getDetails()!=null);
	}

	@Test
	public void testReadingFromSQL() {
		assertTrue(testLightSensor.readingFromSQL(1, 2)!=null);
	}

	@Test
	public void testGetPane() {
		assertTrue(testLightSensor.getPane()!=null);
	}
	
	@Test
	public void testGetReading() {
		assertEquals(testLightSensor.getReading(),5);
	}

	@Test
	public void testReadingToSQL() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		assertEquals(testLightSensor.getName(),"LightSensor");
	}

	@Test
	public void testGetId() {
		assertEquals(testLightSensor.getId(),1);
	}

	@Test
	public void testGetAltid() {
		assertEquals(testLightSensor.getAltid(),"AltId");
	}

	@Test
	public void testGetCategory() {
		assertEquals(testLightSensor.getCategory(),2);
	}

	@Test
	public void testGetSubcategory() {
		assertEquals(testLightSensor.getSubcategory(),3);
	}

	@Test
	public void testGetRoom() {
		assertEquals(testLightSensor.getRoom(),2);
	}

	@Test
	public void testGetParent() {
		assertEquals(testLightSensor.getParent(),3);
	}

	@Test
	public void testGetImage() {
		assertTrue(testLightSensor.getImage().equals("bulb.png"));
	}

	@Test
	public void testSetImage() {
		assertTrue(testLightSensor.getImage().equals("bulb.png"));
		testLightSensor.setImage("NotBulb.png");
		assertTrue(testLightSensor.getImage().equals("NotBulb.png"));
		testLightSensor.setImage("bulb.png");
	}

	@Test
	public void testGetReadingName() {
		assertTrue(testLightSensor.getReadingName().equals("light"));
	}


}

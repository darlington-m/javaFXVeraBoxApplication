package testing;

import static org.junit.Assert.*;
import javafx.scene.layout.Pane;

import org.junit.Test;

import Devices.DanfossRadiator;

public class DanfossRadiatorTest {

	@Test
	public void testToString() {
		DanfossRadiator dr = new DanfossRadiator("test", 1, "string2", 2, 3, 4, 5, 6, 0, 0, 0, null, 0, null, 0, null);
		String actualValue = dr.toString(), expectedValue = "Name: test Id: 1 AltID: string2 Category: 2 Subcategory: 3 Room: 4 Parent: 5 SetPoint: 0 Heat: 0 Cool: 0 Commands: null BatteryLevel: 0 Mode: null State:  0 Comment: null";
		
		assertEquals(expectedValue, actualValue);
		
	}

	@Test
	public void testGetDetails() {
		DanfossRadiator dr1 = new DanfossRadiator("test", 1, "string2", 2, 3, 4, 5, 6, 0, 0, 0, null, 0, null, 0, null);
		DanfossRadiator dr2 = new DanfossRadiator("test", 1, "string2", 2, 3, 4, 5, 6, 0, 0, 0, null, 0, null, 0, null);
		DanfossRadiator dr3 = new DanfossRadiator("test", 1, "string2", 2, 3, 4, 5, 7, 0, 0, 0, null, 0, null, 0, null);
		
		String actualValue1= dr1.getDetails(), expectedValue1 = dr2.getDetails();
		String actualValue2 = dr1.getDetails(), expectedValue2 = dr3.getDetails();

		assertEquals(expectedValue1, actualValue1);
		assertEquals(expectedValue1, actualValue1);
		assertNotSame(expectedValue2, actualValue2);
	}

	@Test
	public void testReadingFromSQL() {
		DanfossRadiator dr1 = new DanfossRadiator("test", 1, "string2", 2, 3, 4, 5, 6, 0, 0, 0, null, 0, null, 0, null);
		DanfossRadiator dr2 = new DanfossRadiator("test", 1, "string2", 2, 3, 4, 5, 6, 0, 0, 0, null, 0, null, 0, null);
		DanfossRadiator dr3 = new DanfossRadiator("test", 1, "string3", 2, 3, 4, 5, 7, 0, 0, 0, null, 0, null, 0, null);
		
		
		String actualValue1= dr1.readingFromSQL(0, 0), expectedValue1 = dr1.readingFromSQL(0, 0);
		String actualValue2 = dr2.readingFromSQL(0, 0), expectedValue2 = dr2.readingFromSQL(0, 0);
		String actualValue3 = dr2.readingFromSQL(0, 0), expectedValue3 = dr3.readingFromSQL(0, 0);
		

		assertEquals(expectedValue1, actualValue1);
		assertEquals(expectedValue1, actualValue1);
		assertNotSame(expectedValue3, actualValue3);
	
		
			}

	@Test
	public void testGetPane() {
		fail("Not yet implemented");
		DanfossRadiator dr = new DanfossRadiator();
		Pane pane = dr.getPane();
		assertTrue(pane instanceof javafx.scene.layout.Pane);
	}


	@Test
	public void testGetReading() {
		DanfossRadiator dr1 = new DanfossRadiator("test", 1, "string2", 2, 3, 4, 5, 6, 0, 0, 0, null, 0, null, 0, null);
		DanfossRadiator dr2 = new DanfossRadiator("test", 1, "string3", 2, 3, 4, 5, 7, 0, 0, 0, null, 0, null, 0, null);
		
		int expectedValue1 = 6, actualValue1 = dr1.getReading();
		int expectedValue2 = dr1.getReading(), actualValue2 = dr2.getReading();
		assertEquals(expectedValue1, actualValue1);
		assertNotSame(expectedValue2, actualValue2);
	}
}

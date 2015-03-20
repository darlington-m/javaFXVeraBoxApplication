package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import Devices.DanfossRadiator;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Remus
 *
 */
public class DanfossRadiatorTest {


	@Test
	public void testToString() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		String actualToString = danfoss.toString();
		String expectedToString = "Name: name" + " Id: " +  1 + " AltID: " + "altid" + " Category: " + 2 + " Subcategory: " + 3 + " Room: " +
				1 + " Parent: " + 1 + " SetPoint: " + 10 + " Heat: " + 30 + " Cool: " + 18 + " Commands: " + "commands" + " BatteryLevel: " + 
				74 + " Mode: " + "mode" + " State:  " + 2 + " Comment: " + "comment";

		assertEquals(expectedToString, actualToString);

	}

	@Test 
	public void testGetDetails() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		String actualDetails = danfoss.getDetails();
		String expectedDetails = "Name: " + "name" + "\nId: " + 1 + "\nAltID: " + "altid"
				+ "\nCategory: " + 2 + "\nSubcategory: " + 3
				+ "\nRoom: " + 1 + "\nParent: " + 1 + "\nSetPoint: " + 10 + "\nHeat: "
				+ 30 + "\nCool: " + 18 + "\nCommands: " + "commands"
				+ "\nBatteryLevel: " + 74 + "\nMode: " + "mode"
				+ "\nState:  " + 2 + "\nComment: " + "comment";
		assertEquals(expectedDetails, actualDetails);
	}

	@Test
	public void testReadingFromSQL() {
		DanfossRadiator danfoss = new DanfossRadiator();
		String actualReading = danfoss.readingFromSQL(12345, 56789);
		String expectedReading = "SELECT heat, reading_date FROM Reading WHERE id =  '" + danfoss.getId() + "' AND reading_date >='" + 12345
				+ "' AND reading_date <='" + 56789 + "'";

		assertEquals(expectedReading, actualReading);
	}

	@Test
	public void testGetPane() {
		fail("Not yet implemented");
        DanfossRadiator danfoss1 = new DanfossRadiator();
		Pane actualPane = danfoss1.getPane();

		assertTrue(actualPane instanceof Pane);
	}

	@Test
	public void testGetBatterylevel() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		int actualBattery = danfoss.getBatterylevel(), expectedBattery = 74;
		assertEquals(expectedBattery, actualBattery);

	}

	@Test
	public void testDanfossRadiator() {
		DanfossRadiator danfoss1 = new DanfossRadiator();

		assertEquals(danfoss1.getImage(), "radiator.jpg");
		assertEquals(danfoss1.getReadingName(), "heat");
		assertTrue(danfoss1 instanceof DanfossRadiator);
		assertEquals(null, danfoss1.getMode());
	}

	@Test
	public void testDanfossRadiatorStringIntStringIntIntIntIntIntIntIntIntStringIntStringIntString() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");

		assertEquals(danfoss.getName(), "name");
		assertEquals(danfoss.getId(), 1);
		assertEquals(danfoss.getAltid(), "altid");
		assertEquals(danfoss.getCategory(), 2);
		assertEquals(danfoss.getSubcategory(), 3);
		assertEquals(danfoss.getRoom(), 1);
		assertEquals(danfoss.getParent(), 1);
		assertEquals(danfoss.getSetpoint(), 10);
		assertEquals(danfoss.getHeat(), 30);
		assertEquals(danfoss.getCool(), 18);
		assertEquals(danfoss.getCommands(), "commands");
		assertEquals(danfoss.getBatterylevel(), 74);
		assertEquals(danfoss.getMode(), "mode");
		assertEquals(danfoss.getState(), 2);
		assertEquals(danfoss.getComment(), "comment");

	}

	@Test
	public void testGetReading() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		int actualReading = danfoss.getReading(), expectedReading = 12345;

		assertEquals(expectedReading, actualReading);
	}

	@Test
	public void testGetSetpoint() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		int actualSetPoint = danfoss.getSetpoint(), expectedSetPoint = 10;

		assertEquals(expectedSetPoint, actualSetPoint);

	}

	@Test
	public void testGetHeat() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		int actualHeat = danfoss.getHeat(), expectedHeat = 30;

		assertEquals(expectedHeat, actualHeat);
	}

	@Test
	public void testGetCool() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		int actualCool = danfoss.getCool(), expectedCool = 18;

		assertEquals(expectedCool, actualCool);
	}

	@Test
	public void testGetCommands() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		String actualCoommands = danfoss.getCommands(), expectedCommands = "commands";

		assertEquals(expectedCommands, actualCoommands);
	}

	@Test
	public void testGetMode() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		String actualMode = danfoss.getMode(), expectedMode = "mode";

		assertEquals(expectedMode, actualMode);
	}

	@Test
	public void testGetState() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		int actualState = danfoss.getState(), expectedState = 2;

		assertEquals(expectedState, actualState);
	}

	@Test
	public void testGetComment() {
		DanfossRadiator danfoss = new DanfossRadiator("name", 1, "altid", 2, 3, 1, 1, 12345, 10, 30, 18, "commands", 74, "mode", 2, "comment");
		String actualMode = danfoss.getComment(), expectedMode = "comment";

		assertEquals(expectedMode, actualMode);
	}

	@Test
	public void testSetSetpoint() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setSetpoint(1);
		int actualSetPoint = danfoss.getSetpoint(), expectedSetPoint = 1;

		assertEquals(expectedSetPoint, actualSetPoint);
	}

	@Test
	public void testSetHeat() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setHeat(30);
		int actualHeat = danfoss.getHeat(), expectedHeat = 30;

		assertEquals(expectedHeat, actualHeat);
	}

	@Test
	public void testSetCool() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setCool(14);
		int actualCool = danfoss.getCool(), expectedCool = 14;

		assertEquals(expectedCool, actualCool);
	}

	@Test
	public void testSetCommands() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setCommands("command1");
		String actualCommands = danfoss.getCommands(), expectedCommands = "command1";

		assertEquals(expectedCommands, actualCommands);
	}

	@Test
	public void testSetBatterylevel() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setBatterylevel(99);
		int actualBattery = danfoss.getBatterylevel(), expectedBattery = 99;

		assertEquals(expectedBattery, actualBattery);
	}

	@Test
	public void testSetMode() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setMode("mode1");
		String actualMode = danfoss.getMode(), expectedMode = "mode1";

		assertEquals(expectedMode, actualMode);
	}

	@Test
	public void testSetState() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setState(2);
		int actualState = danfoss.getState(), expectedState = 2;

		assertEquals(expectedState, actualState);
	}

	@Test
	public void testSetComment() {
		DanfossRadiator danfoss = new DanfossRadiator();
		danfoss.setComment("comment1");
		String actualComment = danfoss.getComment(), expectedComment = "comment1";

		assertEquals(expectedComment, actualComment);
	}

}

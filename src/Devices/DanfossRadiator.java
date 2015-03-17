package Devices;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DanfossRadiator extends Device implements Sensor {
	int setpoint;
	int heat;
	int cool;
	String commands;
	int batterylevel;
	String mode;
	int state;
	String comment;

	public DanfossRadiator() {
		this.image = "radiator.jpg";
		this.readingName = "heat";
	}

	public DanfossRadiator(String name, int id, String altid, int category,
			int subcategory, int room, int parent, int currentReading,
			int setpoint, int heat, int cool, String commands,
			int batterylevel, String mode, int state, String comment) {
		super(name, id, altid, category, subcategory, room, parent,
				"radiator.jpg", "heat", currentReading);
		this.setpoint = setpoint;
		this.heat = heat;
		this.cool = cool;
		this.commands = commands;
		this.batterylevel = batterylevel;
		this.mode = mode;
		this.state = state;
		this.comment = comment;
	}

	@Override
	public String toString() {
		return super.toString() + " SetPoint: " + setpoint + " Heat: " + heat
				+ " Cool: " + cool + " Commands: " + commands
				+ " BatteryLevel: " + batterylevel + " Mode: " + mode
				+ " State:  " + state + " Comment: " + comment;
	}

	@Override
	public int getReading() {
		return currentReading;
	}


	@Override
	public String getDetails() {
		return super.getDetails() + "\nSetPoint: " + setpoint + "\nHeat: "
				+ heat + "\nCool: " + cool + "\nCommands: " + commands
				+ "\nBatteryLevel: " + batterylevel + "\nMode: " + mode
				+ "\nState:  " + state + "\nComment: " + comment;
	}

	public Pane getPane() {
		Pane pane = super.getPane();
		Label reading = new Label("Heat: " + getReading() + "*C");
		reading.setLayoutX(200);
		reading.setLayoutY(100);
		Label battery = new Label(this.batterylevel + "%");
		battery.setId("batteryLevel");
		battery.setLayoutY(37);
		battery.setLayoutX(507);
		ImageView batteryImage = new ImageView(new Image(DanfossRadiator.class.getResource("/Resources/battery-medium.png").toExternalForm()));
		batteryImage.setLayoutY(20);
		batteryImage.setLayoutX(500);
		pane.getChildren().addAll(reading, batteryImage, battery);
		return pane;
	}

	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String(
				"SELECT heat, reading_date FROM Reading WHERE id =  '"
						+ getId() + "' AND reading_date >='" + startDate
						+ "' AND reading_date <='" + endDate + "'");
	}
}

package Devices;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
		Label reading = new Label(getReading() + "°" + "C");
		reading.setLayoutX(200);
		reading.setLayoutY(25);
		reading.setId("readingLabel");
		
		final Button off = new Button("Off");
		off.setLayoutX(265);
		off.setLayoutY(100);
		off.setMinWidth(60);
		off.setMaxWidth(60);
		
		final Button heat = new Button("Heat");
		heat.setLayoutX(200);
		heat.setLayoutY(100);
		heat.setMinWidth(60);
		heat.setMaxWidth(65);
		
		if(mode == "off") {
			heat.setId("heatButtonRed");
			off.setId("heatButtonGray");
		}else {
			heat.setId("heatButtonGray");
			off.setId("heatButtonRed");
		}
		
		EventHandler<ActionEvent> heatButtonHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (heat.getId() == "heatButtonRed") {
					heat.setId("heatButtonGray");
					off.setId("heatButtonRed");			
				} else {
					heat.setId("heatButtonRed");
					off.setId("heatButtonGray");
				}
			}
		};
		
		off.setOnAction(heatButtonHandler);
		heat.setOnAction(heatButtonHandler);
		
		pane.getChildren().addAll(reading, off, heat);
		return pane;
	}

	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String(
				"SELECT heat, reading_date FROM Reading WHERE id =  '"
						+ getId() + "' AND reading_date >='" + startDate
						+ "' AND reading_date <='" + endDate + "'");
	}

	public int getSetpoint() {
		return setpoint;
	}

	public int getHeat() {
		return heat;
	}

	public int getCool() {
		return cool;
	}

	public String getCommands() {
		return commands;
	}

	public int getBatterylevel() {
		return batterylevel;
	}

	public String getMode() {
		return mode;
	}

	public int getState() {
		return state;
	}

	public String getComment() {
		return comment;
	}

	public void setSetpoint(int setpoint) {
		this.setpoint = setpoint;
	}

	public void setHeat(int heat) {
		this.heat = heat;
	}

	public void setCool(int cool) {
		this.cool = cool;
	}

	public void setCommands(String commands) {
		this.commands = commands;
	}

	public void setBatterylevel(int batterylevel) {
		this.batterylevel = batterylevel;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}

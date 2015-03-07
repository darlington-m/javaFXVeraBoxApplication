package Devices;
import java.util.Date;

import GUI.VeraGUI;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class DanfossRadiator extends Device implements Sensor{
	int setpoint;
	int heat;
	int cool;
	String commands;
	int batterylevel;
	String mode;
	int state;
	String comment;
	
	public DanfossRadiator(){
        image = "radiator.jpg";
        readingName = "heat";        
    }
	@Override
	public String toString(){
		return super.toString() + " SetPoint: " +setpoint
				+ " Heat: " + heat
				+ " Cool: " + cool
				+ " Commands: " + commands
				+ " BatteryLevel: " + batterylevel
				+ " Mode: " + mode
				+ " State:  " + state
				+ " Comment: " +comment; 
	}

	@Override
	public int getReading() {
		return heat;
	}

	@Override
	public String readingToSQL() {
        return new String("INSERT INTO Reading " 
        + " (reading_date, reading_device_name, id, altid, category, subcategory, room, parent, heat) VALUES ('" 
        + new Date() + "', '"
        + getName() + "', '"
        + getId() + "',  '"
        + getAltid() + "',  '"
        + getCategory() + "',  '"
        + getSubcategory() + "',  '"
        + getRoom() + "',  '"
        + getParent() + "',  '"
        + getReading()  +  "')");
	}
	
	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String("SELECT heat, reading_date FROM Reading WHERE id =  '" + getId() + "' AND reading_date >='" + startDate + "' AND reading_date <='" + endDate + "'");
	}
	
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nSetPoint: " +setpoint + 
    			"\nHeat: " +heat + 
    			"\nCool: " + cool + 
    			"\nCommands: " +commands + 
    			"\nBatteryLevel: " +batterylevel + 
    			"\nMode: " +mode + 
    			"\nState:  " +state + 
    			"\nComment: " +comment; 
    }
    public Pane getPane(){
    	Pane pane = super.getPane();
		Label reading = new Label("Reading: " + getReading());
		reading.setLayoutX(200);
		reading.setLayoutY(100);
		pane.getChildren().addAll(reading);
		return pane;
    }
}
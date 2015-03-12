package Devices;

import java.util.Date;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class TemperatureSensor extends Device implements Sensor {

	private int temperature;

	public TemperatureSensor() {
		super.setImage("temperature.png");
		readingName = "temperature";
	}
	
	public TemperatureSensor(String name, int id, String altid, int category,
			int subcategory, int room, int parent, int currentReading) {
		super(name, id, altid, category, subcategory, room, parent, "temperature.png", "temperature", currentReading);
	}


	@Override
	public int getReading() {
		return temperature;
	}

	@Override
	public String readingToSQL() {
		return new String(
				"INSERT INTO Reading (reading_date, reading_device_name, id, altid, category, subcategory, room, parent, temperature) VALUES ('"
						+ new Date()
						+ "', '"
						+ getName()
						+ "', '"
						+ getId()
						+ "',  '"
						+ getAltid()
						+ "',  '"
						+ getCategory()
						+ "',  '"
						+ getSubcategory()
						+ "',  '"
						+ getRoom()
						+ "',  '" 
						+ getParent() 
						+ "',  '" 
						+ getReading() 
						+ "')");
	}
    
	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String("SELECT temperature,reading_date FROM Reading WHERE id =  '" + getId()  + "' AND reading_date >='" + startDate + "' AND reading_date <='" + endDate + "'");
	}
    @Override
    public String toString(){
		return super.toString() + " Temperature: " + temperature;
    }
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nTemperature: " + temperature;
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

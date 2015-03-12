package Devices;

import java.util.Date;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class FourInOne extends Device {

	int batterylevel;
	int temperature;
	int light;
	int humidity;
	int armedtripped;
	int armed;
	int state;
	String comment;
	int tripped;
	int lasttrip;

	public FourInOne() {
		super.setImage("4in1.png");
	}
	
	public FourInOne(String name, int id, String altid, int category,
			int subcategory, int room, int parent, int temperature, int light, int humidity) {
		super(name, id, altid, category, subcategory, room, parent, "4in1.jpg", "", 0);
	}

	public String toString() {
		return super.toString() + " Battery Level: " + batterylevel
				+ " Temperature: " + temperature
				+ " Light: " + light + " Humidity: "
				+ humidity + " ArmedTripped: "
				+ armedtripped + " Armed: " + armed + " State: " + state
				+ " Comment: " + comment + " Tripped: " + tripped
				+ " LastTrip: " + lasttrip;
	}

	public String readingToSQL() {
		return "INSERT INTO Reading (reading_date, reading_device_name, id, altid, category, subcategory, room, parent, temperature, humidity, light) VALUES ('"
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
				+ temperature 
				+ "',  '" 
				+ humidity
				+ "',  '" 
				+ light
				+ "')";
	}

	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String(
				"SELECT humidity,light,heat, reading_date FROM Reading WHERE id =  '"
						+ getId() + "' AND reading_date >='" + startDate
						+ "' AND reading_date <='" + endDate + "'");
	}

	@Override
	public String getDetails() {
		return super.getDetails() + "\nTemperature: "
				+ temperature + "\nLight: "
				+ light + "\nHumidity: "
				+ humidity;
	}

	public Pane getPane() {
		Pane pane = super.getPane();
		Label light = new Label("Reading: " + this.light);
		light.setLayoutY(75);
		light.setLayoutX(200);
		Label temp = new Label("Reading: "
				+ this.temperature);
		temp.setLayoutY(100);
		temp.setLayoutX(200);
		Label humidity = new Label("Reading: "
				+ this.humidity);
		humidity.setLayoutY(125);
		humidity.setLayoutX(200);
		pane.getChildren().addAll(light, temp, humidity);
		return pane;
	}
}

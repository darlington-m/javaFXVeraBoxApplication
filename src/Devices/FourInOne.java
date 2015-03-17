package Devices;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	String[] readingNames = new String[4];
	
	
	public FourInOne() {
		super.setImage("4in1.png");
	}
	
	public FourInOne(String name, int id, String altid, int category,
			int subcategory, int room, int parent, int temperature, int light, int humidity,
			int armedTripped,int batterylevel) {
		super(name, id, altid, category, subcategory, room, parent, "4in1.png", "", 0);
		this.temperature = temperature;
		this.light = light;
		this.humidity = humidity;
		this.armedtripped = armedTripped;
		this.batterylevel = batterylevel;
		readingNames[0] = "temperature"; 
		readingNames[1] = "light"; 
		readingNames[2] = "humidity"; 
		readingNames[3] = "armedTripped"; 
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


	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String(
				"SELECT humidity,light,heat, reading_date FROM Reading WHERE id =  '"
						+ getId() + "' AND reading_date >='" + startDate
						+ "' AND reading_date <='" + endDate + "'");
	}
	
	public String readingFromSQL(String reading, long startDate, long endDate) {
		return new String(
				"SELECT " + reading + ", reading_date FROM Reading WHERE id =  '"
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
		Label light = new Label("Light: " + this.light);
		light.setLayoutY(75);
		light.setLayoutX(200);
		Label temp = new Label("Temperature: "
				+ this.temperature);
		temp.setLayoutY(100);
		temp.setLayoutX(200);
		Label humidity = new Label("Humidity: "
				+ this.humidity);
		humidity.setLayoutY(125);
		humidity.setLayoutX(200);
		Label armedTripped = new Label("Armed Tripped: "
				+ this.armedtripped);
		armedTripped.setLayoutY(150);
		armedTripped.setLayoutX(200);
		Label battery = new Label(this.batterylevel + "%");
		battery.setId("batteryLevel");
		battery.setLayoutY(37);
		battery.setLayoutX(407);
		ImageView batteryImage = new ImageView(new Image(FourInOne.class.getResource("/Resources/battery-medium.png").toExternalForm()));
		batteryImage.setLayoutY(20);
		batteryImage.setLayoutX(400);
		pane.getChildren().addAll(light, temp, humidity, armedTripped,batteryImage,battery);
		return pane;
	}
}

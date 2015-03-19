package Devices;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
	ArrayList<String> readingNames = new ArrayList<String>();
	
	
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
		readingNames.add("temperature");
		readingNames.add("humidity");
		readingNames.add("light");
		readingNames.add("armedtripped");
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
		light.setLayoutY(25);
		light.setLayoutX(200);
		Label temp = new Label("Temperature: "
				+ this.temperature);
		temp.setLayoutY(50);
		temp.setLayoutX(200);
		Label humidity = new Label("Humidity: "
				+ this.humidity);
		humidity.setLayoutY(75);
		humidity.setLayoutX(200);
		Label armedTripped = new Label("Armed Tripped: "
				+ this.armedtripped);
		armedTripped.setLayoutY(100);
		armedTripped.setLayoutX(200);
		
		// Switch--------------------------------------------
		
		Label armedOnLabel = new Label("On");
		armedOnLabel.setPrefSize(40, 40);
		armedOnLabel.setLayoutX(250);
		armedOnLabel.setLayoutY(50);

		Label armedOffLabel = new Label("Off");
		armedOffLabel.setPrefSize(40, 40);
		armedOffLabel.setLayoutX(290);
		armedOffLabel.setLayoutY(50);
	
		final Button armedButton = new Button();
		if (armed == 1){
			armedButton.setText("On");
			armedButton.setLayoutX(244);
		} else {
			armedButton.setText("Off");
			armedButton.setLayoutX(283);
		}
		armedButton.setLayoutY(58);
		
		EventHandler<ActionEvent> armedButtonHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (armedButton.getText().equals("Off")){
					armedButton.setText("On");
					armedButton.setLayoutX(244);
				} else {
					armedButton.setText("Off");
					armedButton.setLayoutX(283);
				}
			}
		};
		
		armedButton.setOnAction(armedButtonHandler);

		
		pane.getChildren().addAll(armedOnLabel, armedOffLabel, armedButton);
		return pane;
	}

	public int getBatterylevel() {
		return batterylevel;
	}

	public int getTemperature() {
		return temperature;
	}

	public int getLight() {
		return light;
	}

	public int getHumidity() {
		return humidity;
	}

	public int getArmedtripped() {
		return armedtripped;
	}

	public int getArmed() {
		return armed;
	}

	public int getState() {
		return state;
	}

	public String getComment() {
		return comment;
	}

	public int getTripped() {
		return tripped;
	}

	public int getLasttrip() {
		return lasttrip;
	}

	public void setBatterylevel(int batterylevel) {
		this.batterylevel = batterylevel;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public void setArmedtripped(int armedtripped) {
		this.armedtripped = armedtripped;
	}

	public void setArmed(int armed) {
		this.armed = armed;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setTripped(int tripped) {
		this.tripped = tripped;
	}

	public void setLasttrip(int lasttrip) {
		this.lasttrip = lasttrip;
	}
	
	public void setReadingName(String readingName){
		this.readingName = readingName;
	}
	
	public ArrayList<String> getReadingNames(){
		return readingNames;
	}
	
	
}

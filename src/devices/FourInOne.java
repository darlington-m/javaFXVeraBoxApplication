package devices;

import java.net.HttpURLConnection;
import java.net.URL;
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

	public Pane getPane(final String ip) {
		System.out.println("Test: "+ ip);
		
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
		
		final Label armedBackgroundLabel = new Label();
		armedBackgroundLabel.setId("armedOnLabel");
		armedBackgroundLabel.setPrefSize(70, 25);
		armedBackgroundLabel.setMaxHeight(25);
		armedBackgroundLabel.setMinHeight(25);
		armedBackgroundLabel.setLayoutX(220);
		armedBackgroundLabel.setLayoutY(58);
		
		final Label armedLabel = new Label();
		armedLabel.setLayoutY(93);
	
		final Button armedButton = new Button();
		armedButton.setId("armedButton");
		armedButton.setPrefSize(45, 42);
		
		if (armed == 1){
			armedButton.setLayoutX(245);
			armedLabel.setLayoutX(237);
			armedLabel.setText("Armed");
			armedLabel.setId("armedLabelRed");
			armedBackgroundLabel.setId("armedOnLabel");
		} else {
			
			armedButton.setLayoutX(219);
			armedLabel.setLayoutX(230);
			armedLabel.setText("Bypassed");
			armedLabel.setId("armedLabelGray");
			armedBackgroundLabel.setId("armedOffLabel");
		}
		armedButton.setLayoutY(49);
		
		EventHandler<ActionEvent> armedButtonHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (armedButton.getLayoutX() == 245){
					executeHttp("http://146.87.40.27:3480/data_request?id=lu_action&output_format=json&DeviceNum=" + id+ "&serviceId=urn:micasaverde-com:serviceId:SecuritySensor1&action=SetArmed&newArmedValue=0");
					armed = 0;
					armedButton.setLayoutX(219);
					armedLabel.setLayoutX(230);
					armedLabel.setText("Bypassed");
					armedLabel.setId("armedLabelGray");
					armedBackgroundLabel.setId("armedOffLabel");
				} else {
					executeHttp("http://146.87.40.27:3480/data_request?id=lu_action&output_format=json&DeviceNum=" + id+ "&serviceId=urn:micasaverde-com:serviceId:SecuritySensor1&action=SetArmed&newArmedValue=1");
					armed = 1;
					armedButton.setLayoutX(245);
					armedLabel.setLayoutX(237);
					armedLabel.setText("Armed");
					armedLabel.setId("armedLabelRed");
					armedBackgroundLabel.setId("armedOnLabel");
				}
			}
		};
		
		armedButton.setOnAction(armedButtonHandler);

		
		pane.getChildren().addAll(armedBackgroundLabel, armedButton, armedLabel);
		return pane;
	}
	
	private boolean executeHttp(String urlS) {
		// TODO Auto-generated method stub
		
		boolean check = false;

		try {
			try {
				URL url = new URL(urlS);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.connect();
				if (con.getResponseCode() == 200) {
					// Internet available
					check = true;
				}
			} catch (Exception exception) {
				// No Internet
				check = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return check;
		
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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class FourInOne extends Device {

	int batterylevel;
	TemperatureSensor temperatureSensor;
	LightSensor lightSensor;
	HumiditySensor humiditySensor;
	int armedtripped;
	int armed;
	int state;
	String comment;
	int tripped;
	int lasttrip;
	
	public FourInOne(){
        super.setImage("lightbulb1.jpg");
    }
	
	public String toString(){
		return super.toString() + " Battery Level: " + batterylevel + " Temperature: " + temperatureSensor.getReading() + " Light: " + lightSensor.getReading() + " Humidity: " + humiditySensor.getReading() + " ArmedTripped: " + armedtripped + " Armed: " + armed + " State: " + state + " Comment: " + comment + " Tripped: " + tripped + " LastTrip: " + lasttrip; 
	}
	
	public int getReading(){
		return 0;
	}
	public void setTemp(TemperatureSensor temperatureSensor){
		this.temperatureSensor = temperatureSensor;		
	}
	public void setLight(LightSensor lightSensor){
		this.lightSensor = lightSensor;
	}
	public void setHumidity(HumiditySensor humiditySensor){
		this.humiditySensor = humiditySensor;
	}
	public Pane getPane(){
		Pane pane = new Pane();
		pane.setPrefSize(600,200);
		pane.setStyle("-fx-background-color: #005C99");
		ImageView image = null;
			pane.setStyle("-fx-border-color: red; -fx-background-width: 2px;");
			
			System.out.println ("Resources/" + getImage());
			image = new ImageView(new Image(JsonToJava.class.getResource("Resources/lightbulb1.jpg").toExternalForm()));
			image.setLayoutX(50);
			image.setLayoutY(50);
			
			Text name = new Text(getName());
			name.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
			name.setLayoutX(200);
			name.setLayoutY(50);
			Text light = new Text("Reading: " + lightSensor.getLight());
			light.setLayoutY(75);
			light.setLayoutX(200);
			Text temp = new Text("Reading: " + temperatureSensor.getReading());
			temp.setLayoutY(100);
			temp.setLayoutX(200);
			Text  humidity = new Text("Reading: " + humiditySensor.getReading());
			humidity.setLayoutY(125);
			humidity.setLayoutX(200);
			pane.getChildren().addAll(image,name,light,temp,humidity);
			return pane;
	}
}

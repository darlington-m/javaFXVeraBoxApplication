import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


public class JsonToJava extends Application{
	static ArrayList<Device> devices = new ArrayList<Device>();
	static ArrayList<Room> roomList = new ArrayList<Room>();
	
	Scene scene;
	TabPane root;
	Tab tab1, tab2;
	static MySQLConnect conn;
	
	
	public static void main(String[] args) throws IOException {
		try{
			conn = new MySQLConnect();
			Reader reader = new InputStreamReader(new URL("http://146.87.65.48:3480/data_request?id=sdata&output_format=json").openStream(), "UTF-8");
			Gson gson = new Gson();
			//creates a class Data Object Holds 2 arrays: devices and rooms.
			Data d = gson.fromJson(reader, Data.class);
			// for every device in the devices array
			for(JsonElement x : d.getDevices()){
				// cast the element to an object
				JsonObject object = x.getAsJsonObject();
				// put to an object
				int xa = Integer.parseInt(object.get("id").toString());
				switch(xa){
				case 11 :
					FourInOne four = new FourInOne();
					TemperatureSensor temperaturesensor = gson.fromJson(object, TemperatureSensor.class);
					HumiditySensor humiditysensor = gson.fromJson(object, HumiditySensor.class);
					LightSensor lightsensor = gson.fromJson(object, LightSensor.class);

					four = gson.fromJson(object, FourInOne.class);

					four.setHumidity(humiditysensor);
					four.setLight(lightsensor);
					four.setTemp(temperaturesensor);
					
					devices.add(four);

					break;
				case 9 :
					DanfossRadiator radiator = new DanfossRadiator();
					radiator =  gson.fromJson(object, DanfossRadiator.class);
				//	System.out.println(radiator);
					devices.add(radiator);
					break;
				case 16 :
					DataMining datamining = new DataMining();
					datamining = gson.fromJson(object, DataMining.class);
					//System.out.println(datamining);
					devices.add(datamining);
					break;
				case 14 :
					HumiditySensor humiditySensor = new HumiditySensor();
					humiditySensor = gson.fromJson(object, HumiditySensor.class);
					System.out.println(humiditySensor);
					devices.add(humiditySensor);
					break;
				case 13 :
					LightSensor lightSensor = new LightSensor();
					lightSensor = gson.fromJson(object, LightSensor.class);
					//System.out.println(lightSensor);
					System.out.println(lightSensor.readingToSQL());
					conn.insertRow(lightSensor.readingToSQL());
					devices.add(lightSensor);
					break;
				case 12 :
					TemperatureSensor temperatureSensor = new TemperatureSensor();
					temperatureSensor = gson.fromJson(object, TemperatureSensor.class);
					//System.out.println(temperatureSensor);
					devices.add(temperatureSensor);
					break;       			
				}
			}
			
			// creates rooms and search through the devices and adds the correct device to the correct room.
			
			for(JsonElement rooms : d.getRooms()){
				JsonObject object = rooms.getAsJsonObject();
				Room room = gson.fromJson(object, Room.class);
				roomList.add(room);
					for(Device device : devices){
						if(device.getRoom() == room.getId()){
							room.addDeviceToRoom(device);
						}
					}
				}
		}catch (SocketException se){
			System.out.println("You are not connected to the internet");
		}
		launch(args);
	}
	
	
	public Pane createPane(Device device){
		Pane pane = new Pane();
		pane.setPrefSize(600,200);
		pane.setStyle("-fx-background-color: #005C99");
		ImageView image = null;
			pane.setStyle("-fx-border-color: red; -fx-background-width: 2px;");
			
			//pane.setStyle("-fx-background-color: #661400; -fx-border-radius: 50 50 50 50; -fx-background-radius: 50 50 50 50 ;");
			System.out.println ("Resources/" + (device).getImage());
			image = new ImageView(new Image(JsonToJava.class.getResource("Resources/lightbulb1.jpg").toExternalForm()));
			image.setLayoutX(50);
			image.setLayoutY(50);
			
			Text name = new Text(device.getName());
			name.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
			name.setLayoutX(200);
			name.setLayoutY(50);
			if(device instanceof FourInOne){
				Text light = new Text("Reading: " + ((FourInOne)device).lightSensor.getLight());
				light.setLayoutY(75);
				light.setLayoutX(200);
				Text temp = new Text("Reading: " + ((FourInOne)device).temperatureSensor.getReading());
				temp.setLayoutY(100);
				temp.setLayoutX(200);
				Text  humidity = new Text("Reading: " + ((FourInOne)device).humiditySensor.getReading());
				humidity.setLayoutY(125);
				humidity.setLayoutX(200);
				pane.getChildren().addAll(image,name,light,temp,humidity);
				return pane;
			}
			else{
				Text reading = new Text("Reading: " + ((Sensor) device).getReading());
				reading.setLayoutX(200);
				reading.setLayoutY(100);
				pane.getChildren().addAll(image,name,reading);
				return pane;
			}

	}


	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Software Architectures – Liam Jones");
	  	root = new TabPane();
	    scene = new Scene(root, 1000, 700);
	  	stage.setScene(scene);

	  	tab1 = new Tab();
	  	tab1.setText("First Tab");
	  	tab1.setClosable(false);
	  	root.getTabs().add(tab1);

	  	tab2 = new Tab();
	  	tab2.setText("Second Tab");
	  	tab2.setClosable(false);
	  	root.getTabs().add(tab2);
	  	
	  	Pane pane = new Pane();
	  	tab1.setContent(pane);
	  	
	  	
	  	Pane sideDisplay = new Pane();
	  	sideDisplay.setPrefWidth(125); // the width of the side bar....
	  	sideDisplay.setPrefHeight(scene.getHeight());
	  	sideDisplay.setStyle("-fx-border-color: red; -fx-border-width:2px;");
	  	
	  	Pane topDisplay  =  new Pane();
	  	topDisplay.setLayoutX(sideDisplay.getPrefWidth());
	  	topDisplay.setPrefHeight(100);  // the height of the top bar... 
	  	topDisplay.setPrefWidth(scene.getWidth()-sideDisplay.getPrefWidth());
	  	topDisplay.setStyle("-fx-border-color: green; -fx-border-width:2px;");

	  	
	  	Pane display = new Pane();
	  	display.setLayoutX(sideDisplay.getPrefWidth());
	  	display.setLayoutY(topDisplay.getPrefHeight());
	  	
	  	
	  	pane.getChildren().addAll(sideDisplay,topDisplay,display);
	  	
	  	display.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");

	  	final VBox vb = new VBox(30);
	  	
	  	ScrollBar sc = new ScrollBar();
	  	sc.setLayoutX(scene.getWidth()-sc.getWidth());
        sc.setPrefHeight(scene.getHeight());
        sc.setOrientation(Orientation.VERTICAL);
        sc.setMin(0);
        sc.setMax(1000);
        sc.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                    vb.setLayoutY(-new_val.doubleValue());
            }
        });
        
	  	for(Device device: devices){
	  			vb.getChildren().add(createPane(device));
	  	}
	  	display.getChildren().addAll(vb,sc);
	  	
	  	stage.show();
	  			
	}
}


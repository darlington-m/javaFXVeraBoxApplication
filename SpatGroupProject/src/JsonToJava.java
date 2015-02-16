import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


public class JsonToJava extends Application{
	static ArrayList<Device> devices = new ArrayList<Device>();
	static ArrayList<Room> roomList = new ArrayList<Room>();
	
	Scene scene;
	Pane root, display;
	Tab tab1, tab2;
	static MySQLConnect conn;
	Button dashboardButton, settingsButton,accountButton,logoutButton,sceneButton,back;
	
	EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent arg0) {
			display.getChildren().clear();
			if(arg0.getSource() == dashboardButton){
				displayDevices();
			}
			else if (arg0.getSource() == settingsButton){
				displaySettings();
			}
			else if (arg0.getSource() == accountButton){
				displayAccountInfo();
			}
			else if (arg0.getSource() == logoutButton){
				System.exit(0);
			}
			else if (arg0.getSource() == sceneButton){
				displayScenes();
			}
			else if (arg0.getSource() == back){
				displayDevices();
			}
			back.setVisible(false);
		}};
	
	
	public static void main(String[] args) throws IOException {
		try{
			conn = new MySQLConnect();
			Reader reader = new InputStreamReader(new URL("https://vera-us-oem-relay31.mios.com/relay/relay/relay/device/35111004/session/0C1EA38A3C2843C3F877885C6F34B2F1BC424D/port_3480/data_request?id=sdata&output_format=json").openStream(), "UTF-8");
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
					devices.add(radiator);
					break;
				case 16 :
					DataMining datamining = new DataMining();
					datamining = gson.fromJson(object, DataMining.class);
					devices.add(datamining);
					break;
				case 14 :
					HumiditySensor humiditySensor = new HumiditySensor();
					humiditySensor = gson.fromJson(object, HumiditySensor.class);
					devices.add(humiditySensor);
					break;
				case 13 :
					LightSensor lightSensor = new LightSensor();
					lightSensor = gson.fromJson(object, LightSensor.class);
					devices.add(lightSensor);
					break;
				case 12 :
					TemperatureSensor temperatureSensor = new TemperatureSensor();
					temperatureSensor = gson.fromJson(object, TemperatureSensor.class);
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
		pane.setId("devices");
		pane.setPrefSize(600,200);
		
		Rectangle image = new Rectangle(100,100);
		image.setUserData(device);
		image.setFill(new ImagePattern(new Image(JsonToJava.class.getResource("Resources/"+ device.getImage()).toExternalForm())));
		image.setLayoutX(30);
		image.setLayoutY(30);
		pane.setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				showDeviceDetails(image);
			}});
		
		Label name = new Label(device.getName());
		name.setId("DeviceName");
		name.setLayoutX(200);
		name.setLayoutY(20);
		if(device instanceof FourInOne){
			Label light = new Label("Reading: " + ((FourInOne)device).lightSensor.getLight());
			light.setLayoutY(75);
			light.setLayoutX(200);
			Label temp = new Label("Reading: " + ((FourInOne)device).temperatureSensor.getReading());
			temp.setLayoutY(100);
			temp.setLayoutX(200);
			Label  humidity = new Label("Reading: " + ((FourInOne)device).humiditySensor.getReading());
			humidity.setLayoutY(125);
			humidity.setLayoutX(200);
			pane.getChildren().addAll(image,name,light,temp,humidity);
			return pane;
		}
		else{
			Label reading = new Label("Reading: " + ((Sensor) device).getReading());
			reading.setLayoutX(200);
			reading.setLayoutY(100);
			pane.getChildren().addAll(image,name,reading);
			return pane;
		}

	}


	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Vera Box");
	  	root = new Pane();
	    scene = new Scene(root, 900, 600);
	  	stage.setScene(scene);
	  	stage.setResizable(false);
	  	
	  	scene.getStylesheets().add(JsonToJava.class.getResource("Resources/css.css").toExternalForm()); 	  	
	  	
	  	Pane sideDisplay = new Pane();
	  	sideDisplay.setId("sidePane");
	  	sideDisplay.setPrefSize(200,scene.getHeight()); // the width of the side bar....
	  	
	  	ImageView image = new ImageView(new Image(JsonToJava.class.getResource("Resources/oem_logo.png").toExternalForm()));
	  	image.setLayoutX(20);
	  	image.setLayoutY(30);
	  	
	  	VBox vBox = new VBox(0);
	  	vBox.setLayoutY((image.getLayoutY()+image.getImage().getHeight()) + 10);
	  	vBox.setStyle("-fx-padding: 30px 0 0 0");
	  	
	  	dashboardButton = new Button("Dashboard");
	  	dashboardButton.setOnAction(buttonHandler);
	  	
	  	settingsButton = new Button("Settings");
	  	settingsButton.setOnAction(buttonHandler);
	  	
	  	accountButton = new Button("Account Info");
	  	accountButton.setOnAction(buttonHandler);
	  	
	  	sceneButton = new Button("Scenes");
	  	sceneButton.setOnAction(buttonHandler);
	  	
	  	logoutButton = new Button("Logout");
	  	logoutButton.setOnAction(buttonHandler);
	  	
		back = new Button("Back");
		back.setOnAction(buttonHandler);
		back.setVisible(false);
	  	
	  	vBox.getChildren().addAll(dashboardButton,sceneButton,accountButton,settingsButton,logoutButton,back);
	  	sideDisplay.getChildren().addAll(vBox,image);
	  	
	  	Pane topDisplay = new Pane();
	  	topDisplay.setLayoutX(sideDisplay.getPrefWidth());
	  	topDisplay.setPrefSize((scene.getWidth()-sideDisplay.getPrefWidth()+10),100);
	  	topDisplay.setId("topDisplay");
	  	
	  	Label welcome = new Label("Welcome User");
	  	welcome.setId("WelcomeMessage");
	  	welcome.setLayoutX(20);
	  	welcome.setLayoutY(35);
	  	
	  	Label time = new Label();
	  	time.setId("time"); 
	  	SimpleDateFormat format = new SimpleDateFormat("EEE HH:mm:ss");
	  	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {  
	  	     @Override  
	  	     public void handle(ActionEvent event) {  
	  	          Calendar cal = Calendar.getInstance();  
	  	          time.setText(format.format(cal.getTime()));  
	  	     }
	  	}));
	  	timeline.setCycleCount(Animation.INDEFINITE);  
	  	timeline.play();  
	  	time.setLayoutX(590);
	  	time.setLayoutY(45);
	  	
	  	topDisplay.getChildren().addAll(welcome,time);
	  	
	  	display = new Pane();
	  	display.setId("deviceDisplay");
	  	display.setPrefSize((scene.getWidth()-sideDisplay.getPrefWidth()+10),(scene.getHeight()-topDisplay.getPrefHeight()+10));
	  	display.setLayoutX(sideDisplay.getPrefWidth());
	  	display.setLayoutY(topDisplay.getPrefHeight());  	
	  	root.getChildren().addAll(display,sideDisplay,topDisplay);
	  	

		back.setLayoutX(400);
		back.setLayoutY(400);
	  	stage.show();
	  			
	}

	public void displayDevices(){	
		Pane paneBackground = new Pane();
		paneBackground.setStyle("-fx-background-color:white; -fx-pref-width:600; -fx-pref-height: 40;");
		paneBackground.setLayoutX(45);		
		
	  	Pane sortingPane = new Pane();
	  	sortingPane.setPrefSize(600, 40);
	  	sortingPane.setLayoutX(45);
	  	sortingPane.setId("sortingPane");
	  	
	  	HBox hbox = new HBox(10);
	  	hbox.setStyle("-fx-padding:8px 0 0 30px");
	  	Label sort = new Label("Sort By \t\t");
	  	sort.setId("sortingLabel");
	  	Label roomText = new Label("Rooms:");
	  	roomText.setId("sortingLabel");
	  	ChoiceBox<String> rooms = new ChoiceBox<String>();
	  	rooms.getItems().addAll("Living Room","Kitchen","Study","Bedroom","BathRoom");
	  	rooms.setId("sortingDropDown");
	  	Label devicesText = new Label("\t\tDevices:");
	  	devicesText.setId("sortingLabel");
	  	ChoiceBox<String> deviceList = new ChoiceBox<String>();
	  	deviceList.getItems().addAll("4 in 1 Sensor", "Heat Sensor", "Light Sensor", "Danfoss Radiator");
	  	deviceList.setId("sortingDropDown");
	  	
	  	hbox.getChildren().addAll(sort,roomText,rooms,devicesText,deviceList);
	  	sortingPane.getChildren().addAll(hbox);	  	  	
	  	
	  	VBox vb = new VBox(30);
	  	vb.setLayoutY(sortingPane.getPrefHeight());
	  	vb.setStyle("-fx-padding: 0 0 0 45px");
	  	
	  	ScrollBar sc = new ScrollBar();
	  	sc.setLayoutX(696);
        sc.setPrefHeight(510);
        sc.setOrientation(Orientation.VERTICAL);
        sc.setMin(0);
        sc.setMax(1000);
        sc.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                    vb.setLayoutY(-new_val.doubleValue()+sortingPane.getPrefHeight());
            }
        });
        
	  	for(Device device: devices){
	  			vb.getChildren().add(createPane(device));
	  	}
	  	
	  	display.getChildren().addAll(vb,paneBackground,sortingPane,sc);
	}
	public void displayAccountInfo(){
		
	}
	public void displaySettings(){
		
	}
	public void displayScenes(){
		
	}
	
	public void showDeviceDetails(Rectangle image){
		display.getChildren().clear();
		back.setVisible(true);
		
		Device device = (Device)image.getUserData();
		Text text = new Text(device.getDetails());
		text.setId("deviceDetails");
		image.setLayoutX(50);
		image.setLayoutY(100);
		
		text.setLayoutX(300);
		text.setLayoutY(50);
		
		display.getChildren().addAll(image,text);
	}
}


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class JsonToJava extends Application{
	static ArrayList<Device> devices = new ArrayList<Device>();
	static ArrayList<Room> roomList = new ArrayList<Room>();
	
	Scene scene;
	Pane root, display;
	Tab tab1, tab2;
	static MySQLConnect conn;
	Button dashboardButton, settingsButton,accountButton,logoutButton,sceneButton;
	private static Timer readingTimer;
	
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
		}};
	
	
	public static void main(String[] args) throws IOException {
		
		readingTimer = new Timer();
		readingTimer.schedule(new ReadingTimer(), 0, 5000);
		

		
		launch(args);
	}
	
	
	public Pane createPane(Device device){
		return device.getPane();

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
	  	vBox.setLayoutY((image.getLayoutY()+image.getImage().getHeight()) + 10); // 
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
	  	
	  	vBox.getChildren().addAll(dashboardButton,sceneButton,accountButton,settingsButton,logoutButton);
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
	  	  	
	  	stage.show();
	  			
	}

	public void displayDevices(){
	  	VBox vb = new VBox(30);
	  	
	  	Pane sortingPane = new Pane();
	  	sortingPane.setPrefSize(600, 40);
	  	sortingPane.setLayoutX(30);
	  	sortingPane.setLayoutY(20);
	  	sortingPane.setId("sortingPane");
	  	
	  	HBox hbox = new HBox(10);
	  	hbox.setStyle("-fx-padding:15px 0 0 30px");
	  	Label sort = new Label("Sort By \t\t");
	  	Label roomText = new Label("Rooms:");
	  	ChoiceBox<String> rooms = new ChoiceBox<String>();
	  	rooms.getItems().addAll("Living Room","Kitchen","Study","Bedroom","BathRoom");
	  	Label devicesText = new Label("\t\tDevices:");
	  	ChoiceBox<String> deviceList = new ChoiceBox<String>();
	  	deviceList.getItems().addAll("4 in 1 Sensor", "Heat Sensor", "Light Sensor", "Danfoss Radiator");
	  	
	  	
	  	hbox.getChildren().addAll(sort,roomText,rooms,devicesText,deviceList);
	  	sortingPane.getChildren().add(hbox);
	  	
	  	
	  	ScrollBar sc = new ScrollBar();
	  	sc.setLayoutX(display.getPrefWidth()-sc.getWidth());
        sc.setPrefHeight(display.getHeight());
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
	  	display.getChildren().addAll(sortingPane,vb,sc);
	}
	public void displayAccountInfo(){
		
	}
	public void displaySettings(){
		
	}
	public void displayScenes(){}
	
	public void getDataFromJSON(){
		try{
			conn = new MySQLConnect();
			Reader reader = new InputStreamReader(new URL("http://146.87.65.48:3480/data_request?id=sdata&output_format=json").openStream(), "UTF-8");
			Gson gson = new Gson();
			GSONObjectFactory factory  = new GSONObjectFactory();
			//creates a class Data Object Holds 2 arrays: devices and rooms.
			Data d = gson.fromJson(reader, Data.class);
			// for every device in the devices array
			
			for(JsonElement x : d.getDevices()){
				
				devices.add(factory.toDeviceObject(x));
			}
			
			// creates rooms 
			
			for(JsonElement rooms : d.getRooms()){
				
				roomList.add(factory.toRoomObject(rooms));
					
			}
			
			addDevicesToRooms();
		}catch (Exception se){
			System.out.println("You are not connected to the internet");
		}
	}
	public static void addDevicesToRooms(){
		//search through the devices and adds the correct device to the correct room.
		for(Device device : devices){
			for(Room room : roomList){
				if(device.getRoom() == room.getId()){
					room.addDeviceToRoom(device);
				}
			}
		}
	}
}


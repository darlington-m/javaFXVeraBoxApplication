package GUI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import DataRetrival.JsonToJava;
import DataRetrival.MySQLConnect;
import DataRetrival.ReadingTimer;
import Devices.DanfossRadiator;
import Devices.Data;
import Devices.DataMining;
import Devices.Device;
import Devices.FourInOne;
import Devices.HumiditySensor;
import Devices.LightSensor;
import Devices.Room;
import Devices.Sensor;
import Devices.TemperatureSensor;
import Graphs.Charts;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class VeraGUI extends Application{

	MySQLConnect conn = new MySQLConnect();

	Scene scene;
	Pane root, display;
	Button dashboardButton, settingsButton,accountButton,logoutButton,sceneButton,back;

	EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent arg0) {
			display.getChildren().clear();
			
			switch(((Button) arg0.getSource()).getText()){

			case"Dashboard":
				displayDevices();
				break;
			case"Settings":
				displaySettings();
				break;
			case"Account":
				displayAccountInfo();
				break;
			case"Logout":
				System.exit(0);
				break;
			case"Scenes":
				displayScenes();
				break;
			case"Back":
				displayDevices();
				break;
			}
			back.setVisible(false);

		}};


		public static void main(String[] args) throws IOException {
			launch(args);
		}

		public Pane createPane(Device device){
			Pane pane = new Pane();
			pane.setId("devices");
			pane.setPrefSize(600,200);

			final Rectangle image = new Rectangle(100,100);
			image.setUserData(device);
			image.setFill(new ImagePattern(new Image(VeraGUI.class.getResource("/Resources/"+ device.getImage()).toExternalForm())));
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
				Label light = new Label("Reading: " + ((FourInOne)device).getLightSensor().getLight());
				light.setLayoutY(75);
				light.setLayoutX(200);
				Label temp = new Label("Reading: " + ((FourInOne)device).getTemperatureSensor().getReading());
				temp.setLayoutY(100);
				temp.setLayoutX(200);
				Label  humidity = new Label("Reading: " + ((FourInOne)device).getHumiditySensor().getReading());
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

			scene.getStylesheets().add(VeraGUI.class.getResource("/Resources/css.css").toExternalForm()); 	  	

			Pane sideDisplay = new Pane();
			sideDisplay.setId("sidePane");
			sideDisplay.setPrefSize(200,scene.getHeight()); // the width of the side bar....

			ImageView image = new ImageView(new Image(VeraGUI.class.getResource("/Resources/oem_logo.png").toExternalForm()));
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

			final Label time = new Label();
			time.setId("time"); 
			final SimpleDateFormat format = new SimpleDateFormat("EEE HH:mm:ss");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {  
				@Override  
				public void handle(ActionEvent event) {  
					Calendar cal = Calendar.getInstance();  
					time.setText(format.format(cal.getTime()));  
				}
			}));
			timeline.setCycleCount(Animation.INDEFINITE);  
			timeline.play();  
			time.setLayoutX(500);
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

			final Pane sortingPane = new Pane();
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
			rooms.setMaxWidth(100);
			Label devicesText = new Label("\t\tDevices:");
			devicesText.setId("sortingLabel");
			ChoiceBox<String> deviceList = new ChoiceBox<String>();
			deviceList.getItems().addAll("4 in 1 Sensor", "Heat Sensor", "Light Sensor", "Danfoss Radiator");
			deviceList.setId("sortingDropDown");
			deviceList.setMaxWidth(100);

			hbox.getChildren().addAll(sort,roomText,rooms,devicesText,deviceList);
			sortingPane.getChildren().addAll(hbox);	  	  	

			final VBox vb = new VBox(30);
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
			
			Test test = new Test();
			ArrayList<Device> devices = test.run();
		  	for(Device device: devices){
		  		System.out.println(device.getDetails());
		  		vb.getChildren().add(createPane(device));
		  	}
		  	display.getChildren().addAll(vb,paneBackground,sortingPane,sc);
			
//			try {
//				for(Device device: JsonToJava.getData()){
//					vb.getChildren().add(createPane(device));
//				}
//
//				display.getChildren().addAll(vb,paneBackground,sortingPane,sc);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			
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
			Label text = new Label(((Device) device).getName());
			text.setId("WelcomeMessage");
			text.setId("deviceDetails");
			image.setLayoutX(50);
			image.setLayoutY(30);

			text.setLayoutX(300);
			text.setLayoutY(5);
			System.out.println(device.getId());
			int rowStuff = device.getId();
			String rowGet = null;
			switch(rowStuff)
			{
			case 9 : 
				rowGet = "heat";
				break;
			case 12 : 
				rowGet = "temperature";
				break;
			case 13 : 
				rowGet = "light";
				break;
			case 14 : 
				rowGet = "humidity";
				break;
			case 11 : 
				rowGet = "4in1huehuehue";
				break;
			}

			System.out.println(device.readingFromSQL());
			try {
				ArrayList<Integer> tempArray = new ArrayList<Integer>();
				ResultSet results = conn.getRows(device.readingFromSQL());
				System.out.println(results);
				while (results.next())
				{
					String temp = results.getString(rowGet);
					if(!(temp == null))
					{
						int temp2 =  Integer.parseInt(temp);
						tempArray.add(temp2);
					}

				}

				Charts chart = new Charts(tempArray, device);
				chart.show(display);
			} 
			catch (SQLException e1) 
			{

			}
			try {
				conn.getRows(device.readingFromSQL());
			} catch (SQLException e) {
				e.printStackTrace();
			}

			display.getChildren().addAll(image,text);
		}
}
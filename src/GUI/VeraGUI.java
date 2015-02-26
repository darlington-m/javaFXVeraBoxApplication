package GUI;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
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
import DataRetrival.MySQLConnect;
import Devices.Device;
import Devices.FourInOne;
import Devices.Sensor;
import Graphs.Charts;


public class VeraGUI extends Application{

	MySQLConnect conn = new MySQLConnect();

	private Scene scene;
	private Pane root, display;
	private Button firstButton, secondButton,thirdButton,fourthButton,fifthButton,sixthButton;
	private DatePicker compareTo, compareFrom, secondCompareTo, secondCompareFrom;
	private VBox sideButtons;
	private RadioButton compareone;
	private ChoiceBox<String> graphType;
	private ArrayList<Integer> tempArray = new ArrayList<Integer>();
	private int compare2 = 0;
	private Rectangle tempImage = null;

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
			sixthButton.setVisible(false);
		}};
	
	EventHandler<ActionEvent> detailButtonHandler = new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent event) {
			if(compare2 == 0)
			{
					VBox dropdown = new  VBox(5);
					dropdown.setId("dropdown");
					Label compareLabel = new Label("Compare From");
					Label compareToLabel = new Label("Compare To");
					dropdown.getChildren().addAll(compareLabel, compareFrom,compareToLabel, compareTo);
					sideButtons.getChildren().add(1,dropdown);
					
					VBox dropdown2 = new VBox(5);
					dropdown.setId("dropdown");
					Label compareLabel2 = new Label("Compare From");
					HBox label = new HBox(15);
					label.getChildren().addAll(compareLabel2, compareone);
					Label compareToLabel2 = new Label("Compare To");
					dropdown.getChildren().addAll(label,secondCompareFrom,compareToLabel2,secondCompareTo);
					sideButtons.getChildren().add(2,dropdown2);
					compare2++;
			}
		}
		
	};

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
					changeButtons();
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
			image.setLayoutY(13);
			image.setId("logo");

			sideButtons = new VBox(0);
			sideButtons.setLayoutY((image.getLayoutY()+image.getImage().getHeight())-15);
			sideButtons.setStyle("-fx-padding: 15px 0 0 0");

			firstButton = new Button("Dashboard");
			firstButton.setOnAction(buttonHandler);
			firstButton.setId("sideButton");

			secondButton = new Button("Settings");
			secondButton.setOnAction(buttonHandler);
			secondButton.setId("sideButton");

			thirdButton = new Button("Account Info");
			thirdButton.setOnAction(buttonHandler);
			thirdButton.setId("sideButton");

			fourthButton = new Button("Logout");
			fourthButton.setOnAction(buttonHandler);
			fourthButton.setId("sideButton");
			
			fifthButton = new Button("Scenes");
			fifthButton.setOnAction(buttonHandler);
			fifthButton.setId("sideButton");

			sixthButton = new Button("Back");
			sixthButton.setOnAction(buttonHandler);
			sixthButton.setVisible(false);
			sixthButton.setId("sideButton");

			sideButtons.getChildren().addAll(firstButton,fifthButton,thirdButton,secondButton,fourthButton,sixthButton);
			sideDisplay.getChildren().addAll(sideButtons,image);

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

			sixthButton.setLayoutX(400);
			sixthButton.setLayoutY(400);
			
			compareTo = new DatePicker();
			compareFrom = new DatePicker();
			compareTo.setValue(LocalDate.now());
			compareFrom.setValue(compareTo.getValue().minusDays(1));
			compareTo.setId("datePicker");
			compareFrom.setId("datePicker");
			
			secondCompareTo = new DatePicker();
			secondCompareFrom = new DatePicker();
			secondCompareTo.setValue(LocalDate.now());
			secondCompareFrom.setValue(secondCompareTo.getValue().minusDays(1));
			secondCompareTo.setId("datePicker");
			secondCompareFrom.setId("datePicker");
			secondCompareTo.setDisable(true);
			secondCompareFrom.setDisable(true);
			
			//ToggleGroup group = new ToggleGroup();
			compareone = new RadioButton("Enabled");
			compareone.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					if(((RadioButton)arg0.getSource()).isSelected()){
						secondCompareTo.setDisable(false);
						secondCompareFrom.setDisable(false);
					}else{
						secondCompareTo.setDisable(true);
						secondCompareFrom.setDisable(true);
					}
				}});			
			graphType = new ChoiceBox<String>();
			graphType.getItems().addAll("Line Chart", "Bar Chart");
			graphType.setLayoutX(600);
			graphType.setLayoutY(20);
			
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
		
		private void displayScenes(){

		}

		private void changeButtons(){
			if(firstButton.getText().equals("Dashboard")){
				firstButton.setText("Compare");
				firstButton.setOnAction(detailButtonHandler);
			}else{
				firstButton.setText("Dashboard");
				firstButton.setOnAction(buttonHandler);

				secondButton.setText("Settings");
				secondButton.setOnAction(buttonHandler);

				thirdButton.setText("Account Info");
				thirdButton.setOnAction(buttonHandler);

				fourthButton.setText("Logout");
				fourthButton.setOnAction(buttonHandler);
				
				fifthButton.setText("Scenes");
				fifthButton.setOnAction(buttonHandler);

				sixthButton.setText("Back");
				sixthButton.setOnAction(buttonHandler);
			}
		}
		
		private void showDeviceDetails(Rectangle image){
			display.getChildren().clear();
			sixthButton.setVisible(true);
			tempImage =  image;
			
			final Device device = (Device)image.getUserData();
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
				tempArray = new ArrayList<Integer>();
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
				display.getChildren().addAll(image,text,graphType);
				Charts chart = new Charts(tempArray, device,"Line Chart");
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

			graphType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
				public void changed(ObservableValue<? extends String> source, String oldValue, String newValue){
					display.getChildren().remove(display.getChildren().size()-1); // removes old graph 
					Charts chart = new Charts(tempArray, device,newValue);
					chart.show(display);
					
				}
			});	
			
		}
}
package GUI;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

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
import javafx.stage.Stage;
import javafx.util.Duration;
import DataRetrival.MySQLConnect;
import Devices.Device;
import Graphs.Charts;


public class VeraGUI extends Application{

	MySQLConnect conn = new MySQLConnect();

	private Scene scene;
	private Pane root, display;
	private DatePicker compareTo, compareFrom, secondCompareTo, secondCompareFrom;
	private VBox sideButtons;
	private RadioButton compareone;
	private ChoiceBox<String> graphType;
	private ArrayList<Integer> tempArray = new ArrayList<Integer>();

	ArrayList<Button> buttons = new ArrayList<Button>();

	EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent arg0) {
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
			case"Compare":
				if(sideButtons.getChildren().get(1) instanceof VBox ){
					 changeButtons("details");
				}else{
					changeButtons("compare");
				}
				break;
			case"Back":
				displayDevices();
				break;
			case"Download CSV":
				System.out.println("Do CSV File");
				System.out.println("Date 1:" + compareTo.getValue() + "\n Date 2:" + compareFrom.getValue());
				break;
			}
		}};


			public static void main(String[] args) throws IOException {
				launch(args);
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
				sideButtons.setLayoutY((image.getLayoutY()+image.getImage().getHeight())-5);
				sideButtons.setStyle("-fx-padding: 15px 0 0 0");

				for(int x=0; x<6; x++){
					Button button = new Button();
					buttons.add(button);
					button.setId("sideButton");
					button.setOnAction(buttonHandler);
				}
							
				changeButtons("mainMenu");				
				
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

				compareTo = new DatePicker();
				compareFrom = new DatePicker();
				compareTo.setValue(LocalDate.now());
				compareFrom.setValue(compareTo.getValue().minusDays(1));
				compareTo.setId("datePicker");
				compareFrom.setId("datePicker");
				compareTo.valueProperty().addListener(new ChangeListener<LocalDate>(){

					@Override
					public void changed(ObservableValue<? extends LocalDate> arg0,LocalDate oldDate, LocalDate newDate) {
						System.out.println("Old Date :" + oldDate + "New Date :" + newDate);
					}});
				compareFrom.valueProperty().addListener(new ChangeListener<LocalDate>(){

					@Override
					public void changed(ObservableValue<? extends LocalDate> arg0,LocalDate oldDate, LocalDate newDate) {
						System.out.println("Old Date :" + oldDate + "New Date :" + newDate);
					}});

				secondCompareTo = new DatePicker();
				secondCompareFrom = new DatePicker();
				secondCompareTo.setValue(LocalDate.now());
				secondCompareFrom.setValue(secondCompareTo.getValue().minusDays(1));
				secondCompareTo.setId("datePicker");
				secondCompareFrom.setId("datePicker");
				secondCompareTo.setDisable(true);
				secondCompareFrom.setDisable(true);
				secondCompareTo.valueProperty().addListener(new ChangeListener<LocalDate>(){

					@Override
					public void changed(ObservableValue<? extends LocalDate> arg0,LocalDate oldDate, LocalDate newDate) {
						System.out.println("Old Date :" + oldDate + "New Date :" + newDate);
					}});
				secondCompareFrom.valueProperty().addListener(new ChangeListener<LocalDate>(){

					@Override
					public void changed(ObservableValue<? extends LocalDate> arg0,LocalDate oldDate, LocalDate newDate) {
						System.out.println("Old Date :" + oldDate + "New Date :" + newDate);
					}});

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
	// TESTING DOESNT ADD TO DATABASE CAN BE RAN FROM HOME			
				Test test = new Test();
				ArrayList<Device> devices = test.run();
				for(final Device device: devices){
					Pane pane = device.getPane();
					System.out.println(device.getDetails());
					pane.setOnMouseReleased(new EventHandler<MouseEvent>(){

						@Override
						public void handle(MouseEvent arg0) {
							showDeviceDetails(device);
							changeButtons("details");
						}});
					vb.getChildren().add(pane);
				}
				display.getChildren().addAll(vb,paneBackground,sortingPane,sc);
	// END OF TESTING
				
	// UN COMMENT WHEN ADDING TO DB AND ABLE TO GET DATA
				//			try {
				//				for(Device device: JsonToJava.getData()){
				//					Pane pane = device.getPane();
				//					pane.setOnMouseReleased(new EventHandler<MouseEvent>(){
				//						@Override
				//						public void handle(MouseEvent arg0) {
				//							showDeviceDetails(device);
				//							changeButtons("details");
				//						}});
				//					vb.getChildren().add(device.getPane());
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

			private void changeButtons(String name){
				sideButtons.getChildren().clear();
				java.util.List<String> names = new ArrayList<String>();
				switch(name){
				case"mainMenu":
					String[] words = {"Dashboard", "Settings", "Account Info", "Scenes", "Logout"};	
					names = Arrays.<String>asList(words);
					break;
				case"details":
					String[] words2 = {"Compare", "Download CSV", "Back", "Logout"};	
					names = Arrays.<String>asList(words2);
					break;
				case"compare":
					
					String[] words3 = {"Compare", "Download CSV", "Back", "Logout"};	
					names = Arrays.<String>asList(words3);
					
					VBox dropdown = new  VBox(5);
					dropdown.setId("dropdown");
					sideButtons.getChildren().add(dropdown);
					Label compareLabel = new Label("Compare From");
					Label compareToLabel = new Label("Compare To");
					Label compareLabel2 = new Label("Compare From");
					Label compareToLabel2 = new Label("Compare To");
					HBox label = new HBox(15);// this adds the enable button
					label.getChildren().addAll(compareLabel2, compareone);
					dropdown.getChildren().addAll(compareLabel,compareFrom,compareToLabel, compareTo,
												label,secondCompareFrom,compareToLabel2,secondCompareTo);
					break;				
				}
				int x=0;
				for(Button button : buttons){
					try{
						button.setText(names.get(x));
						button.setId("sideButton");
						button.setOnAction(buttonHandler);
						if(name.equals("compare") && x==0){
							sideButtons.getChildren().add(0,button);		
						}else{
							sideButtons.getChildren().add(button);
						}
					}catch(Exception e){
						button.setVisible(false);
					}
					x++;
				}
			}

			private void showDeviceDetails(final Device device){
				display.getChildren().clear();
				
				Button download = new Button("Download CSV");
				download.setOnAction(buttonHandler);
				download.setId("downloadButton");
				download.setLayoutX(400);
				download.setLayoutY(40);
				display.getChildren().addAll(graphType,download); // adds the dropdownbox for selecting different grpahs

				try {
					tempArray = new ArrayList<Integer>();
					ResultSet results = conn.getRows(device.readingFromSQL());
					System.out.println(device.readingFromSQL());
					while (results.next())
					{
						String temp = results.getString(device.getReadingName());
						if(!(temp == null))
						{
							int temp2 =  Integer.parseInt(temp);
							tempArray.add(temp2);
						}

					}
					display.getChildren().addAll(device.showDeviceDetails().getChildren());
					Charts chart = new Charts(tempArray, device,"Line Chart");
					chart.show(display);
				} 
				catch (SQLException e1) 
				{
					Label warning = new Label("Sorry No Graph Data Available");
					warning.setLayoutX(300);
					warning.setLayoutY(400);
					display.getChildren().add(warning);
				}
				// this adds a change listener to the drop down box and creates a new graph when you select one.
				graphType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
					public void changed(ObservableValue<? extends String> source, String oldValue, String newValue){
						display.getChildren().remove(display.getChildren().size()-1); // removes old graph 
						Charts chart = new Charts(tempArray, device,newValue);
						chart.show(display);

					}
				});
			}
			
			public void getGraph(LocalDate from, LocalDate to){
				
			}

}
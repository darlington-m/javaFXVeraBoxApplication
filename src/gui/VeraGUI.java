package gui;

import graphs.Charts;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
//import java.util.Optional;
import java.util.Timer;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
//import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import dataretrival.CurrentReadings;
import dataretrival.MySQLConnect;
import dataretrival.ReadingsUpdateTimer;
import devices.DanfossRadiator;
import devices.Device;
import devices.FourInOne;
import devices.Room;
import exports.CSV;

public class VeraGUI extends Application {

	MySQLConnect conn = new MySQLConnect();

	private Scene scene;
	private Stage stage;
	private Pane root, display, topDisplay;
	final private Pane sortingPane = new Pane(), loadingPane = new Pane();
	private Label time, welcome ;
	private long compareToDate, compareFromDate;
	private ChoiceBox<String> compareToHours, compareToMinutes,
			compareFromHours, compareFromMinutes;
	private DatePicker compareTo, compareFrom;
	final private VBox sideButtons = new VBox(0), vb = new VBox(30);
	private ChoiceBox<String> graphType, seperateGraphs;
	private ArrayList<Integer> readingsArray = new ArrayList<Integer>();
	private ArrayList<String> dateArray = new ArrayList<String>();
	private Device selectedDevice;
	private ArrayList<Room> roomsList;
	private ArrayList<Device> devicesList;
	private ArrayList<Device> devicesToCSV = new ArrayList<Device>();
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private ScrollBar sc;
	private CheckBox tempCheckBox;
	private CheckBox lightCheckBox;
	private CheckBox humidityCheckBox;
	private CheckBox armedTrippedCheckBox;
	private boolean loggedIn = false;
	private String userName;
	String ip = null;
	private ArrayList<Device> scenesSelectedDevices;
	private final Pane graphsPane = new Pane();

	public VeraGUI() {
		CurrentReadings curr = new CurrentReadings();
		this.roomsList = curr.getRooms();
		this.devicesList = curr.getAllDevices();
	}

	EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {

			if (!InternetConnectionCheck()) {
				if (((Button) arg0.getSource()).getText() == "Quit") {
					System.exit(0);
				} else if (((Button) arg0.getSource()).getText() == "Back"){
					display.getChildren().clear();
					changeButtons("mainMenu");
					showWelcomeSplash();
				} else {
					displayNoInternet();
				}
			} else if(!loggedIn) {
				if (((Button) arg0.getSource()).getText() == "Quit") {
					System.exit(0);
				} else if (((Button) arg0.getSource()).getText() == "Back"){
					display.getChildren().clear();
					changeButtons("mainMenu");
					showWelcomeSplash();
				}
			}else {

				switch (((Button) arg0.getSource()).getText()) {
				case "Dashboard":
					//displayLoading();
					displayDevices();					
					break;
				case "Settings":
					//displayLoading();
					changeButtons("settings");
					displaySettings();
					break;
				case "Graphs":
					//displayLoading();
					changeButtons("graphs");
					displayGraphs();
					break;
				case "Scenes":
					//displayLoading();
					displayScenes();
					break;
				case "Back":
					//displayLoading();
					display.getChildren().clear();
					changeButtons("mainMenu");
					displayDevices();
					break;
				case "Cancel":
					//displayLoading();
					displaySettings();
					break;
				case "Add a room":
					//displayLoading();
					changeButtons("addRoom");
					break;
				case "Download CSV":
					saveToCSV(devicesToCSV, "24");
					break;
				case "Advanced":
					displayAdvancedSettings();
					break;
				case "Quit":
					System.exit(0);
					break;
				}
			} 
		}
	};

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {

		stage.setTitle("Vera Box");
		root = new Pane();
		scene = new Scene(root, 1000, 666);
		stage.setScene(scene);
		stage.setResizable(false);

		//hopefully works
		scene.getStylesheets().add(
				VeraGUI.class.getResource("/resources/css.css")
						.toExternalForm());

		Pane sideDisplay = new Pane();
		sideDisplay.setId("sidePane");
		sideDisplay.setPrefSize(200, scene.getHeight()); // the width of the
															// side bar....

		ImageView image = new ImageView(new Image(VeraGUI.class.getResource(
				"/resources/oem_logo.png").toExternalForm()));
		image.setLayoutX(20);
		image.setLayoutY(13);
		image.setId("logo");

		sideButtons.setLayoutY((image.getLayoutY() + image.getImage()
				.getHeight()) - 5);
		sideButtons.setStyle("-fx-padding: 15px 0 0 0");

		for (int x = 0; x < 6; x++) {
			Button button = new Button();
			buttons.add(button);
			button.setId("sideButton");
			button.setOnAction(buttonHandler);
		}

		changeButtons("mainMenu");

		sideDisplay.getChildren().addAll(sideButtons, image);

		topDisplay = new Pane();
		topDisplay.setLayoutX(sideDisplay.getPrefWidth());
		topDisplay.setPrefSize(
				(scene.getWidth() - sideDisplay.getPrefWidth() + 10), 100);
		topDisplay.setId("topDisplay");

		welcome = new Label();
		welcome.setId("WelcomeMessage");
		welcome.setLayoutX(40);
		welcome.setLayoutY(40);

		time = new Label();
		time.setId("time");
		final SimpleDateFormat format = new SimpleDateFormat("EEE HH:mm:ss");
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Calendar cal = Calendar.getInstance();
						time.setText(format.format(cal.getTime()));
					}
				}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		time.setLayoutX(topDisplay.getPrefWidth() - 180);
		time.setLayoutY(45);

		topDisplay.getChildren().addAll(time);
		showLogin();

		display = new Pane();
		display.setId("deviceDisplay");
		display.setPrefSize(
				(scene.getWidth() - sideDisplay.getPrefWidth() + 10),
				(scene.getHeight() - topDisplay.getPrefHeight() + 10));
		display.setLayoutX(sideDisplay.getPrefWidth());
		display.setLayoutY(topDisplay.getPrefHeight());

		root.getChildren().addAll(display, sideDisplay, topDisplay);

		compareTo = new DatePicker();
		compareTo.setMaxWidth(110);
		compareFrom = new DatePicker();
		compareFrom.setMaxWidth(110);
		compareTo.setValue(LocalDate.now());
		compareFrom.setValue(compareTo.getValue().minusDays(1));
		compareTo.setId("datePicker");
		compareFrom.setId("datePicker");
		ChangeListener<LocalDate> dateChanger = new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0,
					LocalDate oldDate, LocalDate newDate) {
				// create graph compareTo.getValue() compareFrom.getValue();
				SimpleDateFormat format = new SimpleDateFormat("DD MM YYYY");

				String year = newDate.toString().substring(0, 4);
				System.out.println(year);
				String month = newDate.toString().substring(5, 7);
				System.out.println(month);
				String day = newDate.toString().substring(8, 10);
				System.out.println(day);
				String date = day + month + year;
				System.out.println(date);

				System.out.println(" Compare From: "
						+ compareFrom.getValue().toEpochDay());
				System.out.println(format.format(newDate.toEpochDay()));
				System.out.println(" Compare To:  " + compareTo.getValue());

			}
		};

		compareTo.valueProperty().addListener(dateChanger);
		compareFrom.valueProperty().addListener(dateChanger);

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (item.isAfter(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		};

		compareTo.setDayCellFactory(dayCellFactory);
		compareFrom.setDayCellFactory(dayCellFactory);

		stage.show();
		
		final Timer updateReadings = new Timer();
		updateReadings.schedule(new ReadingsUpdateTimer(this), 0, 3000000); // 5 minutes
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
				updateReadings.cancel();
			}
		});

		if (InternetConnectionCheck()) {
			showWelcomeSplash();
		} else {
			displayNoInternet();
		}
	}

	public void displayDevices() {	

		if(topDisplay.getLayoutY()!=0){
			topDisplay.setLayoutY(0);
			display.setLayoutY(topDisplay.getPrefHeight());
			display.setPrefHeight(scene.getHeight() - topDisplay.getPrefHeight() + 10);
		}	
		
		display.getChildren().clear();
		Pane paneBackground = new Pane();
		paneBackground
				.setStyle("-fx-background-color:white; -fx-pref-height: 40;");
		paneBackground.setLayoutX(0);
		paneBackground.setPrefWidth(display.getPrefWidth());

		sortingPane.setPrefSize(display.getWidth()-39, 40);
		sortingPane.setLayoutX(10);
		sortingPane.setLayoutY(10);
		sortingPane.setId("sortingPane");
		
		String[] roomNames = new String[roomsList.size() + 1];
		roomNames[0] = "All";
		int count = 1;
		for (Room room : roomsList) {
			roomNames[count] = room.getName();
			count++;
		}

		HBox hbox = new HBox(10);
		hbox.setStyle("-fx-padding:8px 0 0 30px");

		Label roomText = new Label("Select Room:");
		roomText.setId("sortingLabel");
		
		ObservableList<String> options = FXCollections.observableArrayList();
		final ComboBox<String> roomDropDown = new ComboBox<String>(options);
		roomDropDown.getItems().addAll(roomNames);
		roomDropDown.getSelectionModel().selectFirst();
		roomDropDown.setMaxWidth(100);
		roomDropDown.setMinWidth(100);
		roomDropDown.setLayoutY(-10);
		roomDropDown.setId("comboSty2");
		roomDropDown.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldString, String newString) {
				updateDashboard(sortRooms(newString));
			}
		});

		hbox.getChildren().addAll(roomText, roomDropDown);
		sortingPane.getChildren().addAll(hbox);

		vb.setLayoutY(sortingPane.getPrefHeight() );
		vb.setLayoutX(10);
		//vb.setStyle("-fx-padding: 0 0 0 45px");

		sc = new ScrollBar();
		sc.setLayoutX(display.getPrefWidth() - (display.getPrefWidth()/45) + 5);
		sc.setPrefHeight(display.getPrefHeight());
		sc.setOrientation(Orientation.VERTICAL);
		sc.setVisibleAmount(200);
		sc.setUnitIncrement(160);
		sc.setBlockIncrement(160);
		
		// get the total number of devices across all rooms
		
		// <------------------------- NEED TO CHECK HOW MANY DEVICES ARE IN A ROOM FOR SC
		
		sc.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				vb.setLayoutY(-new_val.doubleValue()
						+ sortingPane.getPrefHeight());
			}
		});
		// PLACE AFTER THE SCROLLBAR
		
		updateDashboard(sortRooms("All"));
		display.getChildren().addAll(vb, paneBackground, sortingPane, sc);
		
		root.getChildren().remove(loadingPane);
		
	}
	

	private ArrayList<Room> sortRooms(String roomToRetrieve) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		rooms.addAll(roomsList);
		if (roomToRetrieve.equals("All")) {
			return rooms;
		} else {
			// setup an iterator
			for (Iterator<Room> iterator = rooms.iterator(); iterator.hasNext();) {
				Room currentRoom = iterator.next();
				// if the room name doesnt match whats been selected then remove
				if (!currentRoom.getName().equals(roomToRetrieve)) {
					iterator.remove();
				}
			}
			return rooms;
		}
	}

	private void updateDashboard(ArrayList<Room> rooms) {
		int scrollBarSize = 0;
		
		vb.getChildren().clear();
		for (final Room room : rooms) {
			scrollBarSize++;
			Pane roomPane = room.getPane(sortingPane.getPrefWidth());
			VBox deviceBox = new VBox(10);
			deviceBox.setLayoutX(50);
			deviceBox.setLayoutY(50);
			for (final Device device : room.getDevices()) {
				scrollBarSize +=3;
				FlowPane pane = new FlowPane();
				pane.setPrefWidth(sortingPane.getPrefWidth() - 100);
//				pane.setMinWidth(780);
//				pane.setMaxWidth(780);
//				pane.setMaxHeight(250);
//				pane.setMinHeight(250);
//				
				Pane topPane = new Pane();
				topPane.setPrefWidth(sortingPane.getPrefWidth() - 100);
				topPane.setMinHeight(50);
				topPane.setMaxHeight(50);
				topPane.setId("deviceBoxTop");
				topPane.setStyle("-fx-background-color: #f4f4f4");
				
				Label name = new Label(device.getName());
				name.setId("DeviceName");
				name.setLayoutX(10);
				name.setLayoutY(10);
				
				topPane.getChildren().add(name);
				
				if (device instanceof FourInOne || device instanceof DanfossRadiator) {
					
					Label battery = new Label(device.getBatterylevel() + "%");
					battery.setId("batteryLevel");
					battery.setLayoutY(18);
					battery.setLayoutX(597);
					ImageView batteryImage = new ImageView(new Image(VeraGUI.class.getResource("/resources/battery-medium.png").toExternalForm()));
					batteryImage.setLayoutY(2);
					batteryImage.setLayoutX(590);
					topPane.getChildren().addAll(batteryImage, battery);
				}
				
				Pane botPane;
				
				if (device instanceof FourInOne){
					botPane = ((FourInOne)device).getPane(getIP());
				} else if (device instanceof DanfossRadiator) {
					botPane = ((DanfossRadiator)device).getPane(getIP());

				} else {
					botPane = device.getPane();

				}
				botPane.setPrefWidth(sortingPane.getPrefWidth() - 100);
				
				Button detailsBtn = new Button("Settings");
				detailsBtn.setId("devicePaneBtn");
				detailsBtn.setLayoutX(525);
				detailsBtn.setLayoutY(100);
				
				Button graphBtn = new Button("24hr Graph");
				graphBtn.setId("devicePaneBtn");
				graphBtn.setLayoutX(525);
				graphBtn.setLayoutY(60);
				
				graphBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						changeButtons("details");
						setSelectedDevice(device);
						try {
							ArrayList<Device> devices = new ArrayList<Device>();
							if (device instanceof FourInOne){
								device.setReadingName("armedtripped");
								devices.add(device);
							} else {
								devices.add(device);
							}
							
							show24hrGraph(devices, "24", display);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Button csvButton = new Button("Download CSV");
						csvButton.setId("passSubmit");
						csvButton.setLayoutX(620);
						csvButton.setLayoutY(25);
						csvButton.setMinWidth(150);
						
						csvButton.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								saveToCSV(devicesToCSV, "24");
							}
						});
						
						display.getChildren().add(csvButton);
						
					}
				});
				
				detailsBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						changeButtons("details");
						showDeviceSettings(device, room);
					}
				});
				
				botPane.getChildren().addAll(graphBtn, detailsBtn);
				pane.getChildren().addAll(topPane, botPane);
				
				deviceBox.getChildren().add(pane);
			}
			roomPane.getChildren().add(deviceBox);
			vb.getChildren().add(roomPane);
		}
		sc.setMax(scrollBarSize * 60);
	}
	

	public void addARoom() {
		display.getChildren().clear();
		final Label addRoom = new Label("Add a new room");
		final Label enterDetails = new Label("Please enter a name below");
		final Label warning = new Label("Please Enter a name");
		warning.setVisible(false);
		final TextField input = new TextField();
		final Button add = new Button("Add room");
		add.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (input.getText().matches("[a-zA-Z]*")) {
					// create room into database
				} else {
					warning.setVisible(true);
				}
			}
		});
		display.getChildren()
				.addAll(addRoom, enterDetails, warning, input, add);
	}
	
	public void displayGraphs() {	
		
		final Button createGraphButton = new Button("Generate Graph");
		final Button createCSVButton = new Button("Download CSV");
		
		if(display.getLayoutY()!=0){
			display.getChildren().clear();
			topDisplay.setLayoutY(-(topDisplay.getPrefHeight()));
			display.setLayoutY(0);
			display.setPrefHeight(root.getPrefHeight());
		
		
		
		FlowPane graphSettingsContainer = new FlowPane(); // Holds all below Panes
		
		final ScrollPane devicesScrollPane = new ScrollPane();
		final Pane devicesPane = new Pane(); 
		Pane filterPane = new Pane();  
		
		final ScrollPane graphScrollPane = new ScrollPane();	
		
		devicesScrollPane.setPrefSize(display.getWidth()-19,
				display.getHeight() / 3 * 0.80); // sets the layout of 1 pane on
												 // the top and two below, evenly
												 // spaces
		filterPane.setPrefSize(display.getWidth(),
				display.getHeight() / 3 * 0.75);
		
		graphScrollPane.setPrefSize(display.getWidth(),
				scene.getHeight());

		// -------------------------------- Setting up the devicesPane

		final ArrayList<String> selectedDevices = new ArrayList<String>(); 
		
		// and adds them to the local variable devices
		
		boolean prevFourInOne = false;

		double j = 0;
		
		for (int i = 0; i < devicesList.size(); i++) // for each device  a
													// pane with an image and a
													// label
		{
			final ImageView deviceImage = new ImageView(new Image(VeraGUI.class
					.getResource("/resources/" + devicesList.get(i).getImage())
					.toExternalForm())); // add the image

			deviceImage.setFitHeight(60); // image sizing
			deviceImage.setFitWidth(60);

			deviceImage.setLayoutX(65); // image layout
			deviceImage.setLayoutY(15);
			
			
			
			//If statement for adding further images for the 4in1 sensor
			// name of the device
			final Label deviceLabel = new Label(devicesList.get(i).getName()); 

			deviceLabel.setPrefWidth(180); // label sizing

			deviceLabel.setLayoutX(30); // label layout
			deviceLabel.setLayoutY(90);

			final Pane imagePane = new Pane(); // name
			// of
			// the
			// device
			imagePane.setId("selectedDevice");
			imagePane.setLayoutY(10);
			imagePane
					.setStyle("-fx-border-color:grey; -fx-border-width: 3; -fx-border-style: solid;");
			
			if (devicesList.get(i) instanceof FourInOne){
				imagePane.setPrefSize(288, 120); // sizing the pane
				
				deviceImage.setLayoutX(45);
				
				tempCheckBox = new CheckBox();
				tempCheckBox.setLayoutX(140);
				tempCheckBox.setLayoutY(10);
				tempCheckBox.setDisable(true);
				
				Label tempLabel = new Label("Temperature");
				tempLabel.setLayoutX(170);
				tempLabel.setLayoutY(10);

				lightCheckBox = new CheckBox();
				lightCheckBox.setLayoutX(140);
				lightCheckBox.setLayoutY(37);
				lightCheckBox.setDisable(true);
				
				Label lightLabel = new Label("Light");
				lightLabel.setLayoutX(170);
				lightLabel.setLayoutY(37);

				humidityCheckBox = new CheckBox();
				humidityCheckBox.setLayoutX(140);
				humidityCheckBox.setLayoutY(64);
				humidityCheckBox.setDisable(true);
				
				Label humidityLabel = new Label("Humidity");
				humidityLabel.setLayoutX(170);
				humidityLabel.setLayoutY(64);

				armedTrippedCheckBox = new CheckBox();
				armedTrippedCheckBox.setLayoutX(140);
				armedTrippedCheckBox.setLayoutY(91);
				armedTrippedCheckBox.setDisable(true);
				
				Label armedTrippedLabel = new Label("Armed Tripped");
				armedTrippedLabel.setLayoutX(170);
				armedTrippedLabel.setLayoutY(91);

				imagePane.getChildren().addAll(tempCheckBox,tempLabel, lightCheckBox,lightLabel, humidityCheckBox,humidityLabel, armedTrippedCheckBox, armedTrippedLabel);
				
			
				prevFourInOne = true;
			} else {
				imagePane.setPrefSize(188, 120); // sizing the pane
				prevFourInOne = false;
			}

			FadeTransition ft = new FadeTransition(Duration.millis(300),
					imagePane);
			ft.setFromValue(0.3);
			ft.setToValue(0.3);
			ft.setCycleCount(1);
			ft.setAutoReverse(false);

			ft.play();

			imagePane.setOnMouseClicked(new EventHandler<Event>() { // when the
						// pane is
						// clicked
						/*
						 * As we are making the program dynamic this needs to
						 * account for any number of devices being added. To do
						 * this we cannot have hard coded panes. A way around
						 * needing to do this is to change the width of the
						 * image page to be able to tell if the image has been
						 * selected or not 144 stands for false, 145 stands for
						 * true. So when you click on the image pane it will
						 * highlight around the pane and set the size to 145.
						 * When clicked again it will unhighlight and set the
						 * size back to 144.
						 */
						@Override
						public void handle(Event event) {
							if (imagePane.getWidth() == 188 || imagePane.getWidth() == 288) {
								imagePane
										.setStyle("-fx-border-color:#12805C; -fx-border-width: 3; -fx-border-style: solid;");
								if (imagePane.getWidth() == 188){
									imagePane.setPrefSize(189, 120);
								} else {
									imagePane.setPrefSize(289, 120);
								}
								if (deviceLabel.getText().equals("4 in 1 sensor")){
									tempCheckBox.setSelected(true);
									tempCheckBox.setDisable(false);
									lightCheckBox.setSelected(true);
									lightCheckBox.setDisable(false);
									humidityCheckBox.setSelected(true);
									humidityCheckBox.setDisable(false);
									armedTrippedCheckBox.setSelected(true);
									armedTrippedCheckBox.setDisable(false);
									createGraphButton.setId("passSubmit");
									createCSVButton.setId("passSubmit");
								} else {
									selectedDevices.add(deviceLabel.getText());
								}
								if (selectedDevices.size() > 0) {
									createGraphButton.setId("passSubmit");
									createCSVButton.setId("passSubmit");
								}
								// System.out.println("Added: " +
								// deviceLabel.getText());

								FadeTransition ft = new FadeTransition(Duration
										.millis(300), imagePane);
								ft.setFromValue(0.3);
								ft.setToValue(1);
								ft.setCycleCount(1);
								ft.setAutoReverse(false);

								ft.play();

							} else {
								imagePane
										.setStyle("-fx-border-color:grey; -fx-border-width: 3; -fx-border-style: solid;");
								if (deviceLabel.getText().equals("4 in 1 sensor")){
									tempCheckBox.setSelected(false);
									tempCheckBox.setDisable(true);
									lightCheckBox.setSelected(false);
									lightCheckBox.setDisable(true);
									humidityCheckBox.setSelected(false);
									humidityCheckBox.setDisable(true);
									armedTrippedCheckBox.setSelected(false);
									armedTrippedCheckBox.setDisable(true);
								} else {
									selectedDevices.remove(deviceLabel.getText());
								}
								if (selectedDevices.size() == 0 && tempCheckBox.isSelected() == false
										 && lightCheckBox.isSelected() == false  && humidityCheckBox.isSelected() == false
										 && armedTrippedCheckBox.isSelected() == false) {
									createGraphButton.setId("passSubmitGrey");
									createCSVButton.setId("passSubmitGrey");
								}
								if (imagePane.getWidth() == 189){
									imagePane.setPrefSize(188, 120);
								} else {
									imagePane.setPrefSize(288, 120);
								}
								// System.out.println("Removed: " +
								// deviceLabel.getText());

								FadeTransition ft = new FadeTransition(Duration
										.millis(300), imagePane);
								ft.setFromValue(1.0);
								ft.setToValue(0.3);
								ft.setCycleCount(1);
								ft.setAutoReverse(true);

								ft.play();
							}
						}
					});
			
			if (j==2){
				imagePane.setLayoutX(j * 200 + 30); // x layout position spread
			} else {
				imagePane.setLayoutX(j * 200 + 30); // x layout position spread
			}
			
			if (prevFourInOne == true) {
				j += 1.5;
			} else {
				j++;
			}

			imagePane.getChildren().addAll(deviceImage, deviceLabel);
			
			devicesPane.setPrefSize(j * 200 + 30,
					display.getHeight() / 3 * 0.79); // sets the layout of 1 pane on
													// the top and two below, evenly
													// spaces
			System.out.println(devicesPane.getChildren().size());
			devicesPane.getChildren().add(imagePane); // add image panes to the
														// devices pane.
			
//			final Pane leftScrollPane = new Pane();
//			Pane rightScrollPane = new Pane();
//			leftScrollPane.setPrefSize(40,72.5);
//			rightScrollPane.setPrefSize(40,72.5);
//			leftScrollPane.setStyle("-fx-background-color:red");
//			rightScrollPane.setStyle("-fx-background-color:red");
//			rightScrollPane.setLayoutX(devicesScrollPane.getPrefWidth()-rightScrollPane.getPrefWidth()-20);
//			rightScrollPane.setLayoutY((devicesScrollPane.getPrefHeight()/2)-(rightScrollPane.getHeight()/2)-40);
//			leftScrollPane.setLayoutY((devicesScrollPane.getPrefHeight()/2)-(leftScrollPane.getHeight()/2)-40);
//			leftScrollPane.setLayoutX(20);
			
//			leftScrollPane.setOnMousePressed(new EventHandler<MouseEvent>(){
//
//			@Override
//			public void handle(MouseEvent arg0) {
//				if(imagePane.getWidth() > display.getWidth())
//				{
//					imagePane.setLayoutX(imagePane.getLayoutX()-10);
//					System.out.println("ddd");
//				}
//			}});
//
//			rightScrollPane.setOnMousePressed(new EventHandler<MouseEvent>()
//					{
//
//				@Override
//				public void handle(MouseEvent arg0) 
//				{
//					if(devicesPane.getWidth() < devicesScrollPane.getWidth())
//					{
//						imagePane.setLayoutX(imagePane.getLayoutX()+10);
//						System.out.println("fff");	
//					}	
//				}});
//
//			devicesPane.getChildren().addAll(leftScrollPane,rightScrollPane);

			}// end of if
		
		

		// -------------------------------- Setting up the comparePane -----------------------------------------------------
		
		ChangeListener<LocalDate> dateChanger = new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0,
					LocalDate oldDate, LocalDate newDate) {
				SimpleDateFormat format = new SimpleDateFormat("DD MM YYYY");

				String year = newDate.toString().substring(0, 4);
				System.out.println(year);
				String month = newDate.toString().substring(5, 7);
				System.out.println(month);
				String day = newDate.toString().substring(8, 10);
				System.out.println(day);
				String date = day + month + year;
				System.out.println(date);

				System.out.println(" Compare From: "
						+ compareFrom.getValue().toEpochDay());
				System.out.println(format.format(newDate.toEpochDay()));
				System.out.println(" Compare To:  " + compareTo.getValue());
			}
		};

		Label compareLabel = new Label("From:"); // compare from label
		compareLabel.setLayoutX(20);
		compareLabel.setLayoutY(27);
		compareLabel.setId("CompareName");

		HBox compareFromRow = new HBox(5); // hbox for the compare from elements
		compareFromRow.setLayoutX(75);
		compareFromRow.setLayoutY(24);

		compareFrom = new DatePicker(); // allows to pick a date
		compareFrom.setMaxWidth(140);
		compareFrom.setValue(compareTo.getValue().minusDays(1));
		compareFrom.setId("comboSty");
		compareFrom.setEditable(false);
		compareFrom.valueProperty().addListener(dateChanger);

		compareFromHours = getBox("hours"); // allows to pick an hour
		compareFromHours.setId("comboSty");
		compareFromHours.setMaxWidth(2);

		Label colonLabel = new Label(":");
		colonLabel.setId("colonPadding");

		compareFromMinutes = getBox("minutes"); // allows to pick a minute
		compareFromMinutes.setId("comboSty");
		compareFromMinutes.setMaxWidth(2);

		compareFromRow.getChildren().addAll(compareFrom, compareFromHours,
				colonLabel, compareFromMinutes); // adds the date picker and
													// hour and minute pickers

		Label compareToLabel = new Label("To:"); // compare to label
		compareToLabel.setLayoutX(42);
		compareToLabel.setLayoutY(90);
		compareToLabel.setId("CompareName");

		HBox compareToRow = new HBox(5); // hbox for the compare from elements
		compareToRow.setLayoutX(75);
		compareToRow.setLayoutY(87);

		compareTo = new DatePicker(); // allows to pick a date
		compareTo.setMaxWidth(140);
		compareTo.setValue(LocalDate.now());
		compareTo.setId("comboSty");
		compareTo.setEditable(false);
		compareTo.valueProperty().addListener(dateChanger);

		compareToHours = getBox("hours"); // allows to pick an hour
		compareToHours.setId("comboSty");
		compareToHours.setMaxWidth(2);

		Label colonLabel2 = new Label(":");
		colonLabel2.setId("colonPadding");

		compareToMinutes = getBox("minutes"); // allows to pick an minute
		compareToMinutes.setId("comboSty");
		compareToMinutes.setMaxWidth(2);

		compareToRow.getChildren().addAll(compareTo, compareToHours,
				colonLabel2, compareToMinutes); // adds the date picker and hour
												// and minute pickers

		filterPane.getChildren().addAll(compareLabel, compareFromRow,
				compareToLabel, compareToRow); // adds labels and rows to the
												// compare pane

		// factory to create a cell for every day within the date picker
		// checks to see if the cell is after todays date
		// sets the cell to disabled and background color to red if date is
		// after current date.
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (item.isAfter(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		};
		// adds the factory to both of the compare buttons.
		compareFrom.setDayCellFactory(dayCellFactory);
		compareTo.setDayCellFactory(dayCellFactory);

		// -------------------------------- Setting up the submitPane
		// -----------------------------------------------------

		graphType = new ChoiceBox<String>(); // creates a combo box to select
												// the type of graph
		graphType.setId("comboSty");
		graphType.getItems().addAll("Line Chart", "Bar Chart");
		graphType.setTooltip(new Tooltip("Select Type Of Graph"));
		graphType.getSelectionModel().selectFirst();
		graphType.setLayoutX(368);
		graphType.setLayoutY(24);
		graphType.setMinWidth(160);
		graphType.setMaxWidth(160);
		// creates a combo box to select the type of graph
		seperateGraphs = new ChoiceBox<String>();
		seperateGraphs.setId("comboSty");
		seperateGraphs.getItems().addAll("One Chart", "Multiple Charts");
		seperateGraphs.setTooltip(new Tooltip(
				"Display readings on one graph or many"));
		seperateGraphs.getSelectionModel().selectFirst();
		seperateGraphs.setLayoutX(368);
		seperateGraphs.setLayoutY(87);
		seperateGraphs.setMinWidth(160);
		seperateGraphs.setMaxWidth(160);

		createGraphButton.setId("passSubmitGrey");
		createGraphButton.setLayoutX(560);
		createGraphButton.setLayoutY(24);
		createGraphButton.setMinWidth(200);
		createGraphButton.setMaxWidth(200);
		//when button is pressed call the  showDeviceDetails method
		createGraphButton.setOnAction(new EventHandler<ActionEvent>() { 
					@Override
					public void handle(ActionEvent arg0) {
						try {
							ArrayList<Device> devicesToDisplay = new ArrayList<Device>();

							if (tempCheckBox.isSelected() ==  true){
								selectedDevices.add( "4 in 1 sensor: temperature");
							}
							if (lightCheckBox.isSelected() ==  true){
								selectedDevices.add( "4 in 1 sensor: light");
							}
							if (humidityCheckBox.isSelected() ==  true){
								selectedDevices.add( "4 in 1 sensor: humidity");
							}
							if (armedTrippedCheckBox.isSelected() ==  true){
								selectedDevices.add( "4 in 1 sensor: armedtripped");
							}
							if (selectedDevices.size() > 0) {
								for (String selectedDevice : selectedDevices) {
									for (Device device : devicesList) {
										if (selectedDevice.contains(device.getName())) {
											if (device instanceof FourInOne){
												FourInOne fourInOne = new FourInOne(device.getName(), device.getId(),
														device.getAltid(), device.getCategory(), device.getSubcategory(),
														device.getRoom(), device.getParent(), ((FourInOne) device).getTemperature(),
														((FourInOne) device).getLight(), ((FourInOne) device).getHumidity(), 
														((FourInOne) device).getArmedtripped(), device.getBatterylevel());
												fourInOne.setReadingName(selectedDevice.substring(15));
												System.out.println(selectedDevice);
												devicesToDisplay.add(fourInOne);
											} else {
												devicesToDisplay.add(device); 
											}						
											
										}
									}
								}
								if (tempCheckBox.isSelected() ==  true){
									selectedDevices.remove( "4 in 1 sensor: temperature");
								}
								if (lightCheckBox.isSelected() ==  true){
									selectedDevices.remove( "4 in 1 sensor: light");
								}
								if (humidityCheckBox.isSelected() ==  true){
									selectedDevices.remove( "4 in 1 sensor: humidity");
								}
								if (armedTrippedCheckBox.isSelected() ==  true){
									selectedDevices.remove( "4 in 1 sensor: armedtripped");
								}
								System.out.println("--------------------------");
								for (Device device : devicesToDisplay){
									System.out.println(device.getReadingName());
								}
								show24hrGraph(devicesToDisplay, "not24", graphsPane); // <--
							
							}
						} catch (SQLException e) { 
							// the dates selected in the drop down boxes.
							e.printStackTrace();
						}
					} 
		});

		createCSVButton.setId("passSubmitGrey");
		createCSVButton.setLayoutX(560);
		createCSVButton.setLayoutY(87);
		createCSVButton.setMinWidth(200);
		createCSVButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
				public void handle(ActionEvent arg0) {
					try {
						ArrayList<Device> devicesToDisplay = new ArrayList<Device>();

						if (tempCheckBox.isSelected() ==  true){
							selectedDevices.add( "4 in 1 sensor: temperature");
						}
						if (lightCheckBox.isSelected() ==  true){
							selectedDevices.add( "4 in 1 sensor: light");
						}
						if (humidityCheckBox.isSelected() ==  true){
							selectedDevices.add( "4 in 1 sensor: humidity");
						}
						if (armedTrippedCheckBox.isSelected() ==  true){
							selectedDevices.add( "4 in 1 sensor: armedtripped");
						}
						if (selectedDevices.size() > 0) {
							for (String selectedDevice : selectedDevices) {
								for (Device device : devicesList) {
									if (selectedDevice.contains(device.getName())) {
										if (device instanceof FourInOne){
											FourInOne fourInOne = new FourInOne(device.getName(), device.getId(),
													device.getAltid(), device.getCategory(), device.getSubcategory(),
													device.getRoom(), device.getParent(), ((FourInOne) device).getTemperature(),
													((FourInOne) device).getLight(), ((FourInOne) device).getHumidity(), 
													((FourInOne) device).getArmedtripped(), device.getBatterylevel());
											fourInOne.setReadingName(selectedDevice.substring(15));
											System.out.println(selectedDevice);
											devicesToDisplay.add(fourInOne);
										} else {
											devicesToDisplay.add(device);
										}					
										
									}
								}
							}
							if (tempCheckBox.isSelected() ==  true){
								selectedDevices.remove( "4 in 1 sensor: temperature");
							}
							if (lightCheckBox.isSelected() ==  true){
								selectedDevices.remove( "4 in 1 sensor: light");
							}
							if (humidityCheckBox.isSelected() ==  true){
								selectedDevices.remove( "4 in 1 sensor: humidity");
							}
							if (armedTrippedCheckBox.isSelected() ==  true){
								selectedDevices.remove( "4 in 1 sensor: armedtripped");
							}
							System.out.println("--------------------------");
							for (Device device : devicesToDisplay){
								System.out.println(device.getReadingName());
							}
							saveToCSV(devicesToDisplay, "not 24"); // <--
							
						}
					} catch (Exception e) { // the dates selected in the drop down boxes.
						e.printStackTrace();
					}
				} 
		});

		filterPane.getChildren().addAll(graphType, seperateGraphs,
				createGraphButton, createCSVButton); 
		// add graph selecter and button to thesubmitPane

		displayNoGraph(graphsPane);

		// -------------------------------- Setting up the
		// graphSettingsContainer
		// -----------------------------------------------------

		devicesScrollPane.setContent(devicesPane);
		devicesScrollPane.setLayoutX(-1);
		devicesScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		devicesScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		devicesScrollPane.setStyle("-fx-background: rgb(255,255,255); -fx-border-color: white;");

		graphSettingsContainer.getChildren().addAll(devicesScrollPane, filterPane, graphsPane);

		graphScrollPane.setContent(graphSettingsContainer);
		graphScrollPane.setLayoutX(0);
		graphScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		graphScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		graphScrollPane.setStyle("-fx-background: rgb(255,255,255); -fx-border-color: white;");

		display.getChildren().add(graphScrollPane);

		}
	}

	public void displaySettings() {
		display.getChildren().clear();
		
		if(topDisplay.getLayoutY()!=0){
			topDisplay.setLayoutY(0);
			display.setLayoutY(topDisplay.getPrefHeight());
			display.setPrefHeight(scene.getHeight() - topDisplay.getPrefHeight() + 10);
		}	
		
		Pane pane = new Pane();
		pane.setId("backPaneBackground");
		pane.setTranslateX(10);
		pane.setTranslateY(10);
		
		Line line = new Line(20, 100, 760, 100);
		line.setId("settingsLine2");
		
		final Button addRoom = new Button("Add Room");
		addRoom.setId("passSubmit");
		addRoom.setLayoutX(660);
		addRoom.setLayoutY(35);
		addRoom.setMaxWidth(100);
		addRoom.setMinWidth(100);
		
		final TextField newRoomName = new TextField();
		newRoomName.setPromptText("Room Name");
		newRoomName.setId("passFields");
		newRoomName.setLayoutX(490);
		newRoomName.setLayoutY(35);
		newRoomName.setMaxWidth(150);
		newRoomName.setMinWidth(150);
		newRoomName.setVisible(false);
		
		
		VBox list = new VBox();
		list.setLayoutX(20);
		list.setLayoutY(105);


		
		
		addRoom.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				if(addRoom.getText() == "Add Room") {
					newRoomName.setVisible(true);
					addRoom.setText("Save");
				}else if(addRoom.getText() == "Save") {
					
					roomsList.add(new Room(newRoomName.getText(), roomsList.size()+1));
					executeHttp("http://" + ip + ":3480/data_request?id=room&action=create&name=" + newRoomName.getText());
					displaySettings();
				}
				
		 	}
		});
		
		for(Room room : roomsList){
			list.getChildren().add(room.getDetailsPane(getIP()));
		}
		
		pane.getChildren().addAll(line, addRoom, newRoomName, list);		
		display.getChildren().add(pane);
	}
	

	private void displayScenes() {
		display.getChildren().clear();
		
		scenesSelectedDevices = new ArrayList<Device>();
		
		
		FlowPane scenesPane = new FlowPane();
		
		Pane topScenesPane = new Pane();
		topScenesPane.setPrefSize(display.getWidth(), display.getHeight() / 2);
		
		Pane bottomScenesPane = new Pane();
		bottomScenesPane.setPrefSize(display.getWidth(), display.getHeight() / 2);


		for (int i = 0; i < devicesList.size(); i++) // for each device 
		{
			final ImageView deviceImage = new ImageView(new Image(VeraGUI.class
					.getResource("/resources/" + devicesList.get(i).getImage())
					.toExternalForm())); 
			// add the image
			// image sizing
			deviceImage.setFitHeight(100);
			deviceImage.setFitWidth(100);
			// image layout
			deviceImage.setLayoutX(25); 
			deviceImage.setLayoutY(20);

			final Label deviceLabel = new Label(devicesList.get(i).getName());

			deviceLabel.setPrefWidth(100); 
			// label sizing
			deviceLabel.setLayoutX(20); 
			// label layout
			deviceLabel.setLayoutY(120);

			final Pane imagePane = new Pane(); 
			// pane to contain the image and the label
			imagePane.setStyle("-fx-border-color:grey; -fx-border-width: 3; -fx-border-style: solid;");
			imagePane.setPrefSize(144, 144);
			imagePane.setLayoutX(20 + i * 150);
			imagePane.setLayoutY(20);
			
			FadeTransition ft = new FadeTransition(Duration.millis(300),
					imagePane);
			ft.setFromValue(0.3);
			ft.setToValue(0.3);
			ft.setCycleCount(1);
			ft.setAutoReverse(false);

			ft.play();

			
			imagePane.setOnMouseClicked(new EventHandler<Event>() { 
				@Override
				public void handle(Event event) {
					if (imagePane.getWidth() == 144) {
						
						for (Device device : devicesList){
							if (device.getName().equals(deviceLabel.getText())){
								scenesSelectedDevices.add(device);
							}
						}
						
						imagePane.setStyle("-fx-border-color:green; -fx-border-width: 3; -fx-border-style: solid;");
						imagePane.setPrefSize(145, 145);

						FadeTransition ft = new FadeTransition(Duration
								.millis(300), imagePane);
						ft.setFromValue(0.3);
						ft.setToValue(1);
						ft.setCycleCount(1);
						ft.setAutoReverse(false);

						ft.play();
					
						for (Device device : scenesSelectedDevices){
								System.out.println(device.getName());
						}
					} else {
						for (Device device : devicesList){
							if (device.getName().equals(deviceLabel.getText())){
								scenesSelectedDevices.remove(device);
							}
						}
						
						imagePane.setStyle("-fx-border-color:grey; -fx-border-width: 3; -fx-border-style: solid;");
						imagePane.setPrefSize(144, 145);

						FadeTransition ft = new FadeTransition(Duration
								.millis(300), imagePane);
						ft.setFromValue(1.0);
						ft.setToValue(0.3);
						ft.setCycleCount(1);
						ft.setAutoReverse(true);

						ft.play();
					}
				}
			});

			imagePane.getChildren().addAll(deviceImage, deviceLabel);

			topScenesPane.getChildren().add(imagePane);
		}

		scenesPane.getChildren().addAll(topScenesPane, bottomScenesPane);

		display.getChildren().add(scenesPane);
	}

	private void changeButtons(String name) {
		sideButtons.getChildren().clear();
		java.util.List<String> names = new ArrayList<String>();
		switch (name) {
		case "mainMenu":
			String[] words = { "Dashboard", "Graphs", "Settings", "Advanced",
			"Quit" };
			names = Arrays.<String> asList(words);
			break;
		case "details":
			String[] words2 = { "Dashboard", "Graphs", "Settings", "Advanced",
			"Quit" };
			names = Arrays.<String> asList(words2);
			break;
		case "graphs":
			String[] words3 = { "Dashboard", "Graphs", "Settings", "Advanced",
			"Quit" };
			names = Arrays.<String> asList(words3);
			break;
		case "settings":
			String[] words4 = { "Dashboard", "Graphs", "Settings", "Advanced", 
			"Quit" };
			names = Arrays.<String> asList(words4);
			break;
		case "advanced":
			String[] words5 = { "Dashboard", "Graphs", "Settings", "Advanced",
			"Quit" };
			names = Arrays.<String> asList(words5);
			break;
		}
		int x = 0;
		for (Button button : buttons) {
			try {
				button.setText(names.get(x));
				button.setId("sideButton");
				button.setOnAction(buttonHandler);
				if (name.equals("compare") && x == 0) {
					sideButtons.getChildren().add(0, button);
				} else {
					sideButtons.getChildren().add(button);
				}
			} catch (Exception e) {

			}
			x++;
		}
	}

	private void show24hrGraph(ArrayList<Device> devicesToDisplay,
		String mode, Pane parent) throws SQLException {
		
		devicesToCSV.clear();
		devicesToCSV.addAll(devicesToDisplay);

		// mode parameter determines if the method has been called from within a
		// device or the graphs pane.
			if (mode.equals("24")) { // if mode = 24 hours set compareFromDate and
				// compareToDate to the past 24 hours
				Calendar currentDate = Calendar.getInstance();

				String trimmedCurrentDate = Long.toString(currentDate
						.getTimeInMillis() / 1000);
				trimmedCurrentDate = trimmedCurrentDate.substring(0, 10);

				compareFromDate = Long.parseLong(trimmedCurrentDate) - 86400;
				compareToDate = Long.parseLong(trimmedCurrentDate);
			} else { // else use the times selected using the dropdown boxes.
				compareFromDate = (compareFrom.getValue().toEpochDay() * 86400)
						+ (Long.parseLong(compareFromHours.getValue()) * 3600)
						+ (Long.parseLong(compareFromMinutes.getValue()) * 60);
				compareToDate = (compareTo.getValue().toEpochDay() * 86400)
						+ (Long.parseLong(compareToHours.getValue()) * 3600)
						+ (Long.parseLong(compareToMinutes.getValue()) * 60) + 60;
			}

			try {
				// ArrayList of ArrayList of reading
				ArrayList<ArrayList> readings = new ArrayList<ArrayList>();
				// ArrayList of ArrayList of dates
				ArrayList<ArrayList> dates = new ArrayList<ArrayList>(); // Array

				/*
				 * This is required as we need to send an array list of readings for
				 * each devices and all of these array lists need to be held within
				 * an array list to be pasted over to the charts method.
				 */
				ResultSet results;
				for (int i = 0; i < devicesToDisplay.size(); i++) { // For each device
					if (devicesToDisplay.get(i) instanceof FourInOne){
						System.out.println(((FourInOne) devicesToDisplay.get(i)).readingFromSQL(devicesToDisplay.get(i).getReadingName(),
								compareFromDate, compareToDate));
						results = conn.getRows(((FourInOne) devicesToDisplay.get(i)).readingFromSQL(devicesToDisplay.get(i).getReadingName(),
								compareFromDate, compareToDate)); 
					} else {
						results = conn.getRows(devicesToDisplay.get(i).readingFromSQL(
								compareFromDate, compareToDate)); 
					}

					// get a result set
					// from the database
					// containing dates
					// and readings

					readingsArray = new ArrayList<Integer>(); // array list for the
					// devices readings
					dateArray = new ArrayList<String>(); // array list for the
					// devices readings
					// dates
					int k = 0;
					while (results.next()) {
						String deviceReading = results.getString(devicesToDisplay.get(i)
								.getReadingName());
						long readingDate = results.getInt("reading_date"); 
						if (!(deviceReading == null)) { // if the reading is not
							// null
							int convertedDeviceReading = Integer
									.parseInt(deviceReading); // convert the reading
							// into an int
							String date = new java.text.SimpleDateFormat(
									"MM/dd/yyyy HH:mm").format(new java.util.Date(
											readingDate * 1000)); 
							System.out.println(date.substring(11, 16));
							
							if (date.substring(11, 16).equals("00:00")){
								date = new java.text.SimpleDateFormat(
										"HH:mm - MM/dd").format(new java.util.Date(
												readingDate * 1000));
							} else {
								date = new java.text.SimpleDateFormat(
										"HH:mm - MM/dd").format(new java.util.Date(
												readingDate * 1000));
							}

							readingsArray.add(convertedDeviceReading); 
							dateArray.add(date); // add date to the array
							k++;
						}
					}
					
					if (readingsArray.size() > 1)
						readings.add(readingsArray); 
					if (readingsArray.size() > 1)
						dates.add(dateArray); 
				}
				System.out.println("+++++++++++++++++++++");
				for (Device device : devicesToDisplay){
					System.out.println(device.getReadingName());
				}
				System.out.println(devicesToDisplay.size());
				System.out.println(readings.size());
				System.out.println(dates.size());
				System.out.println("+++++++++++++++++++++");


				String splitGraphs;
				String chartType;
				int sizeChart = 0;

				if (mode.equals("not24")) { // If method called through graph page
					splitGraphs = seperateGraphs.getValue(); // Check type of graph
					chartType = graphType.getSelectionModel().getSelectedItem();				// to display
				} else { // If method called through device page
					splitGraphs = "One Chart"; // auto set to one chart
					chartType = "Line Chart";
					sizeChart =2;
				}

				if (splitGraphs.equals("One Chart")) { 
					if(sizeChart==2)
					{
						Charts chart = new Charts(readings, dates, devicesToDisplay,
								chartType, 1, 0, 2); 
						parent.getChildren().clear();
						chart.show(parent);
					}
					else
					{
						Charts chart = new Charts(readings, dates, devicesToDisplay,
								chartType, 1, 0, 0); 
						// send the arrays to the chart
						parent.getChildren().clear();
						chart.show(parent);
						// object
					}
				} else { // else split each of the readings into seperate arrayLists
					// (to make compatable with the chart) and create a
					// chart for each reading
					ArrayList<Charts> charts = new ArrayList<Charts>();
					for (int i = 0; i < devicesToDisplay.size(); i++) {
						ArrayList<ArrayList> singleReadings = new ArrayList<ArrayList>();
						ArrayList<ArrayList> singleDates = new ArrayList<ArrayList>();
						ArrayList<Device> singleDevicesToDisplay = new ArrayList<Device>();

						singleReadings.add(readings.get(i));
						singleDates.add(dates.get(i));
						singleDevicesToDisplay.add(devicesToDisplay.get(i));
						charts.add(new Charts(singleReadings, singleDates,
								singleDevicesToDisplay, chartType,
								devicesToDisplay.size(), i, 1));
						charts.get(i).show(parent);
					}
				}
			} catch (Exception e1) {
				// Label warning = new Label("Sorry No Graph Data Available");
				// warning.setPrefSize(600, 300);
				// warning.setId("graphWarning");
				// warning.setLayoutX(50);
				// warning.setLayoutY(150);
				// display.getChildren().add(warning);
				System.out.println("here");
				parent.getChildren().clear();
				displayNoGraph(parent);
			}
		
		// this adds a change listener to the drop down box and creates a new
		// graph when you select one.
	}
	
	
	private synchronized void displayLoading(){
		
			loadingPane.setStyle("-fx-background-color:white");
			loadingPane.setPrefSize(display.getPrefWidth(),display.getPrefHeight());
			loadingPane.setLayoutX(0);
			loadingPane.setLayoutY(0);

			ImageView loadingImage = new ImageView(new Image(VeraGUI.class.getResource(
					"/resources/loadingGif.gif").toExternalForm()));
			loadingImage.setFitHeight(50);
			loadingImage.setFitWidth(50);
			loadingImage.setLayoutX(5);
			loadingImage.setLayoutY(5);

			Label loadingLabel = new Label("Loading...");
			loadingLabel.setLayoutX(80);
			loadingLabel.setLayoutY(23);

			loadingPane.getChildren().addAll(loadingImage, loadingLabel);

			root.getChildren().add(loadingPane);
			
	

	}

	private ChoiceBox<String> getBox(String type) {
		ChoiceBox<String> choicebox = new ChoiceBox<String>();
		choicebox.setId("timeDropDown");
		switch (type) {
		case "hours":
			for (int x = 0; x < 24; x++) {
				if (x < 10) {
					choicebox.getItems().add("0" + x);
				} else {
					choicebox.getItems().add("" + x);
				}
			}
			choicebox.getSelectionModel().selectFirst();
			break;
		case "minutes":
			for (int x = 0; x < 56; x += 5) {
				if (x < 10) {
					choicebox.getItems().add("0" + x);
				} else {
					choicebox.getItems().add("" + x);
				}
			}
			choicebox.getSelectionModel().selectFirst();
			break;
		}
		return choicebox;
	}

	private void saveToCSV(ArrayList<Device> devices, String mode) {
		// TODO Auto-generated method stub

		if (mode.equals("24")) { // if mode = 24 hours set compareFromDate and
			// compareToDate to the past 24 hours
			Calendar currentDate = Calendar.getInstance();

			String trimmedCurrentDate = Long.toString(currentDate
					.getTimeInMillis() / 1000);
			trimmedCurrentDate = trimmedCurrentDate.substring(0, 10);

			compareFromDate = Long.parseLong(trimmedCurrentDate) - 86400;
			compareToDate = Long.parseLong(trimmedCurrentDate);
		} else { // else use the times selected using the dropdown boxes.
			compareFromDate = (compareFrom.getValue().toEpochDay() * 86400)
					+ (Long.parseLong(compareFromHours.getValue()) * 3600)
					+ (Long.parseLong(compareFromMinutes.getValue()) * 60);
			compareToDate = (compareTo.getValue().toEpochDay() * 86400)
					+ (Long.parseLong(compareToHours.getValue()) * 3600)
					+ (Long.parseLong(compareToMinutes.getValue()) * 60) + 60;
		}
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Enter File or Choose File to Overwrite");
		fileChooser.setInitialFileName("veraData_" + compareFromDate + "_to_"
				+ compareToDate + ".csv");
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		// Show save file dialog
		File file = fileChooser.showSaveDialog(stage);

		CSV csv = new CSV();
		try {
			csv.toCSV(file, devices, compareFromDate, compareToDate);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} // Specific id and date.
	}

	public void displayNoInternet() {
		display.getChildren().clear();
		Pane pane = new Pane();
		pane.setId("backPaneBackground");
		pane.setTranslateX(10);
		pane.setTranslateY(10);

		Label warning = new Label("Sorry, No Internet Connection");
		warning.setId("noInternetWarning");
		warning.setLayoutX(230);
		warning.setLayoutY(250);
		
		Button refreshBtn = new Button("Refresh");
		refreshBtn.setId("botPaneBtn");
		refreshBtn.setLayoutX(340);
		refreshBtn.setLayoutY(170);
		
		refreshBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if(InternetConnectionCheck()) {
					displayDevices();
				} else {
					// do nothing yet
				}
			}
		});

		Label warningText = new Label(
				"Internet connection needed to retrieve data from Vera box and the database");
		warningText.setId("noInternetText");
		warningText.setLayoutX(100);
		warningText.setLayoutY(290);

		display.getChildren().addAll(pane, refreshBtn, warning, warningText);
	}

	public void displayNoGraph(Pane givenPane) {
		givenPane.getChildren().clear();
		Pane pane = new Pane();
		pane.setId("backPaneBackground");
		pane.setTranslateX(10);
		pane.setTranslateY(10);

		Label warning = new Label("Sorry, No Graph To Display");
		warning.setId("noInternetWarning");
		warning.setLayoutX(230);
		warning.setLayoutY(250);

		Label warningText = new Label(
				"Try changing date and times in order for a graph to be displayed");
		warningText.setId("noInternetText");
		;
		warningText.setLayoutX(120);
		warningText.setLayoutY(290);

		givenPane.getChildren().addAll(pane, warning, warningText);
	}

	private boolean InternetConnectionCheck() {

		boolean check = false;

		try {
			try {
				URL url = new URL("http://www.csesalford.com");
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

	public ArrayList<Room> getRoomsList() {
		return roomsList;
	}

	public void setRoomsList(ArrayList<Room> roomsList) {
		this.roomsList = roomsList;
	}

	public ArrayList<Device> getDevicesList() {
		return devicesList;
	}

	public void setDevicesList(ArrayList<Device> devicesList) {
		this.devicesList = devicesList;
	}
	
		
	
	public void showLogin() {
		
		topDisplay.setPrefWidth(1000);
	
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Your password");
		
		final TextField userTxt = new TextField();
		userTxt.setPromptText("Username");
		userTxt.setId("passFields");
		userTxt.setLayoutX(50);
		userTxt.setLayoutY(45);
		userTxt.setMaxWidth(130);
		userTxt.setMinWidth(130);
		
		final PasswordField passF = new PasswordField();
		passF.setPromptText("Password");
		passF.setId("passFields");
		passF.setLayoutX(200);
		passF.setLayoutY(45);
		passF.setMaxWidth(130);
		passF.setMinWidth(130);
		
		final Label passRes = new Label();
		passRes.setId("passFail");
		passRes.setLayoutX(50);
		passRes.setLayoutY(20);
		
		Button submit = new Button("Login");
		submit.setId("passSubmit");
		submit.setLayoutX(350);
		submit.setLayoutY(45);
		submit.setMaxWidth(100);
		submit.setMinWidth(100);
		
		topDisplay.getChildren().addAll(userTxt, passF, submit, passRes);
		
		submit.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				ResultSet resultSet = null;
				
				try {
					
					if(userTxt.getText().isEmpty() || passF.getText().isEmpty()) {
						passRes.setText("Please fill in both fields");
					} else {
						
						resultSet = conn.getRows("SELECT * FROM Users WHERE user_name = " + "'" + userTxt.getText() + "'");
						
						if(!resultSet.next()) {
							passRes.setText("Incorrect Username!");
						} else if (!passF.getText().equals(resultSet.getString("password"))) {
							passRes.setText("Incorrect Password!");
						} else {
							String capitalizedName = resultSet.getString("user_name").substring(0, 1).toUpperCase() + resultSet.getString("user_name").substring(1);
							welcome.setText("Welcome " + capitalizedName);
							topDisplay.getChildren().clear();
							topDisplay.getChildren().addAll(time, welcome);
							loggedIn = true;
							userName = resultSet.getString("user_name");
							ip = resultSet.getString("ip_address");
							displayDevices();
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        passF.clear();
				
			}
		});
	}
	
	private void showWelcomeSplash() {
		
		ImageView image = new ImageView(new Image(VeraGUI.class.getResource(
				"/resources/splash.png").toExternalForm()));
		image.setPreserveRatio(false);
		image.setFitWidth(display.getWidth());
		image.setFitHeight(display.getHeight());
		display.getChildren().addAll(image);
	}	
	
	
	public void showDeviceSettings(final Device device, final Room room) {
		display.getChildren().clear();
		Pane pane = new Pane();
		pane.setId("backPaneBackground");
		pane.setTranslateX(10);
		pane.setTranslateY(10);
		
		
		Label devLabel = new Label("Edit Name");
		devLabel.setLayoutX(350);
		devLabel.setLayoutY(45);
		devLabel.setId("genLabel");
		
		final TextField devName = new TextField();
		devName.setId("passFields");
		devName.setText(device.getName());
		devName.setLayoutX(300);
		devName.setLayoutY(100);
		devName.setMaxWidth(200);
		devName.setMinWidth(200);
		
		Button submitName = new Button("Change Name");
		submitName.setId("passSubmit");
		submitName.setLayoutX(322);
		submitName.setLayoutY(160);
		submitName.setMaxWidth(150);
		submitName.setMinWidth(150);
		
		
		Line line = new Line(100, 220, 700, 220);
		line.setId("settingsLine");
		
		String[] roomNames = new String[roomsList.size()];
		int count = 0;
		for (Room roomX : roomsList) {
			roomNames[count] = roomX.getName();
			count++;
		}
		
		ObservableList<String> options = FXCollections.observableArrayList();
		final ComboBox<String> comboBox = new ComboBox<String>(options);
		comboBox.getItems().addAll(roomNames);
		comboBox.setValue(room.getName());
		comboBox.setLayoutX(300);
		comboBox.setLayoutY(300);
		comboBox.setMaxWidth(200);
		comboBox.setMinWidth(200);
		comboBox.setId("comboSty");
		
		Label roomLabel = new Label("Change Room");
		roomLabel.setLayoutX(340);
		roomLabel.setLayoutY(250);
		roomLabel.setId("genLabel");
		
		Button submitRoom = new Button("Change Room");
		submitRoom.setId("passSubmit");
		submitRoom.setLayoutX(322);
		submitRoom.setLayoutY(360);
		submitRoom.setMaxWidth(150);
		submitRoom.setMinWidth(150);
		
		submitName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				String url = "http://" + ip + ":3480/data_request?id=device&action=rename&device=" + 
				device.getId() + "&name=" + devName.getText() + "&room=" + room.getId();
				if (executeHttp(url)) {
					System.out.println("Command Sent");
					device.setName(devName.getText());
				} else {
					System.out.println("Device out of Reach");
				}
					
			}
		});
		
		submitRoom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				String url = "http://" + ip + ":3480/data_request?id=device&action=add&device=" + 
				device.getId() + "&name=" + devName.getText() + "&room=" + room.getId();
				if (executeHttp(url)) {
					
					System.out.println("Command Sent");
					for(Room room: roomsList) {
						if (room.getName().equalsIgnoreCase(comboBox.getValue())) {
							room.addDeviceToRoom(device);
						}
					}
					room.removeDeviceFromRoom(device);
				} else {
					System.out.println("Device out of Reach");
				}
			}
		});
		

		display.getChildren().addAll(pane, devName, devLabel, submitName, line, comboBox, roomLabel, submitRoom);
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

	public Device getSelectedDevice() {
		return selectedDevice;
	}

	public void setSelectedDevice(Device selectedDevice) {
		this.selectedDevice = selectedDevice;
	}
	
	private void displayAdvancedSettings() {
		// TODO Auto-generated method stub
		
		if(topDisplay.getLayoutY()!=0){
			topDisplay.setLayoutY(0);
			display.setLayoutY(topDisplay.getPrefHeight());
			display.setPrefHeight(scene.getHeight() - topDisplay.getPrefHeight() + 10);
		}	
		
		ResultSet resultSet = null;
		Label ipLab2 = null;
		
		try {
			resultSet = conn.getRows("SELECT * FROM Users WHERE user_name = " + "'" + userName + "'");
			if(resultSet.next()) {
				ipLab2 = new Label(resultSet.getString("ip_address"));
				ipLab2.setLayoutX(140);
				ipLab2.setLayoutY(20);
				ipLab2.setId("genText");
				ip = resultSet.getString("ip_address");
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		display.getChildren().clear();
		Pane pane = new Pane();
		pane.setId("backPaneBackground");
		pane.setTranslateX(10);
		pane.setTranslateY(10);
		
		Line line = new Line(20, 120, 760, 120);
		line.setId("settingsLine2");
		
		Label ipLab = new Label("Current IP: ");
		ipLab.setLayoutX(20);
		ipLab.setLayoutY(20);
		ipLab.setId("genText");
		
		final Button changeIpBtn = new Button("Change IP");
		changeIpBtn.setId("passSubmit");
		changeIpBtn.setLayoutX(20);
		changeIpBtn.setLayoutY(70);
		changeIpBtn.setMaxWidth(150);
		changeIpBtn.setMinWidth(150);
		
		final TextField newIpTxt = new TextField();
		newIpTxt.setText(ip);
		newIpTxt.setId("passFields");
		newIpTxt.setLayoutX(200);
		newIpTxt.setLayoutY(70);
		newIpTxt.setMaxWidth(200);
		newIpTxt.setMinWidth(200);
		newIpTxt.setVisible(false);
		
		Label changePassLab = new Label("Change Password");
		changePassLab.setLayoutX(20);
		changePassLab.setLayoutY(140);
		changePassLab.setId("genText");
		
		final Button changePassBtn = new Button("Change Password");
		changePassBtn.setId("passSubmit");
		changePassBtn.setLayoutX(20);
		changePassBtn.setLayoutY(190);
		changePassBtn.setMaxWidth(150);
		changePassBtn.setMinWidth(150);
		
		final PasswordField passF = new PasswordField();
		passF.setPromptText("New Password");
		passF.setId("passFields");
		passF.setLayoutX(200);
		passF.setLayoutY(190);
		passF.setMaxWidth(150);
		passF.setMinWidth(150);
		passF.setVisible(false);
		
		changeIpBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				if(changeIpBtn.getText() == "Change IP") {
					newIpTxt.setVisible(true);
					changeIpBtn.setText("Save IP");
				}else if(changeIpBtn.getText() == "Save IP") {
						conn.insertRow("UPDATE Users SET ip_address = " + "'" + ip + "'" + " WHERE user_name = " + "'" + userName + "'");
						displayAdvancedSettings();
					} 
				}
		});
		
		
		changePassBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				if(changePassBtn.getText() == "Change Password") {
					passF.setVisible(true);
					changePassBtn.setText("Save Password");
				}else if(changePassBtn.getText() == "Save Password") {
						conn.insertRow("UPDATE Users SET password = " + "'" + passF.getText() + "'" + " WHERE user_name = " + "'" + userName + "'");
						displayAdvancedSettings();
					} 
				}
		});
		
		pane.getChildren().addAll(ipLab, ipLab2, changeIpBtn, newIpTxt, line, changePassLab, changePassBtn, passF);
		display.getChildren().add(pane);	
	}
	
	private String getIP(){
		return ip;
	}
	
	
//	private boolean getConfirmAlert(String message) {
//		
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle(message);
//		Optional<ButtonType> result = alert.showAndWait();
//		
//		if (result.get() == ButtonType.OK){
//			return true;
//		} else {
//			return false;
//		}
//	}
}
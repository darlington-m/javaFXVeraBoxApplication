package GUI;

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
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import DataRetrival.CurrentReadings;
import DataRetrival.MySQLConnect;
import Devices.Device;
import Devices.Room;
import Exports.CSV;
import Graphs.Charts;

public class VeraGUI extends Application {

	MySQLConnect conn = new MySQLConnect();

	private Scene scene;
	private Stage stage;
	private Pane root, display;
	final private Pane sortingPane = new Pane();
	private long compareToDate, compareFromDate;
	private ChoiceBox<String> compareToHours, compareToMinutes,
			compareFromHours, compareFromMinutes, secondCompareFromHours,
			secondCompareFromMinutes, secondCompareFromHours2,
			secondCompareFromMinutes2;
	private DatePicker compareTo, compareFrom, secondCompareTo,
			secondCompareFrom;
	final private VBox sideButtons = new VBox(0), vb = new VBox(30);
	private RadioButton compareone;
	private ChoiceBox<String> graphType, seperateGraphs;
	private ArrayList<Integer> readingsArray = new ArrayList<Integer>();
	private ArrayList<String> dateArray = new ArrayList<String>();
	private Device selectedDevice;

	ArrayList<Button> buttons = new ArrayList<Button>();

	EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {

			if (InternetConnectionCheck()) {

				switch (((Button) arg0.getSource()).getText()) {
				case "Dashboard":
					displayDevices("All");
					break;
				case "Settings":
					changeButtons("settings");
					displaySettings();
					break;
				case "Graphs":
					displayGraphs();
					break;
				case "Scenes":
					displayScenes();
					break;
				case "Back":
					display.getChildren().clear();
					changeButtons("mainMenu");
					displayDevices("All");
					break;
				case "Cancel":
					displaySettings();
					break;
				case "Add a room":
					changeButtons("addRoom");
					break;
				case "Download CSV":
					saveToCSV(selectedDevice);
					break;
				case "Quit":
					System.exit(0);
					break;
				}
			} else {

				if (((Button) arg0.getSource()).getText() == "Quit") {
					System.exit(0);
				} else {
					displayNoInternet();
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

		scene.getStylesheets().add(
				VeraGUI.class.getResource("/Resources/css.css")
						.toExternalForm());

		Pane sideDisplay = new Pane();
		sideDisplay.setId("sidePane");
		sideDisplay.setPrefSize(200, scene.getHeight()); // the width of the
															// side bar....

		ImageView image = new ImageView(new Image(VeraGUI.class.getResource(
				"/Resources/oem_logo.png").toExternalForm()));
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

		Pane topDisplay = new Pane();
		topDisplay.setLayoutX(sideDisplay.getPrefWidth());
		topDisplay.setPrefSize(
				(scene.getWidth() - sideDisplay.getPrefWidth() + 10), 100);
		topDisplay.setId("topDisplay");

		Label welcome = new Label("Welcome");
		welcome.setId("WelcomeMessage");
		welcome.setLayoutX(20);
		welcome.setLayoutY(35);

		final Label time = new Label();
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

		topDisplay.getChildren().addAll(welcome, time);

		display = new Pane();
		display.setId("deviceDisplay");
		display.setPrefSize(
				(scene.getWidth() - sideDisplay.getPrefWidth() + 10),
				(scene.getHeight() - topDisplay.getPrefHeight() + 10));
		display.setLayoutX(sideDisplay.getPrefWidth());
		display.setLayoutY(topDisplay.getPrefHeight());
		root.getChildren().addAll(display, sideDisplay, topDisplay);

		compareTo = new DatePicker();
		compareTo.setMaxWidth(101);
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

		if (InternetConnectionCheck()) {
			displayDevices("All");
		} else {
			displayNoInternet();
		}
	}

	public void displayDevices(String chosenRoom) {
		final CurrentReadings currentReadings = new CurrentReadings();
		display.getChildren().clear();
		Pane paneBackground = new Pane();
		paneBackground
				.setStyle("-fx-background-color:white; -fx-pref-height: 40;");
		paneBackground.setLayoutX(45);
		paneBackground.setPrefWidth(display.getPrefWidth());

		sortingPane.setPrefSize(display.getPrefWidth() - 200, 40);
		sortingPane.setLayoutX(100);
		sortingPane.setId("sortingPane");

		String[] roomNames = new String[currentReadings.getRooms().size() + 1];
		roomNames[0] = "All";
		int count = 1;
		for (Room room : currentReadings.getRooms()) {
			roomNames[count] = room.getName();
			count++;
		}

		HBox hbox = new HBox(10);
		hbox.setStyle("-fx-padding:8px 0 0 30px");
		// Label sort = new Label("Sort By \t\t");
		// sort.setId("sortingLabel");

		Label roomText = new Label("Select Room:");
		roomText.setId("sortingLabel");
		final ChoiceBox<String> roomDropDown = new ChoiceBox<String>();
		roomDropDown.getItems().addAll(roomNames);
		roomDropDown.getSelectionModel().selectFirst();
		roomDropDown.setId("sortingDropDown");
		roomDropDown.setMaxWidth(100);
		roomDropDown.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldString, String newString) {
				updateDashboard(getRooms(newString));
			}
		});

		hbox.getChildren().addAll(roomText, roomDropDown);
		sortingPane.getChildren().addAll(hbox);

		vb.setLayoutY(sortingPane.getPrefHeight());
		vb.setLayoutX(55);
		vb.setStyle("-fx-padding: 0 0 0 45px");

		ScrollBar sc = new ScrollBar();
		sc.setLayoutX(display.getPrefWidth() - 20);
		sc.setPrefHeight(display.getPrefHeight());
		sc.setOrientation(Orientation.VERTICAL);
		sc.setMinWidth(20);
		sc.setMaxWidth(20);
		sc.setVisibleAmount(160);
		sc.setUnitIncrement(160);
		sc.setBlockIncrement(160);
		sc.setMax(currentReadings.getAllDevices().size() * 160);
		sc.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				vb.setLayoutY(-new_val.doubleValue()
						+ sortingPane.getPrefHeight());
			}
		});
		// PLACE AFTER THE SCROLLBAR

		ArrayList<Room> roomsToDisplay = new ArrayList<Room>();

		if (chosenRoom == "All") {
			roomsToDisplay = currentReadings.getRooms();
		} else {
			for (Room room : currentReadings.getRooms()) {
				if (room.getName() == chosenRoom) {
					roomsToDisplay.add(room);
				}
			}
		}

		Timer timer = new java.util.Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						// the "refresh" checks what has been selected from the
						// dropdown box
						// and gets the relevant rooms and updates the
						// dashboard.
						updateDashboard(getRooms(roomDropDown
								.getSelectionModel().getSelectedItem()));
					}
				});
			}
		}, 0, 300000);

		display.getChildren().addAll(vb, paneBackground, sortingPane, sc);
	}

	private ArrayList<Room> getRooms(String roomToRetrieve) {
		ArrayList<Room> rooms = new CurrentReadings().getRooms();
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
		vb.getChildren().clear();
		for (Room room : rooms) {
			Pane roomPane = room.getPane(sortingPane.getPrefWidth());
			VBox deviceBox = new VBox(10);
			deviceBox.setLayoutX(50);
			deviceBox.setLayoutY(50);
			for (final Device device : room.getDevices()) {
				Pane pane = device.getPane();
				pane.setPrefWidth(sortingPane.getPrefWidth() - 100);
				pane.setOnMouseReleased(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						changeButtons("details");
						selectedDevice = device;
						try {
							ArrayList<Device> devices = new ArrayList<Device>();
							devices.add(device);
							showDeviceDetails(devices, "24");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				deviceBox.getChildren().add(pane);
			}
			roomPane.getChildren().add(deviceBox);
			vb.getChildren().add(roomPane);
		}
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
		display.getChildren().clear();

		FlowPane graphSettingsContainer = new FlowPane(); // Contains the 3
															// panes the make
															// the graph
															// settings page

		Pane devicesPane = new Pane(); // Holds the images which the user can
										// select to add to the graph
		Pane comparePane = new Pane(); // Holds the combo boxes that allow the
										// user to select which dates they want
										// the graph to display between
		Pane submitPane = new Pane(); // Holds the drop down box for the user to
										// decide which graph they want to
										// display and the button to commit

		devicesPane.setPrefSize(display.getWidth(),
				display.getHeight() / 3 * 1.5); // sets the layout of 1 pane on
												// the top and two below, evenly
												// spaces
		comparePane.setPrefSize(display.getWidth() / 2,
				display.getHeight() / 3 * 1.5);
		submitPane.setPrefSize(display.getWidth() / 2,
				display.getHeight() / 3 * 1.5);

		// -------------------------------- Setting up the devicesPane
		// -----------------------------------------------------

		CurrentReadings currentReadings = new CurrentReadings();

		final ArrayList<Device> devices = new ArrayList<Device>(); // array of
																	// devices
		final ArrayList<String> selectedDevices = new ArrayList<String>(); // array
																			// of
																			// names
																			// of
																			// the
																			// selected
																			// devices

		devices.addAll(currentReadings.getAllDevices());

		for (int i = 0; i < devices.size(); i++) // for each device create a
													// pane with an image and a
													// label
		{
			final ImageView deviceImage = new ImageView(new Image(VeraGUI.class
					.getResource("/Resources/" + devices.get(i).getImage())
					.toExternalForm())); // add the image

			deviceImage.setFitHeight(100); // image sizing
			deviceImage.setFitWidth(100);

			deviceImage.setLayoutX(25); // image layout
			deviceImage.setLayoutY(20);

			final Label deviceLabel = new Label(devices.get(i).getName()); // name
																			// of
																			// the
																			// device

			deviceLabel.setPrefWidth(100); // label sizing

			deviceLabel.setLayoutX(20); // label layout
			deviceLabel.setLayoutY(120);

			final Pane imagePane = new Pane(); // pane to contain the image and
												// the label
			imagePane.setLayoutY(30);
			imagePane
					.setStyle("-fx-border-color:grey; -fx-border-width: 3; -fx-border-style: solid;");

			imagePane.setPrefSize(144, 145); // sizing the pane

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
							if (imagePane.getWidth() == 144) {
								imagePane
										.setStyle("-fx-border-color:green; -fx-border-width: 3; -fx-border-style: solid;");
								imagePane.setPrefSize(145, 145);
								selectedDevices.add(deviceLabel.getText());
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
								selectedDevices.remove(deviceLabel.getText());
								imagePane.setPrefSize(144, 145);
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

			imagePane.setLayoutX(i * 150 + 30); // x layout position spread

			imagePane.getChildren().addAll(deviceImage, deviceLabel);

			devicesPane.getChildren().add(imagePane); // add image panes to the
														// devices pane.
		}

		// -------------------------------- Setting up the comparePane
		// -----------------------------------------------------

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

		Label compareLabel = new Label("Compare From"); // compare from label
		compareLabel.setLayoutX(40);
		compareLabel.setLayoutY(10);

		HBox compareFromRow = new HBox(5); // hbox for the compare from elements
		compareFromRow.setLayoutX(40);
		compareFromRow.setLayoutY(40);

		compareFrom = new DatePicker(); // allows to pick a date
		compareFrom.setMaxWidth(110);
		compareFrom.setValue(compareTo.getValue().minusDays(1));
		compareFrom.setId("datePicker");
		compareFrom.setEditable(false);
		compareFrom.valueProperty().addListener(dateChanger);

		compareFromHours = getBox("hours"); // allows to pick an hour
		compareFromHours.setMaxWidth(2);

		Label colonLabel = new Label(":");

		compareFromMinutes = getBox("minutes"); // allows to pick a minute
		compareFromMinutes.setMaxWidth(2);

		compareFromRow.getChildren().addAll(compareFrom, compareFromHours,
				colonLabel, compareFromMinutes); // adds the date picker and
													// hour and minute pickers

		Label compareToLabel = new Label("Compare To"); // compare to label
		compareToLabel.setLayoutX(40);
		compareToLabel.setLayoutY(110);

		HBox compareToRow = new HBox(5); // hbox for the compare from elements
		compareToRow.setLayoutX(40);
		compareToRow.setLayoutY(150);

		compareTo = new DatePicker(); // allows to pick a date
		compareTo.setMaxWidth(101);
		compareTo.setValue(LocalDate.now());
		compareTo.setId("datePicker");
		compareTo.setEditable(false);
		compareTo.valueProperty().addListener(dateChanger);

		compareToHours = getBox("hours"); // allows to pick an hour
		compareToHours.setMaxWidth(2);

		Label colonLabel2 = new Label(":");

		compareToMinutes = getBox("minutes"); // allows to pick an minute
		compareToMinutes.setMaxWidth(2);

		compareToRow.getChildren().addAll(compareTo, compareToHours,
				colonLabel2, compareToMinutes); // adds the date picker and hour
												// and minute pickers

		comparePane.getChildren().addAll(compareLabel, compareFromRow,
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
		graphType.getItems().addAll("Line Chart", "Bar Chart");
		graphType.setTooltip(new Tooltip("Select Type Of Graph"));
		graphType.getSelectionModel().selectFirst();
		graphType.setLayoutX(100);
		graphType.setLayoutY(10);

		seperateGraphs = new ChoiceBox<String>(); // creates a combo box to
													// select the type of graph
		seperateGraphs.getItems().addAll("One Chart", "Multiple Charts");
		seperateGraphs.setTooltip(new Tooltip(
				"Display readings on one graph or many"));
		seperateGraphs.getSelectionModel().selectFirst();
		seperateGraphs.setLayoutX(87);
		seperateGraphs.setLayoutY(60);

		Button createGraphButton = new Button("Generate Graph"); // creates a
																	// button
																	// used to
																	// generate
																	// the graph
		createGraphButton.setOnAction(new EventHandler<ActionEvent>() { // when
																		// button
																		// is
																		// pressed
																		// call
																		// the
																		// showDeviceDetails
																		// method
					@Override
					public void handle(ActionEvent arg0) {
						try {
							ArrayList<Device> devicesToDisplay = new ArrayList<Device>();
							for (String selectedDevice : selectedDevices) {
								for (Device device : devices) {
									if (selectedDevice.equals(device.getName())) {
										devicesToDisplay.add(device); // basically
																		// finds
																		// which
																		// devices
																		// are
																		// selected
																		// and
																		// adds
																		// them
																		// to
																		// this
																		// array
																		// list
									}
								}
							}
							showDeviceDetails(devicesToDisplay, "not24"); // <--
																			// passes
																			// the
																			// devices
																			// to
																			// be
																			// displayed
																			// in
																			// the
																			// graph
																			// and
																			// tells
																			// the
																			// method
																			// to
																			// use
						} catch (SQLException e) { // the dates selected in the
													// dropdown boxes.
							e.printStackTrace();
						}
					}
				});
		createGraphButton.setLayoutX(95); // layout of the button
		createGraphButton.setLayoutY(110);

		submitPane.getChildren().addAll(graphType, seperateGraphs,
				createGraphButton); // add graph selecter and button to the
									// submitPane

		// -------------------------------- Setting up the
		// graphSettingsContainer
		// -----------------------------------------------------

		graphSettingsContainer.getChildren().addAll(devicesPane, comparePane,
				submitPane);
		display.getChildren().add(graphSettingsContainer);
	}

	public void displaySettings() {
		display.getChildren().clear();
		VBox list = new VBox();
		list.setLayoutX(60);
		Pane pane = new Pane();

		Label roomName = new Label("Name");
		roomName.setId("DeviceName");
		roomName.setLayoutY(50);
		Label number = new Label("Number of devices");
		number.setId("DeviceName");
		number.setLayoutX(250);
		number.setLayoutY(50);
		Label action = new Label("Action");
		action.setId("DeviceName");
		action.setLayoutX(600);
		action.setLayoutY(50);
		Separator separator = new Separator();
		separator
				.setStyle("-fx-background-color:#12805C; -fx-pref-height:2px;");
		pane.getChildren().addAll(roomName, number, action);
		list.getChildren().addAll(pane, separator);

		display.getChildren().add(list);
	}

	private void displayScenes() {
		display.getChildren().clear();
	}

	private void changeButtons(String name) {
		sideButtons.getChildren().clear();
		java.util.List<String> names = new ArrayList<String>();
		switch (name) {
		case "mainMenu":
			String[] words = { "Dashboard", "Graphs", "Settings", "Scenes",
					"Quit" };
			names = Arrays.<String> asList(words);
			break;
		case "details":
			String[] words2 = { "Download CSV", "Back", "Quit" };
			names = Arrays.<String> asList(words2);
			break;
		case "settings":
			String[] words4 = { "Add a room", "Back", "Quit" };
			names = Arrays.<String> asList(words4);
			break;
		case "addRoom":
			String[] words5 = { "Cancel", "Back", "Quit" };
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

	private void showDeviceDetails(ArrayList<Device> devicesToDisplay,
			String mode) throws SQLException {

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

		display.getChildren().clear(); // fresh window

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

			for (Device device : devicesToDisplay) { // For each device
				ResultSet results = conn.getRows(device.readingFromSQL(
						compareFromDate, compareToDate)); // get a result set
															// from the database
															// containing dates
															// and readings

				readingsArray = new ArrayList<Integer>(); // array list for the
															// devices readings
				dateArray = new ArrayList<String>(); // array list for the
														// devices readings
														// dates

				while (results.next()) { // while there is still date in the
											// array
					String deviceReading = results.getString(device
							.getReadingName()); // assign the reading to
												// deviceReading
					long readingDate = results.getInt("reading_date"); // assign
																		// the
																		// date
																		// to
																		// readingDate
					if (!(deviceReading == null)) { // if the reading is not
													// null
						int convertedDeviceReading = Integer
								.parseInt(deviceReading); // convert the reading
															// into an int
						String date = new java.text.SimpleDateFormat(
								"MM/dd/yyyy HH:mm").format(new java.util.Date(
								readingDate * 1000)); // convert the date into a
														// more readable format
						date = date.replaceAll("/", "");
						date = date.replaceAll(":", "");
						date = date.replaceAll(" ", "");
						String dateHours = date.substring(8, 10);
						String dateMinutes = date.substring(10, 12);
						date = dateHours + ":" + dateMinutes;
						readingsArray.add(convertedDeviceReading); // add
																	// reading
																	// to the
																	// array
						dateArray.add(date); // add date to the array
					}
				}
				readings.add(readingsArray); // add the current devices reading
												// array to the array of reading
												// arrays
				dates.add(dateArray); // add the current devices date array to
										// the array of date arrays
			}

			String splitGraphs;

			if (mode.equals("not24")) { // If method called through graph page
				splitGraphs = seperateGraphs.getValue(); // Check type of graph
															// to display
			} else { // If method called through device page
				splitGraphs = "One Chart"; // auto set to one chart
			}

			if (splitGraphs.equals("One Chart")) { // If one chart selected give
													// all readings to the chart
													// to display all readings
													// on one graph
				Charts chart = new Charts(readings, dates, devicesToDisplay,
						"Line Chart", 1, 0); // send the arrays to the chart
												// object
				chart.show(display);
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
							singleDevicesToDisplay, "Line Chart",
							devicesToDisplay.size(), i));
					charts.get(i).show(display);
				}
			}
		} catch (SQLException e1) {
			// Label warning = new Label("Sorry No Graph Data Available");
			// warning.setPrefSize(600, 300);
			// warning.setId("graphWarning");
			// warning.setLayoutX(50);
			// warning.setLayoutY(150);
			// display.getChildren().add(warning);

			displayNoGraph();
		}
		// this adds a change listener to the drop down box and creates a new
		// graph when you select one.
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

	private void saveToCSV(Device device) {
		// TODO Auto-generated method stub

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
			csv.toCSV(file, device, compareFromDate, compareToDate);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} // Specific id and date.
	}

	public void displayNoInternet() {
		display.getChildren().clear();
		Pane pane = new Pane();
		pane.setId("noInternet");
		pane.setTranslateX(30);
		pane.setTranslateY(30);

		Label warning = new Label("Sorry, No Internet Connection");
		warning.setId("noInternetWarning");
		;
		warning.setLayoutX(230);
		warning.setLayoutY(250);

		Label warningText = new Label(
				"Internet connection needed to retrieve data from Vera box and the database");
		warningText.setId("noInternetText");
		warningText.setLayoutX(100);
		warningText.setLayoutY(290);

		display.getChildren().addAll(pane, warning, warningText);
	}

	public void displayNoGraph() {
		display.getChildren().clear();
		Pane pane = new Pane();
		pane.setId("noInternet");
		pane.setTranslateX(30);
		pane.setTranslateY(30);

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

		display.getChildren().addAll(pane, warning, warningText);
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
}
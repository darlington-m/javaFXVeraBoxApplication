package GUI;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import DataRetrival.CurrentReadings;
import DataRetrival.MySQLConnect;
import Devices.Device;
import Devices.Room;
import Exports.CSV;
import Graphs.Charts;

public class VeraGUI extends Application {
//Comment to push to Darlington

	MySQLConnect conn = new MySQLConnect();

	private Scene scene;
	private Stage stage;
	private Pane root, display;
	private long lastCompareToDate, lastCompareFromDate;
	private ChoiceBox<String> compareToHours, compareToMinutes,
			compareFromHours, compareFromMinutes, secondCompareFromHours,
			secondCompareFromMinutes, secondCompareFromHours2,
			secondCompareFromMinutes2;
	private DatePicker compareTo, compareFrom, secondCompareTo,
			secondCompareFrom;
	private VBox sideButtons;
	private RadioButton compareone;
	private ChoiceBox<String> graphType;
	private ArrayList<Integer> readingsArray = new ArrayList<Integer>();
	private ArrayList<String> dateArray = new ArrayList<String>();
	private Device selectedDevice;

	ArrayList<Button> buttons = new ArrayList<Button>();

	EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {
			switch (((Button) arg0.getSource()).getText()) {
			case "Dashboard":
				displayDevices();
				break;
			case "Settings":
				changeButtons("settings");
				displaySettings();
				break;
			case "Account":
				displayAccountInfo();
				break;
			case "Quit":
				System.exit(0);
				break;
			case "Scenes":
				displayScenes();
				break;
			case "Compare":
				if (sideButtons.getChildren().get(1) instanceof VBox) {
					changeButtons("details");
				} else {
					changeButtons("compare");
				}
				break;
			case "Back":
				display.getChildren().clear();
				changeButtons("mainMenu");
				graphType.setValue("Line Chart");
				displayDevices();
				break;
			case "Cancel":
				displaySettings();
				break;
			case "Add a room":
				changeButtons("addRoom");
				break;
			case "Set Temperature":
				setRadiatorTemp();
				break;
			case "Download CSV":
				System.out.println("Date 1:" + compareTo.getValue()
						+ "\n Date 2:" + compareFrom.getValue());
				System.out.println(turnDateToLong(compareFrom.getValue()));

				saveToCSV(selectedDevice);
				break;
			}
		}
	};

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

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

		sideButtons = new VBox(0);
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

		secondCompareTo = new DatePicker();
		secondCompareFrom = new DatePicker();
		secondCompareTo.setValue(LocalDate.now());
		secondCompareFrom.setValue(secondCompareTo.getValue().minusDays(1));
		secondCompareTo.setId("datePicker");
		secondCompareFrom.setId("datePicker");
		secondCompareTo.setDisable(true);
		secondCompareFrom.setDisable(true);

		ChangeListener<LocalDate> secondChanger = new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0,
					LocalDate oldDate, LocalDate newDate) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("A");
				LocalDate date = LocalDate.parse(oldDate.toString(), formatter);
				System.out
						.println("Old Date :" + date + "New Date :" + newDate);
			}
		};

		secondCompareTo.valueProperty().addListener(secondChanger);
		secondCompareFrom.valueProperty().addListener(secondChanger);

		compareTo.setDayCellFactory(dayCellFactory);
		compareFrom.setDayCellFactory(dayCellFactory);
		secondCompareTo.setDayCellFactory(dayCellFactory);
		secondCompareFrom.setDayCellFactory(dayCellFactory);

		// ToggleGroup group = new ToggleGroup();
		compareone = new RadioButton("Enabled");
		compareone.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (((RadioButton) arg0.getSource()).isSelected()) {
					secondCompareTo.setDisable(false);
					secondCompareFrom.setDisable(false);
					secondCompareFromHours.setDisable(false);
					secondCompareFromMinutes.setDisable(false);
					secondCompareFromHours2.setDisable(false);
					secondCompareFromMinutes2.setDisable(false);
				} else {
					secondCompareTo.setDisable(true);
					secondCompareFrom.setDisable(true);
					secondCompareFromHours.setDisable(true);
					secondCompareFromMinutes.setDisable(true);
					secondCompareFromHours2.setDisable(true);
					secondCompareFromMinutes2.setDisable(true);
				}
			}
		});

		graphType = new ChoiceBox<String>();
		graphType.getItems().addAll("Line Chart", "Bar Chart");
		graphType.setLayoutX(topDisplay.getPrefWidth() - 130);
		graphType.setLayoutY(20);
		graphType.setTooltip(new Tooltip("Select Type Of Graph"));
		graphType.getSelectionModel().selectFirst();

		Label colonLabel = new Label(":");
		secondCompareFromHours = getBox("hours");
		secondCompareFromHours.setMaxWidth(10);
		secondCompareFromMinutes = getBox("minutes");
		secondCompareFromMinutes.setDisable(true);
		secondCompareFromHours.setDisable(true);
		secondCompareFromHours2 = getBox("hours");
		secondCompareFromMinutes2 = getBox("minutes");
		secondCompareFromMinutes2.setDisable(true);
		secondCompareFromHours2.setDisable(true);

		stage.show();
		displayDevices();
	}

	public void displayDevices() {
		display.getChildren().clear();
		Pane paneBackground = new Pane();
		paneBackground
				.setStyle("-fx-background-color:white; -fx-pref-height: 40;");
		paneBackground.setLayoutX(45);
		paneBackground.setPrefWidth(display.getPrefWidth());

		final Pane sortingPane = new Pane();
		sortingPane.setPrefSize(display.getPrefWidth() - 200, 40);
		sortingPane.setLayoutX(100);
		sortingPane.setId("sortingPane");

		HBox hbox = new HBox(10);
		hbox.setStyle("-fx-padding:8px 0 0 30px");
		Label sort = new Label("Sort By \t\t");
		sort.setId("sortingLabel");
		Label roomText = new Label("Rooms:");
		roomText.setId("sortingLabel");
		ChoiceBox<String> rooms = new ChoiceBox<String>();
		rooms.getItems().addAll("Living Room", "Kitchen", "Study", "Bedroom",
				"BathRoom");
		rooms.getSelectionModel().selectFirst();
		rooms.setId("sortingDropDown");
		rooms.setMaxWidth(100);
		Label devicesText = new Label("\t\tDevices:");
		devicesText.setId("sortingLabel");
		ChoiceBox<String> deviceList = new ChoiceBox<String>();
		deviceList.getItems().addAll("4 in 1 Sensor", "Heat Sensor",
				"Light Sensor", "Danfoss Radiator");
		deviceList.getSelectionModel().selectFirst();
		deviceList.setId("sortingDropDown");
		deviceList.setMaxWidth(100);

		hbox.getChildren().addAll(sort, roomText, rooms, devicesText,
				deviceList);
		sortingPane.getChildren().addAll(hbox);

		final VBox vb = new VBox(30);
		vb.setLayoutY(sortingPane.getPrefHeight());
		vb.setLayoutX(55);
		vb.setStyle("-fx-padding: 0 0 0 45px");

		ScrollBar sc = new ScrollBar();
		sc.setLayoutX(display.getPrefWidth() - 22);
		sc.setPrefHeight(display.getPrefHeight());
		sc.setOrientation(Orientation.VERTICAL);
		sc.setMinWidth(22);
		sc.setMaxWidth(22);
		sc.setMin(0);
		sc.setMax(1000);
		sc.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				vb.setLayoutY(-new_val.doubleValue()
						+ sortingPane.getPrefHeight());
			}
		});
		CurrentReadings currentReadings = new CurrentReadings();

		for (Room room : currentReadings.getRooms()) {
			Pane roomPane = room.getPane(sortingPane.getPrefWidth());
			VBox deviceBox = new VBox(10);
			deviceBox.setLayoutX(50);
			deviceBox.setLayoutY(50);
			for (final Device device : room.getDevices()) {
				System.out.println("test");
				Pane pane = device.getPane();
				pane.setPrefWidth(sortingPane.getPrefWidth() - 100);
				pane.setOnMouseReleased(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						changeButtons("details");
						selectedDevice = device;
						try {
							showDeviceDetails(device);
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
		display.getChildren().addAll(vb, paneBackground, sortingPane, sc);
	}

	// roomPane.setId("roomPane");
	// VBox deviceBox = new VBox(10);
	// roomPane.setPrefWidth(sortingPane.getPrefWidth());
	// deviceBox.setLayoutX(50);
	// deviceBox.setLayoutY(50);
	// Label roomName = new Label("Room : " + room.getName());
	// roomName.setLayoutX(20);
	// roomName.setLayoutY(15);
	// roomName.setId("roomName");
	// roomPane.getChildren().add(roomName);
	// for (final Device device : room.getDevices()) {
	// Pane pane = device.getPane();
	// pane.setPrefWidth(sortingPane.getPrefWidth()-100);
	// System.out.println(device.getDetails());
	// pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
	//
	// @Override
	// public void handle(MouseEvent arg0) {
	// changeButtons("details");
	// selectedDevice = device;
	// try {
	// showDeviceDetails(device);
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// deviceBox.getChildren().add(pane);
	// }
	// roomPane.getChildren().add(deviceBox);

	// }

	// END OF TESTING

	// UN COMMENT WHEN ADDING TO DB AND ABLE TO GET DATA
	// try {
	// for(Device device: JsonToJava.getData()){
	// Pane pane = device.getPane();
	// pane.setOnMouseReleased(new EventHandler<MouseEvent>(){
	// @Override
	// public void handle(MouseEvent arg0) {
	// showDeviceDetails(device);
	// changeButtons("details");
	// }});
	// vb.getChildren().add(device.getPane());
	// }
	//
	// display.getChildren().addAll(vb,paneBackground,sortingPane,sc);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }

	// }

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

	public void displayAccountInfo() {
		display.getChildren().clear();
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

		Test test = new Test();
		ArrayList<Room> rooms = test.run();

		for (Room room : rooms) {
			list.getChildren().add(room.getDetailsPane());
		}
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
			String[] words = { "Dashboard", "Settings", "Scenes", "Quit" };
			names = Arrays.<String> asList(words);
			break;
		case "details":
			String[] words2 = { "Compare", "Download CSV", "Back", "Quit" };
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
		case "compare":

			String[] words3 = { "Compare", "Download CSV", "Back", "Quit" };
			names = Arrays.<String> asList(words3);

			VBox dropdown = new VBox(5);
			dropdown.setId("dropdown");
			sideButtons.getChildren().add(dropdown);
			Label compareLabel = new Label("Compare From");

			Label colonLabel = new Label(":");
			Label colonLabel2 = new Label(":");

			HBox compareFromRow = new HBox(5);
			compareFromHours = getBox("hours");
			compareFromHours.setMaxWidth(2);
			compareFromMinutes = getBox("minutes");
			compareFromMinutes.setMaxWidth(2);
			compareFromRow.getChildren().addAll(compareFrom, compareFromHours,
					colonLabel, compareFromMinutes);
			HBox compareToRow = new HBox(5);
			compareToHours = getBox("hours");
			compareToHours.setMaxWidth(2);
			compareToMinutes = getBox("minutes");
			compareToMinutes.setMaxWidth(2);
			compareToRow.getChildren().addAll(compareTo, compareToHours,
					colonLabel2, compareToMinutes);

			HBox compareFromRow2 = new HBox(5);
			compareFromRow2.getChildren().addAll(secondCompareFrom,
					secondCompareFromHours, secondCompareFromMinutes);
			HBox compareToRow2 = new HBox(5);
			compareToRow2.getChildren().addAll(secondCompareTo,
					secondCompareFromHours2, secondCompareFromMinutes2);

			Label compareToLabel = new Label("Compare To");
			Label compareLabel2 = new Label("Compare From");
			Label compareToLabel2 = new Label("Compare To");
			HBox label = new HBox(15);// this adds the enable button
			label.getChildren().addAll(compareLabel2, compareone);

			Button submitCompare = new Button("Compare");
			submitCompare.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						showDeviceDetails(selectedDevice, "test");
						graphType.getSelectionModel().selectFirst();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			dropdown.getChildren().addAll(compareLabel, compareFromRow,
					compareToLabel, compareToRow, label, compareFromRow2,
					compareToLabel2, compareToRow2, submitCompare);
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

	private void showDeviceDetails(final Device device) throws SQLException {
		changeButtons("compare");
		Calendar currentDate = Calendar.getInstance();

		compareFromHours.getSelectionModel().select(
				currentDate.get(Calendar.HOUR_OF_DAY));
		compareToHours.getSelectionModel().select(
				currentDate.get(Calendar.HOUR_OF_DAY));
		if (currentDate.get(Calendar.MINUTE) < 55) {
			compareFromMinutes.getSelectionModel().select(
					(int) currentDate.get(Calendar.MINUTE) / 5 + 1);
			compareToMinutes.getSelectionModel().select(
					(int) currentDate.get(Calendar.MINUTE) / 5 + 1);
		} else {
			compareFromMinutes.getSelectionModel().select(0);
			compareToMinutes.getSelectionModel().select(0);
		}

		String trimmedCurrentDate = Long
				.toString(currentDate.getTimeInMillis() / 1000);
		trimmedCurrentDate = trimmedCurrentDate.substring(0, 10);

		lastCompareFromDate = Long.parseLong(trimmedCurrentDate) - 86400;
		lastCompareToDate = Long.parseLong(trimmedCurrentDate);

		ResultSet results = conn.getRows(device.readingFromSQL(
				lastCompareFromDate, lastCompareToDate));
		display.getChildren().clear();
		display.getChildren().addAll(graphType); // adds the dropdownbox for
													// selecting different
													// grpahs

		try {
			readingsArray = new ArrayList<Integer>();
			while (results.next()) {
				String temp = results.getString(device.getReadingName());
				long temp2 = results.getInt("reading_date");
				if (!(temp == null)) {
					int temp3 = Integer.parseInt(temp);
					String date = new java.text.SimpleDateFormat(
							"MM/dd/yyyy HH:mm").format(new java.util.Date(
							temp2 * 1000));
					date = date.replaceAll("/", "");
					date = date.replaceAll(":", "");
					date = date.replaceAll(" ", "");
					String dateHours = date.substring(8, 10);
					String dateMinutes = date.substring(10, 12);
					date = dateHours + ":" + dateMinutes;
					readingsArray.add(temp3);
					dateArray.add(date);
				}
			}
			display.getChildren().addAll(
					device.showDeviceDetails().getChildren());
			Charts chart = new Charts(readingsArray, dateArray, device,
					"Line Chart");
			chart.show(display);
		} catch (SQLException e1) {
			Label warning = new Label("Sorry No Graph Data Available");
			warning.setPrefSize(600, 300);
			warning.setId("graphWarning");
			warning.setLayoutX(50);
			warning.setLayoutY(150);
			display.getChildren().add(warning);
			graphType.setDisable(true);
		}
		// this adds a change listener to the drop down box and creates a new
		// graph when you select one.
		graphType.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					public void changed(
							ObservableValue<? extends String> source,
							String oldValue, String newValue) {
						display.getChildren().remove(
								display.getChildren().size() - 1); // removes
																	// old graph
						Charts chart = new Charts(readingsArray, dateArray,
								device, newValue);
						chart.show(display);

					}
				});
	}

	private void showDeviceDetails(final Device device, String userSet) 
			throws SQLException {
		lastCompareFromDate = (compareFrom.getValue().toEpochDay() * 86400)
				+ (Long.parseLong(compareFromHours.getValue()) * 3600)
				+ (Long.parseLong(compareFromMinutes.getValue()) * 60);
		lastCompareToDate = (compareTo.getValue().toEpochDay() * 86400)
				+ (Long.parseLong(compareToHours.getValue()) * 3600)
				+ (Long.parseLong(compareToMinutes.getValue()) * 60) + 60;
		ResultSet results = conn.getRows(device.readingFromSQL(
				lastCompareFromDate, lastCompareToDate));
		display.getChildren().clear();
		display.getChildren().addAll(graphType); // adds the drop down box for
													// selecting different
													// graphs

		try {
			readingsArray = new ArrayList<Integer>();
			dateArray = new ArrayList<String>();
			while (results.next()) {
				String temp = results.getString(device.getReadingName());
				long temp2 = results.getInt("reading_date");
				if (!(temp == null)) {
					int temp3 = Integer.parseInt(temp);
					String date = new java.text.SimpleDateFormat(
							"MM/dd/yyyy HH:mm").format(new java.util.Date(
							temp2 * 1000));
					date = date.replaceAll("/", "");
					date = date.replaceAll(":", "");
					date = date.replaceAll(" ", "");
					String dateHours = date.substring(8, 10);
					String dateMinutes = date.substring(10, 12);
					date = dateHours + ":" + dateMinutes;
					readingsArray.add(temp3);
					dateArray.add(date);
				}
			}
			display.getChildren().addAll(
					device.showDeviceDetails().getChildren());
			Charts chart = new Charts(readingsArray, dateArray, device,
					"Line Chart");
			chart.show(display);
		} catch (SQLException e1) {
			Label warning = new Label("Sorry No Graph Data Available");
			warning.setPrefSize(600, 300);
			warning.setId("graphWarning");
			warning.setLayoutX(50);
			warning.setLayoutY(150);
			display.getChildren().add(warning);
			graphType.setDisable(true);
		}
		// this adds a change listener to the drop down box and creates a new
		// graph when you select one.
		graphType.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					public void changed(
							ObservableValue<? extends String> source,
							String oldValue, String newValue) {
						display.getChildren().remove(
								display.getChildren().size() - 1); // removes
																	// old graph
						Charts chart = new Charts(readingsArray, dateArray,
								device, newValue);
						chart.show(display);

					}
				});
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
		fileChooser.setInitialFileName("veraData_" + lastCompareFromDate
				+ "_to_" + lastCompareToDate + ".csv");
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		// Show save file dialog
		File file = fileChooser.showSaveDialog(stage);

		CSV csv = new CSV();
		try {
			csv.toCSV(file, device, lastCompareFromDate, lastCompareToDate);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Specific id and date.
	}

	private Long turnDateToLong(LocalDate locDate) {
		// TODO Auto-generated method stub

		LocalDate ld = compareTo.getValue();
		Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault())
				.toInstant();
		Date res = Date.from(instant);
		long l = res.getTime() / 1000;
		return l;
	}
	
	
	private void setRadiatorTemp() {
		// TODO Auto-generated method stub
		
		System.out.println("Clicked to change temp");
		
		
		final Stage newStage = new Stage();
		VBox comp = new VBox();

		Pane pane = new Pane();
		pane.setPrefSize(300, 300);
		pane.setLayoutX(0);
		pane.setStyle("-fx-background-color: #3399cc");
		comp.getChildren().add(pane);

		final TextField wantedSetPoint = new TextField("Enter Temp");
		wantedSetPoint.setMinSize(200, 40);
		wantedSetPoint.setMaxSize(200, 40);
		wantedSetPoint.setLayoutX(50);
		wantedSetPoint.setLayoutY(50);
		wantedSetPoint.setStyle("-fx-font-size: 20");
		
		Button saveBtn = new Button("Save Temp");
		saveBtn.setLayoutX(88);
		saveBtn.setLayoutY(130);
		saveBtn.setMinSize(124, 40);
		saveBtn.setMaxSize(124, 40);
		saveBtn.setStyle("-fx-font-weight: bold");
		saveBtn.setStyle("-fx-font-size: 20");

		pane.getChildren().addAll(wantedSetPoint, saveBtn);
		Scene stageScene = new Scene(comp, 300, 300);
		newStage.setScene(stageScene);
		newStage.show();

		saveBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				newStage.close();
			}
		});

		newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				newStage.close();
			}
		});
	}
}
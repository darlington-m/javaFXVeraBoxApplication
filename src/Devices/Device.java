package Devices;

import java.awt.Toolkit;
//import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import GUI.VeraGUI;

public abstract class Device {

	String name;
	int id;
	String altid;
	int category;
	int subcategory;
	int room;
	int parent;
	String image = "";
	String readingName;
	int currentReading;
	
	public Device(){
		
	}

	public Device(String name, int id, String altid, int category,
			int subcategory, int room, int parent, String image,
			String readingName, int currentReading) {
		super();
		this.name = name;
		this.id = id;
		this.altid = altid;
		this.category = category;
		this.subcategory = subcategory;
		this.room = room;
		this.parent = parent;
		this.image = null;
		this.readingName = readingName;
		this.currentReading = currentReading;
	}

	@Override
	public String toString() {
		return "Name: " + name + " Id: " + id + " AltID: " + altid
				+ " Category: " + category + " Subcategory: " + subcategory
				+ " Room: " + room + " Parent: " + parent;
	}

	public String getDetails() {
		return "Name: " + name + "\nId: " + id + "\nAltID: " + altid
				+ "\nCategory: " + category + "\nSubcategory: " + subcategory
				+ "\nRoom: " + room + "\nParent: " + parent;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getAltid() {
		return altid;
	}

	public int getCategory() {
		return category;
	}

	public int getSubcategory() {
		return subcategory;
	}

	public int getRoom() {
		return room;
	}

	public int getParent() {
		return parent;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public abstract String readingFromSQL(long startDate, long endDate);

	public String getReadingName() {
		return readingName;
	}

	public Pane getPane() {
		Pane pane = new Pane();
		pane.setId("devices");
		pane.setPrefSize(600, 200);

		Rectangle imageView = new Rectangle(100, 100);
		imageView.setUserData(this);
		imageView.setFill(new ImagePattern(new Image(VeraGUI.class.getResource(
				"/Resources/radiator.jpg").toExternalForm())));
		imageView.setLayoutX(30);
		imageView.setLayoutY(30);

		Label name = new Label(getName());
		name.setId("DeviceName");
		name.setLayoutX(200);
		name.setLayoutY(20);

		pane.getChildren().addAll(name, imageView);
		return pane;
	}

	public Pane showDeviceDetails() {
		Pane pane = new Pane();

		final TextField textbox = new TextField();
		textbox.setText(getName());
		textbox.setPrefSize(250, 50);
		textbox.setLayoutX(200);
		textbox.setLayoutY(5);
		textbox.setId("textField");

		final Label capsLock = new Label("Caps Lock is on");
		capsLock.setId("capsLock");
		capsLock.setLayoutX(225);
		capsLock.setLayoutY(60);
		textbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				if (key.getCode() == KeyCode.CAPS) {
					if (Toolkit.getDefaultToolkit().getLockingKeyState(20)) {
						capsLock.setText("Caps Lock is on");
						capsLock.setVisible(true);
						System.out.println("ON");
					} else {
						capsLock.setText("Caps Lock is off");
						capsLock.setVisible(false);
						System.out.println("OFF");
					}
				}
			}
		});

		textbox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (textbox.getText().isEmpty()) {
					capsLock.setText("Name Cannot Be Blank");
					capsLock.setVisible(true);
				} else {
					renameDevice(textbox.getText());
				}
			}
		});
		capsLock.setVisible(false);

		Rectangle imageView = new Rectangle(100, 100);
		imageView.setUserData(this);
		imageView.setFill(new ImagePattern(new Image(VeraGUI.class.getResource(
				"/Resources/" + getImage()).toExternalForm())));
		imageView.setLayoutX(50);
		imageView.setLayoutY(30);

		pane.getChildren().addAll(textbox, imageView, capsLock);// ,text
		return pane;
	}

	public void renameDevice(String newName) {
		String urlString = new String(
				"http://ip_address:3480/data_request?id=device&action=rename&device="
						+ getId() + "&name=" + newName + "&room=" + getRoom());
		// remove whitespace
		urlString.replaceAll("\\s", "");
		try {
			// URL url;
			// url = new URL(urlString);
			// URLConnection urlCon = null;
			// urlCon = url.openConnection();
			// name = newName;
			URL url = new URL(urlString);
			url.openConnection();
			name = newName;
		} catch (IOException e) {
			System.out.println("Cannot connect renaming URL for " + getName());
		}

	}
}

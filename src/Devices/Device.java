package Devices;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import GUI.VeraGUI;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

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

    @Override
    public String toString(){
    	return "Name: " + name 
    			+ " Id: " + id
    			+ " AltID: " + altid
    			+ " Category: " + category
    			+ " Subcategory: " + subcategory
    			+ " Room: " + room
    			+ " Parent: " + parent;
    }
    
    public String getDetails(){
    	return "Name: " + name + 
    			"\nId: " + id + 
    			"\nAltID: " + altid + 
    			"\nCategory: " + category + 
    			"\nSubcategory: " + subcategory + 
    			"\nRoom: " + room + 
    			"\nParent: " + parent;
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
	public String getImage(){
		return image;
	}
	public void setImage(String image){
		this.image = image;
	}
	
	public abstract String readingFromSQL();
	
	public String getReadingName(){
		return readingName;
	}

	public Pane getPane(){
		Pane pane = new Pane();
		pane.setId("devices");
		pane.setPrefSize(600,200);

		Rectangle imageView = new Rectangle(100,100);
		imageView.setUserData(this);
		imageView.setFill(new ImagePattern(new Image(VeraGUI.class.getResource("/Resources/"+ getImage()).toExternalForm())));
		imageView.setLayoutX(30);
		imageView.setLayoutY(30);
		
		Label name = new Label(getName());
		name.setId("DeviceName");
		name.setLayoutX(200);
		name.setLayoutY(20);
		
		pane.getChildren().addAll(name,imageView);
		return pane;
	}
	public Pane showDeviceDetails(){
		Pane pane = new Pane();
		Label text = new Label(getName());
		text.setId("deviceDetails");
		Rectangle imageView = new Rectangle(100,100);
		imageView.setUserData(this);
		imageView.setFill(new ImagePattern(new Image(VeraGUI.class.getResource("/Resources/"+ getImage()).toExternalForm())));
		imageView.setLayoutX(50);
		imageView.setLayoutY(30);

		text.setLayoutX(300);
		text.setLayoutY(5);
		pane.getChildren().addAll(imageView,text);
		return pane;
	}
	public void renameDevice(String newName) throws MalformedURLException{
		 String urlString = new String("http://ip_address:3480/data_request?id=device&action=rename&device="+ getId() +"&name="+ newName + "&room=" + getRoom());
		 //remove whitespace
		 urlString.replaceAll("\\s","");
		 URL url = new URL(urlString);
		 URLConnection urlCon = null;
		 try {
			 urlCon = url.openConnection();
		} catch (IOException e) {
			
			System.out.println("Cannot connect renaming URL for " + getName());
		}
		 
	}
	
}

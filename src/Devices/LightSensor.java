package Devices;
import java.util.Date;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class LightSensor extends Device implements Sensor{
    private String light;
    
    public LightSensor(){
        image = "bulb.png";
        readingName = "light";
    }
    @Override
    public int getReading(){    	
        return Integer.parseInt(light);
    }
    @Override
    public String readingToSQL() {
    	System.out.println(light);
        return new String("INSERT INTO Reading"
        + "(reading_date, reading_device_name, id, altid, category, subcategory, room, parent, light) VALUES ('" 
    	+ new Date()
        + "', '" + getName()
        + "', '"  + getId()
        + "',  '"  + getAltid()
        + "',  '"  + getCategory()
        + "',  '"  + getSubcategory()
        + "',  '"  + getRoom()
        + "',  '"  +getParent()
        + "',  '"  +getReading()+  "')");
    }
    
	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String("SELECT light, reading_date FROM Reading WHERE id =  '" + getId() + "' AND reading_date >='" + startDate + "' AND reading_date <='" + endDate + "'");
	}
	
    @Override
    public String toString(){
    	return super.toString() + " Light: " + light + "Image: " + super.getImage();
    }
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nLight: " + light;
    }
    public Pane getPane(){
    	Pane pane = super.getPane();
		Label reading = new Label("Reading: " + getReading());
		reading.setLayoutX(200);
		reading.setLayoutY(100);
		pane.getChildren().addAll(reading);
		return pane;
    }
}

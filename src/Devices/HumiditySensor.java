package Devices;
import java.util.Date;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class HumiditySensor extends Device implements Sensor{
    private int humidityReading;
    public HumiditySensor(){
    	 super.setImage("humidity.jpg");
    	 readingName = "humidity";
    }
    @Override
    public int getReading(){
        return humidityReading;
    }

    @Override
    public String readingToSQL() {
        return new String("INSERT INTO Reading (reading_date, reading_device_name, id, altid, category, subcategory, room, parent, humidity) VALUES ('" + new Date() + "', '" + getName() + "', '"  + getId() + "',  '"  + getAltid() + "',  '"  + getCategory() + "',  '"  + getSubcategory() + "',  '"  + getRoom() + "',  '"  +getParent() + "',  '"  +getReading()  +  "')");
    }
    
	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String("SELECT humidity, reading_date FROM Reading WHERE id =  '" + getId() + "' AND reading_date >='" + startDate + "' AND reading_date <='" + endDate + "'");
	}
	
    @Override
    public String toString(){
    	return super.toString() + " Humidity Reading: " + humidityReading;
    }
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nHumidity: " + humidityReading;
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
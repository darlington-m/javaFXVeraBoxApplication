import java.util.Date;

public class TemperatureSensor extends Device implements Sensor{
	
    private int temperature;
    
    public TemperatureSensor(){
    	 super.setImage("temperature.png");
    }
    @Override
    public int getReading(){
        return temperature;
    }
    @Override
    public String readingToSQL() {
        return new String("INSERT INTO Reading (reading_date, reading_device_name, id, altid, category, subcategory, room, parent, temperature) VALUES ('" + new Date() + "', '" + getName() + "', '"  + getId() + "',  '"  + getAltid() + "',  '"  + getCategory() + "',  '"  + getSubcategory() + "',  '"  + getRoom() + "',  '"  +getParent() + "',  '"  +getReading()  +  "')");
    }
    @Override
    public String toString(){
		return super.toString() + " Temperature: " + temperature;
    }
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nTemperature: " + temperature;
    }
}

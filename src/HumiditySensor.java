import java.util.Date;

public class HumiditySensor extends Device implements Sensor{
    private int humidityReading;
    HumiditySensor(){
    	 super.setImage("humidity.jpg");
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
    public String toString(){
    	return super.toString() + " Humidity Reading: " + humidityReading;
    }
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nHumidity: " + humidityReading;
    }
}
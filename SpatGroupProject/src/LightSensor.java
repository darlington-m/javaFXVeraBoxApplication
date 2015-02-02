import java.util.Date;

public class LightSensor extends Device implements Sensor{
    private String light;
    
    public LightSensor(){
        super.setImage("lightbulb1.jpg");
    }
    @Override
    public int getReading(){    	
        return Integer.parseInt(light.substring(0, 3));
    }
    @Override
    public String readingToSQL() {
        return new String("INSERT INTO Reading (reading_date, reading_device_name, id, altid, category,subcategory,room,parent,light) VALUES (" + new Date() + ", " + getName() + ",  "  + getId() + ",  "  + getAltid() + ",  "  + getCategory() + ",  "  + getSubcategory() + ",  "  + getRoom() + ",  "  +getParent() + ",  "  +getLight()  +  ")");
    }
    @Override
    public String toString(){
    	return super.toString() + " Light: " + light + "Image: " + super.getImage();
    }
    public int getLight(){
    	return Integer.parseInt(light.substring(0,3));
    }
}

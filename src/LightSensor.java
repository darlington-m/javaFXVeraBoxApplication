import java.util.Date;

public class LightSensor extends Device implements Sensor{
    private String light;
    
    public LightSensor(){
        super.setImage("bulb.png");
    }
    @Override
    public int getReading(){    	
        return 0;
    }
    @Override
    public String readingToSQL() {
    	System.out.println(light);
        return new String("INSERT INTO Reading (reading_date, reading_device_name, id, altid, category, subcategory, room, parent, light) VALUES ('" + new Date() + "', '" + getName() + "', '"  + getId() + "',  '"  + getAltid() + "',  '"  + getCategory() + "',  '"  + getSubcategory() + "',  '"  + getRoom() + "',  '"  +getParent() + "',  '"  +getLight()  +  "')");
    }
    @Override
    public String toString(){
    	return super.toString() + " Light: " + light + "Image: " + super.getImage();
    }
    public int getLight(){
    	return 0;
    }
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nLight: " + light;
    }
}

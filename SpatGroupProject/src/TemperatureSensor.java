public class TemperatureSensor extends Device implements Sensor{
	
    private int temperature;
    
    public TemperatureSensor(){
    	 super.setImage("lightbulb1.jpg");
    }
    @Override
    public int getReading(){
        return temperature;
    }
    @Override
    public String readingToSQL() {
        return new String("Insert into reading");
    }
    @Override
    public String toString(){
		return super.toString() + " Temperature: " + temperature;
    	
    }
}

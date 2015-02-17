public class HumiditySensor extends Device implements Sensor{
    private int humidityReading;
    HumiditySensor(){
    	 super.setImage("lightbulb1.jpg");
    }
    @Override
    public int getReading(){
        return humidityReading;
    }

    @Override
    public String readingToSQL() {
        return new String("Insert into reading");
    }
    @Override
    public String toString(){
    	return super.toString() + " Humidity Reading: " + humidityReading;
    }
}
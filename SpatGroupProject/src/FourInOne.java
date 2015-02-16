
public class FourInOne extends Device {

	int batterylevel;
	TemperatureSensor temperatureSensor;
	LightSensor lightSensor;
	HumiditySensor humiditySensor;
	int armedtripped;
	int armed;
	int state;
	String comment;
	int tripped;
	int lasttrip;
	
	public FourInOne(){
        super.setImage("4in1.jpg");
    }
	
	public String toString(){
		return super.toString() + " Battery Level: " + batterylevel + " Temperature: " + temperatureSensor.getReading() + " Light: " + lightSensor.getReading() + " Humidity: " + humiditySensor.getReading() + " ArmedTripped: " + armedtripped + " Armed: " + armed + " State: " + state + " Comment: " + comment + " Tripped: " + tripped + " LastTrip: " + lasttrip; 
	}
	
	public void setTemp(TemperatureSensor temperatureSensor){
		this.temperatureSensor = temperatureSensor;		
	}
	public void setLight(LightSensor lightSensor){
		this.lightSensor = lightSensor;
	}
	public void setHumidity(HumiditySensor humiditySensor){
		this.humiditySensor = humiditySensor;
	}
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nTemperature: " + temperatureSensor.getReading() + 
    			"\nLight: " + lightSensor.getReading() + 
    			"\nHumidity: " + humiditySensor.getReading();
    }
}

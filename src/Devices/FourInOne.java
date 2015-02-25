package Devices;

public class FourInOne extends Device {

	int batterylevel;
	private TemperatureSensor temperatureSensor;
	private LightSensor lightSensor;
	private HumiditySensor humiditySensor;
	int armedtripped;
	int armed;
	int state;
	String comment;
	int tripped;
	int lasttrip;
	
	public FourInOne(){
        super.setImage("4in1.png");
    }
	
	public String toString(){
		return super.toString() + " Battery Level: " + batterylevel + " Temperature: " + getTemperatureSensor().getReading() + " Light: " + getLightSensor().getReading() + " Humidity: " + getHumiditySensor().getReading() + " ArmedTripped: " + armedtripped + " Armed: " + armed + " State: " + state + " Comment: " + comment + " Tripped: " + tripped + " LastTrip: " + lasttrip; 
	}
	public TemperatureSensor getTemp(){
		return getTemperatureSensor();
	}
	public LightSensor getLight(){
		return getLightSensor();
	}
	public HumiditySensor getHumidity(){
		return getHumiditySensor();
	}
	
	public void setTemp(TemperatureSensor temperatureSensor){
		this.setTemperatureSensor(temperatureSensor);		
	}
	public void setLight(LightSensor lightSensor){
		this.setLightSensor(lightSensor);
	}
	public void setHumidity(HumiditySensor humiditySensor){
		this.setHumiditySensor(humiditySensor);
	}
	
	public String readingToSQL(){
		return 	getTemperatureSensor().readingToSQL() + ";" + 
		lightSensor.readingToSQL() + "," +
		humiditySensor.readingToSQL();
	}
    
	@Override
	public String readingFromSQL() {
		return new String("SELECT humidity,light,heat FROM Reading WHERE id =  '" + getId() + "'");
	}
	
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nTemperature: " + getTemperatureSensor().getReading() + 
    			"\nLight: " + getLightSensor().getReading() + 
    			"\nHumidity: " + getHumiditySensor().getReading();
    }

	public LightSensor getLightSensor() {
		return lightSensor;
	}

	public void setLightSensor(LightSensor lightSensor) {
		this.lightSensor = lightSensor;
	}

	public TemperatureSensor getTemperatureSensor() {
		return temperatureSensor;
	}

	public void setTemperatureSensor(TemperatureSensor temperatureSensor) {
		this.temperatureSensor = temperatureSensor;
	}

	public HumiditySensor getHumiditySensor() {
		return humiditySensor;
	}

	public void setHumiditySensor(HumiditySensor humiditySensor) {
		this.humiditySensor = humiditySensor;
	}
}

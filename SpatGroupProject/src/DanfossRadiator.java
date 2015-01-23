
public class DanfossRadiator extends Device implements Sensor{
	int setpoint;
	int heat;
	int cool;
	String commands;
	int batterylevel;
	String mode;
	int state;
	String comment;
	
	public DanfossRadiator(){
        super.setImage("lightbulb1.jpg");
    }
	@Override
	public String toString(){
		return super.toString() + " SetPoint: " +setpoint + " Heat: " +heat + " Cool: " + cool + " Commands: " +commands + " BatteryLevel: " +batterylevel + " Mode: " +mode + " State:  " +state + " Comment: " +comment; 
	}

	@Override
	public int getReading() {
		return heat;
	}

	@Override
	public String readingToSQL() {
		return "" + heat;
	}
}

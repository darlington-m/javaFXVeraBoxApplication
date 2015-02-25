package Devices;
import java.util.Date;


public class DanfossRadiator extends Device implements Sensor{
	int setpoint;
	int heat;
	int cool;
	String commands;
	int batterylevel;
	String mode;
	int state;
	String comment;
	
	public DanfossRadiator(int setpoint, int heat, int cool, String commands,
			int batterylevel, String mode, int state, String comment) {
		super();
		this.setpoint = setpoint;
		this.heat = heat;
		this.cool = cool;
		this.commands = commands;
		this.batterylevel = batterylevel;
		this.mode = mode;
		this.state = state;
		this.comment = comment;
	}
	public DanfossRadiator(){
        super.setImage("radiator.jpg");
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
        return new String("INSERT INTO Reading (reading_date, reading_device_name, id, altid, category, subcategory, room, parent, heat) VALUES ('" + new Date() + "', '" + getName() + "', '"  + getId() + "',  '"  + getAltid() + "',  '"  + getCategory() + "',  '"  + getSubcategory() + "',  '"  + getRoom() + "',  '"  +getParent() + "',  '"  +getReading()  +  "')");
	}
	
	@Override
	public String readingFromSQL() {
		return new String("SELECT heat FROM Reading WHERE id =  '" + getId() + "'");
	}
	
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nSetPoint: " +setpoint + 
    			"\nHeat: " +heat + 
    			"\nCool: " + cool + 
    			"\nCommands: " +commands + 
    			"\nBatteryLevel: " +batterylevel + 
    			"\nMode: " +mode + 
    			"\nState:  " +state + 
    			"\nComment: " +comment; 
    }
}

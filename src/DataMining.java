
public class DataMining extends Device implements Sensor{
	
	String chcnt;
	
	@Override
	public String toString(){
		return super.toString() + " Chcnt: " + chcnt;
	}

	@Override
	public int getReading() {
		return 0;
	}

	@Override
	public String readingToSQL() {
		return null;
	}
	

}

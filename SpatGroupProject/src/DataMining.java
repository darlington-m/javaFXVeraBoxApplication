
public class DataMining extends Device implements Sensor{
	
	String chcnt;
	
	public DataMining(){
        super.setImage("datamining.png");
	}
	
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
    @Override
    public String getDetails(){
    	return super.getDetails() + "\nCHCNT: " + chcnt;
    }
}

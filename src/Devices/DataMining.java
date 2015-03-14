package Devices;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class DataMining extends Device implements Sensor {

	String chcnt;

	public DataMining() {
		super.setImage("datamining.png");
	}

	@Override
	public String toString() {
		return super.toString() + " Chcnt: " + chcnt;
	}

	@Override
	public int getReading() {
		return 0;
	}
	

	@Override
	public String getDetails() {
		return super.getDetails() + "\nCHCNT: " + chcnt;
	}

	public Pane getPane() {
		Pane pane = super.getPane();
		Label reading = new Label("Reading: " + getReading());
		reading.setLayoutX(200);
		reading.setLayoutY(100);
		pane.getChildren().addAll(reading);
		return pane;
	}

	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return null;
	}
}

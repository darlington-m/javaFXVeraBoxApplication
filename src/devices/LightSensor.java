package devices;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class LightSensor extends Device implements Sensor {
	private int light;

	public LightSensor() {
		image = "bulb.png";
		readingName = "light";
	}
	
	public LightSensor(String name, int id, String altid, int category,
			int subcategory, int room, int parent, int currentReading) {
		super(name, id, altid, category, subcategory, room, parent, "bulb.png", "light", currentReading);
	}

	@Override
	public int getReading() {
		return currentReading;
	}
	

	@Override
	public String readingFromSQL(long startDate, long endDate) {
		return new String(
				"SELECT light, reading_date FROM Reading WHERE id =  '"
						+ getId() + "' AND reading_date >='" + startDate
						+ "' AND reading_date <='" + endDate + "'");
	}

	@Override
	public String toString() {
		return super.toString() + " Light: " + light + "Image: "
				+ super.getImage();
	}

	@Override
	public String getDetails() {
		return super.getDetails() + "\nLight: " + light;
	}

	public Pane getPane() {
		Pane pane = super.getPane();
		Label reading = new Label(getReading() + "%");
		reading.setLayoutX(200);
		reading.setLayoutY(30);
		reading.setId("readingLabel");
		pane.getChildren().addAll(reading);
		return pane;
	}
}

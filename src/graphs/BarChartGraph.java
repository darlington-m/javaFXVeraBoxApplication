package graphs;

import java.util.ArrayList;

import devices.Device;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class BarChartGraph 
{
	private String datesRecorded;
	ArrayList<XYChart.Series<String, Number>> readingsList = new ArrayList<XYChart.Series<String, Number>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BarChart checkCompare(BarChart chart, ArrayList<ArrayList> readings, ArrayList<ArrayList> dates, ArrayList<Device> devices) 
	{
		int numberOfDisplayedPointsOfData = readings.get(0).size(); ; // How many points on the graph to be shown
		String id = "dontCompare"; //compare checker, if compare radio button not active "dontCompare" a single line, else show 2 lines on graph
		int i = 0;
		int lineOneReadings = 0;
		
		for (int j = 0; j < readings.size(); j++){
		// Information for line one displayed on the graph
		XYChart.Series lineOfReadings = new XYChart.Series(); // Default first line, second line added if id = compare2
		lineOfReadings.setName(devices.get(j).getName() + " Reading"); // Legend (bottom colour indicator) name

			// Information for line two displayed on the graph
			XYChart.Series readingsTwo = new XYChart.Series();
			readingsTwo.setName("Reading 2 " + "Reading");
			try 
			{
				while (i < readings.get(j).size()-1) // while there's still data in the array
				{
					int yAxisReadings = (int) readings.get(j).get(i); // get the readings through all index 0-array.size()-1 and add them into the arraylist
					String xAxisReadings = (String) dates.get(j).get(i);  // get all the dates for the readings and store them exactly the same way as the readings, same index's
					lineOfReadings.getData().add(new XYChart.Data(xAxisReadings, yAxisReadings)); // add the data to the chart					
					i++;
				}
				readingsList.add(lineOfReadings); // return the data back to charts to be displayed
			}
			catch (IndexOutOfBoundsException e) 
			{
				numberOfDisplayedPointsOfData = readings.get(j).size(); // default retrieval, can't get an error.
				checkCompare(chart, readings, dates, devices); // Pass everything back to the method and try again.
			}
			chart.setBarGap(0); // styling
			chart.setCategoryGap(20); //styling
			chart.getData().addAll(readingsList); // add both readings to the chart
		}
		chart.setLegendVisible(true); // show the icons to indicate which line is which
		return chart;
	}
}

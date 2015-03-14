package Graphs;

import java.util.ArrayList;

import Devices.Device;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class LineGraph 
{
	private String datesRecorded;
	ArrayList<XYChart.Series<String, Number>> readingsList = new ArrayList<XYChart.Series<String, Number>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LineChart checkCompare(LineChart chart, ArrayList<ArrayList> readings, ArrayList<ArrayList> dates, ArrayList<Device> devices) 
	{
		int numberOfDisplayedPointsOfData = readings.get(0).size(); // How many points on the graph to be shown
		if(numberOfDisplayedPointsOfData > 572)
		{
			int i = 0;
			while(i < 34)
			{
				
			}
		}
		String id = "dontCompare"; //compare checker, if compare radio button not active "dontCompare" a single line, else show 2 lines on graph
		int i = 0;
		int lineReadings = 0;
		
		// Information for line one displayed on the graph
		for (int j = 0; j < devices.size(); j++){
			System.out.println("Size: " + j);

			XYChart.Series<String, Number> lineOfReadings = new XYChart.Series<String, Number>(); // Default first line, second line added if id = compare2
			lineOfReadings.setName(devices.get(j).getName() + " Reading"); // Legend (bottom colour indicator) name
			try 
			{
				i = 0;
				while (i < readings.get(j).size()-1)  // while there's still data in the array
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
		}
		chart.getData().addAll(readingsList);
		chart.setCreateSymbols(false); // remove symbols displayed on the graph
		chart.setLegendVisible(true); // show the icons to indicate which line is which
		return chart;
	}
}
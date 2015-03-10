package Graphs;

import java.util.ArrayList;

import Devices.Device;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class LineGraph 
{
	private Long datesRecorded;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LineChart checkCompare(LineChart chart, ArrayList<Integer> array,ArrayList<Long> array2, Device device) 
	{
		int numberOfDisplayedPointsOfData = array.size(); // How many points on the graph to be shown
		String id = "dontCompare"; //compare checker, if compare radio button not active "dontCompare" a single line, else show 2 lines on graph
		int i = 0;
		int lineOneReadings = 0;
		
		// Information for line one displayed on the graph
		XYChart.Series<String, Number> readingsOne = new XYChart.Series<String, Number>(); // Default first line, second line added if id = compare2
		readingsOne.setName(device.getName() + " Reading"); // Legend (bottom colour indicator) name

		if (id.equalsIgnoreCase("dontCompare")) // If a single reading has been checked, use this
		{
			try 
			{
				while (i < array.size()-1)  // while there's still data in the array
				{
					lineOneReadings = array.get(i); // get the readings through all index 0-array.size()-1 and add them into the arraylist
					datesRecorded = array2.get(i);  // get all the dates for the readings and store them exactly the same way as the readings, same index's
					readingsOne.getData().add(new XYChart.Data(Long.toString(datesRecorded),lineOneReadings)); // add the data to the chart
					i++;
				}
					chart.getData().add(readingsOne); // return the data back to charts to be displayed
			} 
			catch (IndexOutOfBoundsException e)
			{
				numberOfDisplayedPointsOfData = array.size(); // default retrieval, can't get an error.
				checkCompare(chart, array, array2, device); // Pass everything back to the method and try again.
			}
		}
		else if (id.equalsIgnoreCase("compare2")) // If there's multiple readings being compared, use this
		{
			// Information for line two displayed on the graph
			XYChart.Series<String, Number> readingsTwo = new XYChart.Series<String, Number>();
			readingsTwo.setName(device.getName() + " Reading");  // Legend (bottom colour indicator) name
			try 
			{
				while (i < array.size()-1) // while there's still data in the array
				{
					lineOneReadings = array.get(i);  // get the readings through all index 0-array.size()-1 and add them into the arraylist
					datesRecorded = array2.get(i); // get recorded time here
					readingsOne.getData().add(new XYChart.Data(Long.toString(datesRecorded),lineOneReadings));
					readingsTwo.getData().add(new XYChart.Data(Long.toString(datesRecorded), lineOneReadings));
					i++;
				}
				chart.getData().addAll(readingsOne, readingsTwo);  // add both readings to the chart
			}
			catch (IndexOutOfBoundsException e) 
			{
				numberOfDisplayedPointsOfData = array.size(); // default retrieval, can't get an error.
				checkCompare(chart, array, array2, device); // Pass everything back to the method and try again.
			}
		}
		chart.setCreateSymbols(false); // remove symbols displayed on the graph
		chart.setLegendVisible(true); // show the icons to indicate which line is which
		return chart;
	}
}
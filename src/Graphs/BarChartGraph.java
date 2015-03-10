package Graphs;

import java.util.ArrayList;

import Devices.Device;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class BarChartGraph 
{
	private long datesRecorded;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BarChart checkCompare(BarChart chart, ArrayList<Integer> array,ArrayList<Long> array2, Device device) 
	{
		int numberOfDisplayedPointsOfData = array.size(); // How many points on the graph to be shown
		String id = "dontCompare"; //compare checker, if compare radio button not active "dontCompare" a single line, else show 2 lines on graph
		int i = 0;
		int lineOneReadings = 0;
		
		// Information for line one displayed on the graph
		XYChart.Series readingsOne = new XYChart.Series(); // Default first line, second line added if id = compare2
		readingsOne.setName(device.getName() + " Reading"); // Legend (bottom colour indicator) name

		if (id.equalsIgnoreCase("dontCompare"))  // If a single reading has been checked, use this
		{
			try 
			{
				while (i < array.size()-1) // while there's still data in the array
				{
					lineOneReadings = array.get(i);
					datesRecorded = array2.get(i);
					readingsOne.getData().add(new XYChart.Data(Integer.toString((int) datesRecorded),lineOneReadings));
					i++;
				}
				chart.getData().add(readingsOne); // add one
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
			XYChart.Series readingsTwo = new XYChart.Series();
			readingsTwo.setName("Reading 2 " + "Reading");
			try 
			{
				while (i < array.size()-1) // while there's still data in the array
				{
					lineOneReadings = array.get(i);
					datesRecorded = array2.get(i); // get recorded time here
					readingsOne.getData().add(new XYChart.Data(Integer.toString((int) datesRecorded),numberOfDisplayedPointsOfData)); // results 1
					readingsTwo.getData().add(new XYChart.Data(Integer.toString((int) datesRecorded),numberOfDisplayedPointsOfData)); // results 2
					i++;
				}
			}
			catch (IndexOutOfBoundsException e) 
			{
				numberOfDisplayedPointsOfData = array.size(); // default retrieval, can't get an error.
				checkCompare(chart, array, array2, device); // Pass everything back to the method and try again.
			}
			chart.setBarGap(0); // styling
			chart.setCategoryGap(20); //styling
			chart.getData().addAll(readingsOne, readingsTwo); // add both readings to the chart
		}
		chart.setLegendVisible(true); // show the icons to indicate which line is which
		return chart;
	}
}

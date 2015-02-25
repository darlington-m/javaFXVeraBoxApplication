package Graphs;



import java.util.ArrayList;

import Devices.Device;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class LineGraph
{
	private int readings;
	int timePeriod = 14; // amount of plots on the graph

	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	public LineChart checkCompare(LineChart chart, ArrayList<Integer> array, Device device)
	{
		String id = "compare2"; // if comparing do this, blah.
		XYChart.Series<String, Number> readingsOne = new XYChart.Series<String, Number>();
		readingsOne.setName(device.getName() + " Reading");
		int i = array.size()-timePeriod;
		int lastReadings = 0;
		
		if(id.equalsIgnoreCase("dontCompare")) // single reading
		{
			try
			{
				while(i < array.size())
				{ 
					lastReadings = array.get(i);// not sure on which readings are retrieved need to change
					readings+= 50; // get recorded time here
					readingsOne.getData().add(new XYChart.Data(Integer.toString((int) readings), lastReadings));
					i++;
				}
				chart.getData().add(readingsOne); // add one
			}
			catch(IndexOutOfBoundsException e)
			{
				System.out.println("Not enough data, showing last 2 weeks of data");
				timePeriod = 14;
				checkCompare(chart, array, device);
			}
		}
		else if(id.equalsIgnoreCase("compare2")) // single reading
		{
			XYChart.Series<String, Number> readingsTwo = new XYChart.Series<String, Number>();
			try
			{
				while(i < array.size())
				{	
					lastReadings = array.get(i);// not sure on which readings are retrieved need to change
					//int lastReadingsCompare = array.get(i); // second array being passed will populate a second array
					int random = (int) ( 40 * Math.random());
					readings+= 50; // get recorded time here
					readingsOne.getData().add(new XYChart.Data(Integer.toString((int) readings), lastReadings));
					readingsTwo.getData().add(new XYChart.Data(Integer.toString((int) readings), random));
					readingsOne.setName(device.getName() + " Reading One"); // second reading (replace with primary readings or comparison setting)
					readingsTwo.setName(device.getName() + " Reading Two"); // second reading (replace with comparison setting)
					i++;
				}
				chart.getData().addAll(readingsOne, readingsTwo); // add one
			}
			catch(IndexOutOfBoundsException e)
			{
				System.out.println("Not enough data");
				timePeriod = 14;
				checkCompare(chart, array, device);
			}
		}
		chart.setLegendVisible(true);
		return chart;
	}
}
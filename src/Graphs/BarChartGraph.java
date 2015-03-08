package Graphs;

import java.util.ArrayList;

import Devices.Device;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class BarChartGraph {
	private long readings;
	int timePeriod = 14; // amount of plots on the graph

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BarChart checkCompare(BarChart chart, ArrayList<Integer> array,
			ArrayList<Long> array2, Device device) {
		String id = "compare2"; // if comparing do this, blah.
		XYChart.Series readingsOne = new XYChart.Series();
		readingsOne.setName(device.getName() + " Reading");
		int i = array.size() - timePeriod;
		int lastReadings = 0;

		if (id.equalsIgnoreCase("dontCompare")) // single reading
		{
			try {
				while (i < array.size()) {
					lastReadings = array.get(i);// not sure on which readings
												// are retrieved need to change
					readings = array2.get(i);
					readingsOne.getData().add(
							new XYChart.Data(Integer.toString((int) readings),
									lastReadings));
					i++;
				}
				chart.getData().add(readingsOne); // add one
			} catch (IndexOutOfBoundsException e) {
				System.out
						.println("Not enough data, showing last 2 weeks of data");
				timePeriod = 14;
				checkCompare(chart, array, array2, device);
			}
		} else if (id.equalsIgnoreCase("compare2")) // multiple readings
		{
			XYChart.Series readingsTwo = new XYChart.Series(); // create second
																// series of
																// results

			try {
				while (i < array.size()) {
					lastReadings = array.get(i);
					int random2 = (int) (20 * Math.random() + 15); // get
																	// temperature
																	// etc here
					readings += 50; // get recorded time here
					readingsOne.getData().add(
							new XYChart.Data(Integer.toString((int) readings),
									lastReadings)); // results 1
					readingsTwo.getData().add(
							new XYChart.Data(Integer.toString((int) readings),
									random2)); // results 2
					readingsOne.setName("Reading 1 " + "times");
					readingsTwo.setName("Reading 2 " + "times");
					i++; // ignore
				}
			} catch (IndexOutOfBoundsException e) {
				System.out
						.println("Not enough data, showing last 2 weeks of data");
				timePeriod = 14;
				checkCompare(chart, array, array2, device);
			}

			chart.setBarGap(0);
			chart.setCategoryGap(20);
			chart.getData().addAll(readingsOne, readingsTwo); // add two
		}
		chart.setLegendVisible(true);
		// chart.setStyle();
		return chart; // return
	}
}

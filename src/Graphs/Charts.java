package Graphs;

import java.util.ArrayList;

import Devices.Device;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;

public class Charts {

	String type = "test";
	//Instantiate the Line/Bar graphs + device
	BarChartGraph barChartGraph = new BarChartGraph();
	LineGraph lineChartGraph = new LineGraph();
	Device device;
	//Instantiate the X/Y Axis of the charts
	final CategoryAxis xAxis = new CategoryAxis();
	final NumberAxis yAxis = new NumberAxis();
	
	Scene scene = new Scene(xAxis, 0, 0);

	ArrayList<Integer> lineOneReadings; // Array storing readings for line one
	ArrayList<String> lineOneDates; // Array storing dates for line one

	public Charts(ArrayList<Integer> tempArray, ArrayList<String> tempArray2,Device device, String type) 
	{
		this.lineOneReadings = tempArray; //store the readings
		this.lineOneDates = tempArray2; // store the dates
		this.device = device; // the device
		this.type = type; // the type
	}

	public void show(Pane pane) 
	{
		xAxis.setLabel("Timescale");
		yAxis.setLabel("Readings"); // Change to make more dynamic, heat = C(o) etc... <------------ change

		if (type.equalsIgnoreCase("Bar Chart")) // if user select bar chart, do this
		{
			final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis); // set Axis's
			barChart.setId("BarChart"); // set ID for CSS
			barChartGraph.checkCompare(barChart, lineOneReadings, lineOneDates, device); // send the data over to bar chart to be added to the chart
			for (Node n : barChart.lookupAll(".default-color0.chart-bar")) // Set the colour of the bar in the chart
			{
				n.setStyle("-fx-bar-fill: #00AF33;");
			}
			for (Node n : barChart.lookupAll(".default-color1.chart-bar")) // Set the colour of the bar in the chart
			{
				n.setStyle("-fx-bar-fill: #66CD00;");
			}
			barChart.setPrefSize(800, 400);
			barChart.setLayoutX(0);
			barChart.setLayoutY(150);
			pane.getChildren().add(barChart);
		} 
		else if (type.equalsIgnoreCase("Line Chart"))  // if user select line chart, do this
		{
			final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis); // set Axis's
			lineChartGraph.checkCompare(lineChart, lineOneReadings, lineOneDates, device);  // send the data over to bar chart to be added to the chart
			lineChart.setPrefSize(800, 400);
			lineChart.setLayoutX(0);
			lineChart.setLayoutY(150);
			pane.getChildren().add(lineChart);
		}
	}

}
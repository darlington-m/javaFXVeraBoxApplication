package graphs;

import java.util.ArrayList;

import devices.Device;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

public class Charts {

	String type = "test";
	//Instantiate the Line/Bar graphs + device
	BarChartGraph barChartGraph = new BarChartGraph();
	LineGraph lineChartGraph = new LineGraph();
	Device device;
	//Instantiate the X/Y Axis of the charts
	final CategoryAxis xAxis = new CategoryAxis();
	NumberAxis yAxis = new NumberAxis();
	
	Scene scene = new Scene(xAxis, 0, 0);
	
	ArrayList<ArrayList> readings = new ArrayList<ArrayList>();
	ArrayList<ArrayList> dates = new ArrayList<ArrayList>();
	ArrayList<Device> devices = new ArrayList<Device>();

	ArrayList<Integer> oneLineOfReadings; // Array storing readings for line one
	ArrayList<String> oneLineOfDates; // Array storing dates for line one
	
	int size; // number of readings 
	int position; // number of the current reading
	int callRequest; // checks if one or more graphs for layout
	/*
	 * these variables are used to decide the size and position of the graphs. Further explaination below with the calculations
	 */

	public Charts(ArrayList<ArrayList> readings, ArrayList<ArrayList> dates, ArrayList<Device> devices, String type, int size, int position, int callRequest) 
	{
		this.readings = readings; //store the readings
		this.dates = dates; // store the dates
		this.devices = devices; // the devices
		this.type = type; // the type
		this.size = size;
		this.position = position;
		this.callRequest = callRequest;
	}

	public void show(Pane pane) 
	{
		xAxis.setLabel("Timescale");
		yAxis.setLabel("Readings"); // Change to make more dynamic, heat = C(o) etc... <------------ change

		if (type.equalsIgnoreCase("Bar Chart")) // if user select bar chart, do this
		{
			final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis); // set Axis's
			barChart.setId("BarChart"); // set ID for CSS
			barChartGraph.checkCompare(barChart, readings, dates, devices); // send the data over to bar chart to be added to the chart
			for (Node n : barChart.lookupAll(".default-color0.chart-bar")) // Set the colour of the bar in the chart
			{
				n.setStyle("-fx-bar-fill: #00AF33;");
			}
			for (Node n : barChart.lookupAll(".default-color1.chart-bar")) // Set the colour of the bar in the chart
			{
				n.setStyle("-fx-bar-fill: #66CD00;");
			}
			barChart.setPrefSize(800, 350);
			barChart.setLayoutX(0);
			barChart.setLayoutY(0);
			pane.getChildren().add(barChart);
		} 
		else if (type.equalsIgnoreCase("Line Chart"))  // if user select line chart, do this
		{				
			getYAxis(); // Checks all of the readings to find what the bounds of the axis should be
			final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis); // set Axis's
			lineChartGraph.checkCompare(lineChart, readings, dates, devices);  // send the data over to bar chart to be added to the chart
			if(callRequest==2){
				lineChart.setPrefSize(800, (500)); // Size of the chart will decrease when the number of charts needed increases
				lineChart.setLayoutX(0);
				lineChart.setLayoutY((60)); // calculation to determine y position. If you want to change where the charts
			}
			else{
				lineChart.setPrefSize(800, (350)); // Size of the chart will decrease when the number of charts needed increases
				lineChart.setLayoutX(0);
				lineChart.setLayoutY(((400* position))); // calculation to determine y position. If you want to change where the charts
			}
			pane.getChildren().add(lineChart); // 					 appears in the layoutY then change the 550. 50 determines where to place the first chart.
		}
	}
	
	public void getYAxis(){
		int lowerBound = (int) readings.get(0).get(0);
		int upperBound = (int) readings.get(0).get(0);
		
		// Information for line one displayed on the graph
		for (int j = 0; j < readings.size(); j++){ // for each device
			int i = 0;

			while (i < readings.get(j).size()-1)  // for each reading for the device
			{
				int yAxisReadings = (int) readings.get(j).get(i); // check the reading

				if (yAxisReadings < lowerBound) { // if lower than the current lowest value, make the lower bound
					lowerBound = yAxisReadings;
				}
				if (yAxisReadings > upperBound) {// if higher than the current highest value, make the upper bound
					upperBound = yAxisReadings;
				}

				i++;
			}
		} 
		
		int check =  (int) (lowerBound* 1.1- upperBound* 0.9);
		//System.out.println(check);
		if(check > 35)
		{
			yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/(int)(check/5));	
		}
		else
		{
			yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/4);	
		}
	}
}
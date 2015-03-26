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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
	
	ScrollPane chartsScrollPane = new ScrollPane();

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
		getSkippedReadings();
		
		chartsScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		chartsScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		
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
			if (callRequest == 2) {
				chartsScrollPane.setPrefSize(800, 500);
				chartsScrollPane.setLayoutX(0);
				chartsScrollPane.setLayoutY((60)); // calculation to determine y position. If you want to change where the charts
			
				if (8 * readings.get(0).size() > 800){
					barChart.setPrefSize(8 * readings.get(0).size(), 500);
				} else {
					barChart.setPrefSize(800, 500); // Size of the chart will decrease when the number of charts needed increases
				}
			}
			else
			{
				chartsScrollPane.setPrefSize(800, 350);
				chartsScrollPane.setLayoutX(0);
				chartsScrollPane.setLayoutY(400* position); // calculation to determine y position. If you want to change where the charts
			
				if (8 * readings.get(0).size() > 800){
					barChart.setPrefSize(8 * readings.get(0).size(), 350);
				} else {
					barChart.setPrefSize(800, 350); // Size of the chart will decrease when the number of charts needed increases
				}
			}
			barChart.setId("chartStyling");
			
			chartsScrollPane.setContent(barChart);
			
			pane.getChildren().add(chartsScrollPane);
				
		} 
		else if (type.equalsIgnoreCase("Line Chart"))  // if user select line chart, do this
		{				
			getYAxis(); // Checks all of the readings to find what the bounds of the axis should be
			final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis); // set Axis's
			lineChartGraph.checkCompare(lineChart, readings, dates, devices);  // send the data over to bar chart to be added to the chart
			if(callRequest==2){
				chartsScrollPane.setPrefSize(800, 500);
				chartsScrollPane.setLayoutX(0);
				chartsScrollPane.setLayoutY((60)); // calculation to determine y position. If you want to change where the charts
			
				if (8 * readings.get(0).size() > 800){
					lineChart.setPrefSize(8 * readings.get(0).size(), 500);
				} else {
					lineChart.setPrefSize(800, 500); // Size of the chart will decrease when the number of charts needed increases
				}
			}
			else{
				chartsScrollPane.setPrefSize(800, 370);
				chartsScrollPane.setVmax(0);
				chartsScrollPane.setLayoutX(0);
				chartsScrollPane.setLayoutY(400* position); // calculation to determine y position. If you want to change where the charts
				if (8 * readings.get(0).size() > 800){
					lineChart.setPrefSize(8 * readings.get(0).size(), 350);
					System.out.println("Readings size: " + readings.get(0).size());
				} else {
					lineChart.setPrefSize(800, 350); // Size of the chart will decrease when the number of charts needed increases
				}
			}
			
			lineChart.setId("chartStyling");
			chartsScrollPane.setStyle("-fx-background: rgb(255,255,255); -fx-border-color: white;");
			chartsScrollPane.setContent(lineChart);
			
			pane.getChildren().add(chartsScrollPane); // 					 appears in the layoutY then change the 550. 50 determines where to place the first chart.
		}
	}
	
	public void getYAxis(){
			int lowerBound = 0;
			int upperBound = 0;

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
		
		if(!(upperBound==0))
		{
			int check =  (int) (lowerBound* 1.1- upperBound* 0.9);
			//System.out.println(check);
			if(check < -200)
			{
				yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/(int)(check/20));	
			}
			else if(check < -150)
			{
				yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/(int)(check/15));	
			}
			else if(check < -100)
			{
				yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/(int)(check/10));	
			}
			else if(check < -50)
			{
				yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/(int)(check/8));	
			}
			else if(check < -25)
			{
				yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/(int)(check/5));	
			}
			else if(check > -24)
			{
				yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/(int)(check/4));	
			}
			else
			{
				yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound * 1.1), (int)(check)/3);	
			}
		}
		else
		{
			yAxis = new NumberAxis((int)(lowerBound * 0.9), (int)(upperBound = 5), (int)(5)/3);	
		}
	}
	
	public void getSkippedReadings(){
		ArrayList<Integer> skippedReadings = new ArrayList<Integer>();
		ArrayList<String> skippedDates = new ArrayList<String>();
		ArrayList<ArrayList> newReadings = new ArrayList<ArrayList>();
		ArrayList<ArrayList> newDates = new ArrayList<ArrayList>();
		
		
		for(int j = 0; j < readings.size(); j++){
			
			System.out.println(readings.get(j).size());			
			
			skippedReadings = new ArrayList<Integer>();
			
			if(readings.get(j).size() >= 107000 ){
				for(int skip = 0; skip < readings.get(j).size(); skip+=96){
					skippedReadings.add((int)readings.get(j).get(skip));
				}
				newReadings.add(skippedReadings);
			}
			else if(readings.get(j).size() >= 8064 ){
				for(int skip = 0; skip < readings.get(j).size(); skip+=48){
					skippedReadings.add((int)readings.get(j).get(skip));
				}
				newReadings.add(skippedReadings);
			}
			else if(readings.get(j).size() >= 4032 ){
				for(int skip = 0; skip < readings.get(j).size(); skip+=24){
					skippedReadings.add((int)readings.get(j).get(skip));
				}
				newReadings.add(skippedReadings);
			}
			else if(readings.get(j).size() >= 2016 ){
				for(int skip = 0; skip < readings.get(j).size(); skip+=12){
					skippedReadings.add((int)readings.get(j).get(skip));
				}
				newReadings.add(skippedReadings);
			}
		
			else if(readings.get(j).size() >= 864 ){
				for(int skip = 0; skip < readings.get(j).size(); skip+=6){
					skippedReadings.add((int)readings.get(j).get(skip));
				}
				newReadings.add(skippedReadings);
			}
			else if(readings.get(j).size() >= 288 ){
				for(int skip = 0; skip < readings.get(j).size(); skip+=2){
					skippedReadings.add((int)readings.get(j).get(skip));
				}
				newReadings.add(skippedReadings);
			}
			else
			{
				newReadings.add(readings.get(j));
			}
		}
		
		System.out.println("dates size" + dates.size());
		
		for(int j = 0; j < dates.size(); j++){
			
			System.out.println("dates size 2 " + dates.size());
			System.out.println("dates size 3 " + dates.get(j).size());
			
			skippedDates = new ArrayList<String>();
			
			if(dates.get(j).size() >= 107000 ){
				for(int skip = 0; skip < dates.get(j).size(); skip+=64){
					skippedDates.add((String)dates.get(j).get(skip));
				}
				newDates.add(skippedDates);
			}
			else if(dates.get(j).size() >= 8064 ){
				for(int skip = 0; skip < dates.get(j).size(); skip+=32){
					skippedDates.add((String)dates.get(j).get(skip));
				}
				newDates.add(skippedDates);
			}
			else if(dates.get(j).size() >= 4032 ){
				for(int skip = 0; skip < dates.get(j).size(); skip+=16){
					skippedDates.add((String)dates.get(j).get(skip));
				}
				newDates.add(skippedDates);
			}
			else if(dates.get(j).size() >= 2016 ){
				for(int skip = 0; skip < dates.get(j).size(); skip+=8){
					skippedDates.add((String)dates.get(j).get(skip));
				}
				newDates.add(skippedDates);
			}
			else if(dates.get(j).size() >= 864 ){
				for(int skip = 0; skip < dates.get(j).size(); skip+=3){
					skippedDates.add((String)dates.get(j).get(skip));
				}
				newDates.add(skippedDates);
			}
			else if(dates.get(j).size() >= 288 ){
				System.out.println("dates: " + dates.get(j).size());
				for(int skip = 0; skip < dates.get(j).size(); skip+=2){
					skippedDates.add((String)dates.get(j).get(skip));
				}
				newDates.add(skippedDates);
				System.out.println("new dates: " + newDates.size());
			}
			else
			{
				newDates.add(dates.get(j));
			}
		}

		//override
		readings = newReadings;
		dates = newDates;
	}
}
package Graphs;

import java.util.ArrayList;

import Devices.Device;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;

public class Charts {


	String test = "test1";
	

	//chart stuff
	BarChartGraph barChartGraph =  new BarChartGraph();
	LineGraph lineChartGraph =  new LineGraph();

	final CategoryAxis xAxis = new CategoryAxis();
	final NumberAxis yAxis = new NumberAxis();
	Scene scene = new Scene(xAxis, 0, 0);
	Device device;

	ArrayList<Integer> array;
	public Charts(ArrayList<Integer> tempArray, Device device){
		this.array = tempArray;
		this.device = device;
	}
	
	public void show(Pane pane) 
	{
		
		//stage.setTitle("Relevant shit");
		xAxis.setLabel("The time in which the reading was recorded");
		yAxis.setLabel("Whatever measurement you're reading in");
		
		if(test.equalsIgnoreCase("test"))
		{
			final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
			barChart.setId("BarChart");
			barChartGraph.checkCompare(barChart, array, device);
			for(Node n:barChart.lookupAll(".default-color0.chart-bar")) 
			{
				n.setStyle("-fx-bar-fill: #00AF33;");

			}
			for(Node n:barChart.lookupAll(".default-color1.chart-bar")) 
			{
				n.setStyle("-fx-bar-fill: #66CD00;");
			}
			barChart.setPrefSize(700,350);
			barChart.setLayoutX(0);
			barChart.setLayoutY(150);
			pane.getChildren().add(barChart);
		//	scene = new Scene(barChart, 800, 600);
		}
		else if(test.equalsIgnoreCase("test1"))
		{
			final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
			lineChart.setId("BarChart");
			lineChartGraph.checkCompare(lineChart, array, device);
			lineChart.setPrefSize(700,350);
			lineChart.setLayoutX(0);
			lineChart.setLayoutY(150);
			pane.getChildren().add(lineChart);
	//		scene = new Scene(lineChart, 800, 600);
		}
	//	stage.setScene(scene);
	//	scene.getStylesheets().add(Charts.class.getResource("/Resources/css.css").toExternalForm());
	//	stage.show();
	}

}
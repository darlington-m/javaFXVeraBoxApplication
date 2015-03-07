package Exports;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import DataRetrival.MySQLConnect;

public final class CSV {
	
	public void toCSV(File file, int id, long startDate, long endDate) throws SQLException, IOException{
		
		//CSV csv = new CSV();
		//csv.toCSV(14, 1425583201, 1425585602); // Specific id and date.
		//csv.toCSV(-1, 0000000000, 9999999999L); // Will retrieve everything.
		
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_HH.mm.ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		
//		File file = new File(dateFormat.format(date) + ".csv");
//		if (!file.exists()) {
//			file.createNewFile();
//		}
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		MySQLConnect conn;
		conn = new MySQLConnect();
		
		String sqlStatement = "SELECT * FROM Reading WHERE ";
		if (id != -1){
			sqlStatement += "id = '" + id + "' AND ";
		}
		sqlStatement += "reading_date >='" + startDate + "' AND reading_date <='" + endDate + "'";
		
		//ResultSet results = conn.getRows("SELECT * FROM Reading WHERE id = '" + id + "' AND reading_date >='" + startDate + "' AND reading_date <='" + endDate + "'");
		ResultSet results = conn.getRows(sqlStatement);
		
		results.first();
		bufferedWriter.write("reading_id,reading_date,reading_device_name,id,altid,category,subcategory,room,parent,batterylevel,temperature,light,humidity,armedtripped,armed,lasttripped,tripped,state,reading_comment,setpoint,heat,cool,status,commands,reading_mode,chcnt\n");
		do{
			int i = 1;
			for(i = 1; i < 27; i++){
				bufferedWriter.append(results.getString(i));
				if (i < 26){
					bufferedWriter.append(",");
				}
			}
			bufferedWriter.append("\n");
		} while(results.next());

		bufferedWriter.close();
		System.out.println("Test complete");
	}
}

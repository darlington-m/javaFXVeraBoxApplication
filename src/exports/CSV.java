package exports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataretrival.MySQLConnect;
import devices.Device;

public final class CSV {

	public void toCSV(File file, Device device, long startDate, long endDate)
			throws SQLException, IOException {

		// CSV csv = new CSV();
		// csv.toCSV(14, 1425583201, 1425585602); // Specific id and date.
		// csv.toCSV(-1, 0000000000, 9999999999L); // Will retrieve everything.

		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		MySQLConnect conn;
		conn = new MySQLConnect();

		String sqlStatement = "SELECT reading_date," + device.getReadingName() +  " FROM Reading WHERE ";
		if (device.getId() != -1) {
			sqlStatement += "id = '" + device.getId() + "' AND ";
		}
		sqlStatement += "reading_date >='" + startDate
				+ "' AND reading_date <='" + endDate + "'";

		// ResultSet results = conn.getRows("SELECT * FROM Reading WHERE id = '"
		// + id + "' AND reading_date >='" + startDate +
		// "' AND reading_date <='" + endDate + "'");
		ResultSet results = conn.getRows(sqlStatement);

		results.first();
		bufferedWriter
				.write("reading_date," + device.getReadingName() + "\n");
		do {
			int i = 1;
			for (i = 1; i < 3; i++) {
				bufferedWriter.append(results.getString(i));
				if (i < 2) {
					bufferedWriter.append(",");
				}
			}
			bufferedWriter.append("\n");
		} while (results.next());

		bufferedWriter.close();
		System.out.println("Test complete");
	}
}

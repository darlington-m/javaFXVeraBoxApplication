package testing;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import dataretrival.MySQLConnect;

/**
 * 
 * @author Remus
 *
 */
public class MySQLConnectTest {

	//@Test
//	public void testInsertRow() throws SQLException {
//		///fail("Not yet bla");
//		
//		MySQLConnect sql = new MySQLConnect();
//		ResultSet resultSet = sql.getRows("SELECT * FROM Room");
//		sql.insertRow("INSERT INTO Room (room_id , room_name , section)"
//				+ "VALUES (4, 'testX',2)");
//		
//		ArrayList<Object> actualValueList1 = new ArrayList<Object>();
//		ArrayList<Object> expectValueList1 = new ArrayList<Object>();
//		
//		expectValueList1.add(1);
//		expectValueList1.add(2);
//		expectValueList1.add(3);
//		expectValueList1.add("Lab");
//		expectValueList1.add("xcvscasc");
//		expectValueList1.add("test");
//		expectValueList1.add(1);
//		expectValueList1.add(1);
//		expectValueList1.add(1);
//		expectValueList1.add(4);
//		expectValueList1.add("testX");
//		expectValueList1.add(2);
//		
//		while (resultSet.next()){
//			actualValueList1.add(resultSet.getInt("room_id"));
//			actualValueList1.add(resultSet.getString("room_name"));
//			actualValueList1.add(resultSet.getInt("section"));
//				
//		}
//		assertEquals(actualValueList1, expectValueList1);
//		
//	}

	@Test
	public void testGetRows() throws SQLException {
		MySQLConnect sql = new MySQLConnect();
		ResultSet resultSet = sql.getRows("SELECT * FROM Room");
		ArrayList<Object> actualValueList1 = new ArrayList<Object>();
		ArrayList<Object> expectValueList1 = new ArrayList<Object>();
		ArrayList<Object> actualValueList2 = new ArrayList<Object>();
		ArrayList<Object> expectValueList2 = new ArrayList<Object>();
		ArrayList<Object> actualValueList3 = new ArrayList<Object>();
		ArrayList<Object> expectValueList3 = new ArrayList<Object>();
		expectValueList1.add(1);
		expectValueList1.add(2);
		expectValueList1.add(3);
		expectValueList2.add("Lab");
		expectValueList2.add("xcvscasc");
		expectValueList2.add("test");
		expectValueList3.add(1);
		expectValueList3.add(1);
		expectValueList3.add(1);
		
		while (resultSet.next()){
			actualValueList1.add(resultSet.getInt("room_id"));
			actualValueList2.add(resultSet.getString("room_name"));
			actualValueList3.add(resultSet.getInt("section"));
			
		}
		
		assertEquals(actualValueList1, expectValueList1);
		assertEquals(actualValueList2, expectValueList2);
		assertEquals(actualValueList3, expectValueList3);
	
	}

}

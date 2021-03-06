package dataretrival;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import devices.Data;
import devices.Device;

@RunWith(Suite.class)
@SuiteClasses({})
public class AllTests {

	@SuppressWarnings("null")
	@Test
	public void checkRadiator() {
		Gson gson = new Gson();
		Data data;

		// Factory to be tested
	//	GSONObjectFactory factory = new GSONObjectFactory();

		// object to be tested
		Device testObj = null;
		try {

			BufferedReader br = new BufferedReader(new FileReader(
					"resources/radiatortestfile.json"));

			// convert the json string back to jsonArray
			data = gson.fromJson(br, Data.class);
			// get added item from array
			for (@SuppressWarnings("unused") JsonElement x : data.getDevices()) {

		//		testObj = factory.toDeviceObject(x);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("Name from JSON is Danfoss Radiator", "Danfoss Radiator",
				testObj.getName());
		assertEquals("id from JSON is 9", 9, testObj.getId());
		assertEquals("room from JSON is 1", 1, testObj.getRoom());
		assertEquals("category from JSON is 5", 5, testObj.getCategory());
		// start garbage collection
		data = null;

	}
}

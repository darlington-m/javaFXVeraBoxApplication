package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import Devices.DanfossRadiator;
import Devices.DataMining;
import Devices.Device;
import Devices.FourInOne;
import Devices.HumiditySensor;
import Devices.LightSensor;
import Devices.Room;
import Devices.TemperatureSensor;

import com.google.gson.Gson;

public class Test {
	
	public Test(){
		
	}
	
	public ArrayList<Room> run(){
		ArrayList<Device> list = new ArrayList<Device>();
		ArrayList<Room> rooms = new ArrayList<Room>();
		Gson gson = new Gson();
		try {
			BufferedReader br;
			
			File radiatorFile = new File("src/Resources/Test/RadiatorTest.json");
			File lightFile = new File("src/Resources/Test/LightTest.json");
			File dataMiningFile = new File("src/Resources/Test/DataMiningTest.json");
			File humidityFile = new File("src/Resources/Test/HumidityTest.json");
			File tempFile = new File("src/Resources/Test/TempTest.json");
			File fourInOneFile = new File("src/Resources/Test/4in1Test.json");

			File labRoom = new File("src/Resources/Test/LabRoom.json");
			File livingRoom = new File("src/Resources/Test/LivingRoom.json");
			
			br = new BufferedReader(new FileReader(radiatorFile));
			DanfossRadiator radiator = new DanfossRadiator(); 
			radiator =  gson.fromJson(br, DanfossRadiator.class);
			list.add(radiator);
			
			br = new BufferedReader(new FileReader(lightFile));
			LightSensor light = new LightSensor();
			light = gson.fromJson(br, LightSensor.class);
			list.add(light);
			
//			br = new BufferedReader(new FileReader(dataMiningFile));
//			DataMining dataMine = new DataMining();
//			dataMine = gson.fromJson(br, DataMining.class);
//			list.add(dataMine);
			
			br = new BufferedReader(new FileReader(humidityFile));
			HumiditySensor humidity = new HumiditySensor();
			humidity = gson.fromJson(br, HumiditySensor.class);
			list.add(humidity);
			
			br = new BufferedReader(new FileReader(tempFile));
			TemperatureSensor temp = new TemperatureSensor(); 
			temp = gson.fromJson(br, TemperatureSensor.class);
			list.add(temp);
			
			BufferedReader br4 = new BufferedReader(new FileReader(fourInOneFile));
			FourInOne fourinOne = new FourInOne();
			fourinOne = gson.fromJson(br4, FourInOne.class);
			
			
			br = new BufferedReader(new FileReader(humidityFile));
			HumiditySensor humidity2 = new HumiditySensor();
			humidity2 = gson.fromJson(br, HumiditySensor.class);
			//fourinOne.setHumidity(humidity2);
			
			br = new BufferedReader(new FileReader(tempFile));
			TemperatureSensor temp2 = new TemperatureSensor(); 
			temp2 = gson.fromJson(br, TemperatureSensor.class);
			//fourinOne.setTemp(temp2);
			
			br = new BufferedReader(new FileReader(lightFile));
			LightSensor light2 = new LightSensor();
			light2 = gson.fromJson(br, LightSensor.class);
			//fourinOne.setLight(light2);
			
			list.add(fourinOne);
			
			// create the rooms and add the devices
			
			br = new BufferedReader(new FileReader(labRoom));
			Room lab = gson.fromJson(br, Room.class);
			rooms.add(lab);
			
			br = new BufferedReader(new FileReader(livingRoom));
			Room living = gson.fromJson(br, Room.class);
			rooms.add(living);
			
			for(Room currentRoom: rooms){
				for(Device currentDevice: list){
					if(currentDevice.getRoom() == currentRoom.getId()){
						currentRoom.addDeviceToRoom(currentDevice);
					}
				}
			}
			
			
			
			System.out.println("Devices : " + list.size());
			System.out.println("Rooms : " + rooms.size());
			
			System.out.println("Room 1 has " + rooms.get(0).getDevices().size() + " devices");
			System.out.println("Room 2 has " + rooms.get(1).getDevices().size() + " devices");
			System.out.println("\n\n ---- TEST COMPLETE ---- \n\n");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return rooms;
	}
}
import com.google.gson.*;


public class GSONObjectFactory {

	GSONObjectFactory(){
		
		
	}
	public Device toDeviceObject(JsonElement x){

		Gson gson  = new Gson();
		// cast the element to an object
		JsonObject object = x.getAsJsonObject();
		// put to an object
		int xa = Integer.parseInt(object.get("id").toString());
		
		switch(xa){
		case 11 :
			FourInOne four = new FourInOne();
			TemperatureSensor temperaturesensor = gson.fromJson(object, TemperatureSensor.class);
			HumiditySensor humiditysensor = gson.fromJson(object, HumiditySensor.class);
			LightSensor lightsensor = gson.fromJson(object, LightSensor.class);
	
			four = gson.fromJson(object, FourInOne.class);
	
			four.setHumidity(humiditysensor);
			four.setLight(lightsensor);
			four.setTemp(temperaturesensor);
			
			//devices.add(four);
			return four;
			
		case 9 :
			DanfossRadiator radiator = new DanfossRadiator();
			radiator =  gson.fromJson(object, DanfossRadiator.class);
		//	System.out.println(radiator);
			//devices.add(radiator);
			return radiator;
		case 16 :
			DataMining datamining = new DataMining();
			datamining = gson.fromJson(object, DataMining.class);
			//System.out.println(datamining);
			//devices.add(datamining);
			return datamining;
		case 14 :
			HumiditySensor humiditySensor = new HumiditySensor();
			humiditySensor = gson.fromJson(object, HumiditySensor.class);
			//System.out.println(humiditySensor);
			//devices.add(humiditySensor);
			return humiditySensor;
		case 13 :
			LightSensor lightSensor = new LightSensor();
			lightSensor = gson.fromJson(object, LightSensor.class);
			//System.out.println(lightSensor);
			System.out.println(lightSensor.readingToSQL());
			//conn.insertRow(lightSensor.readingToSQL());
			//devices.add(lightSensor);
			return lightSensor;
		case 12 :
			TemperatureSensor temperatureSensor = new TemperatureSensor();
			temperatureSensor = gson.fromJson(object, TemperatureSensor.class);
			//System.out.println(temperatureSensor);
			//devices.add(temperatureSensor);
			return temperatureSensor;      			
		}
		return null;
	}
	public Room toRoomObject(JsonElement x){
		Gson gson  = new Gson();
		JsonObject object = x.getAsJsonObject();
		return gson.fromJson(object, Room.class);
	}
}

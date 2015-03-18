package DataRetrival;

import java.util.TimerTask;

import GUI.VeraGUI;

public class ReadingsUpdateTimer extends TimerTask {
	
	private VeraGUI veraGUI;

	public ReadingsUpdateTimer(VeraGUI veraGUI) {
		// TODO Auto-generated constructor stub
		this.veraGUI = veraGUI;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		veraGUI.setRoomsList(new CurrentReadings().getRooms());
		veraGUI.setDevicesList(new CurrentReadings().getAllDevices());
		
	}

}

package DataRetrival;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.TimerTask;

//import GUI.VeraGUI;

public class ReadingTimer extends TimerTask {

	@Override
	public void run() {
		try {
			JsonToJava.getData();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Reading taken");
	}

}

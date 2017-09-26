package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Test;

public class ClientMessagesTest {

	private BufferedReader in;
	private OutputStreamWriter out;
	private Socket socket;

	public void setup() {
		try {
			socket = new Socket("localhost", 4455);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true)
					receiveGeneralMessage();
			}

		}).start();
	}

	private void receiveGeneralMessage() {

		String jsonText = null;
		try {
			jsonText = in.readLine();
			if (jsonText != null) {
				JSONObject jsonObject = new JSONObject(jsonText);
				String s = jsonObject.optString("type");
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		System.out.println(jsonText);
	}

	private void sendGeneralMessage(JSONObject json) {
		try {
			out.write(json.toString() + "\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test() {
		setup();

		JSONObject json = new JSONObject();
		 try {
//		 json.put("type", "join game");
//		 json.put("nameOfGame", "TH");
//		 json.put("game id", 0);
//		 json.put("color", "java.awt.Color[/rasdfasdf]");
		 json.put("type", "login");
		 json.put("nick", "ki");
		 } catch (JSONException e) {
		 e.printStackTrace();
		 }
		sendGeneralMessage(json);

		while (true) {
		}
	}
	
}

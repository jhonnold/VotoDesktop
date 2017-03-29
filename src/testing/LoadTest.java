package testing;

import java.util.ArrayList;
import java.util.Random;

public class LoadTest {

	public static void main(String[] args) {

		ArrayList<Client> fakeClients = new ArrayList<>();
		
		int count = 200;

		for (int i = 0; i < count; i++) {
			fakeClients.add(new Client("141.219.152.185", getSaltString(), i));
		}


		for (Client c : fakeClients) {
			new Thread(c).start();
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				
			}
		}

	}

	protected static String getSaltString() {

		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();

		while (salt.length() < rnd.nextInt(20) + 10) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}

		String saltStr = salt.toString();
		return saltStr;

	}

}

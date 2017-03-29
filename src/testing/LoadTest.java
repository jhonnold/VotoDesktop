package testing;

import java.util.ArrayList;
import java.util.Random;

public class LoadTest {

	ArrayList<Client> fakeClients;
	int count = 8;

	public static void main(String[] args) {

		ArrayList<Client> fakeClients = new ArrayList<>();
		;
		int count = 8;

		for (int i = 0; i < count; i++) {
			fakeClients.add(new Client("192.168.1.11", getSaltString(), i));
		}

		count *= 2;

		for (Client c : fakeClients) {
			new Thread(c).start();
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

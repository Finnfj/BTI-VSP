package chat;

import java.rmi.RemoteException;

public class Main {

	public static void main(String[] args) throws RemoteException {
		Server server = new Server(8, 30);
		Client client = new Client("localhost", 30);

		client.sendMessage("HAHAHAHA");
		client.getMessage();
		client.getMessage();
		client.getMessage();
		client.sendMessage("HöHAHAHA");
		client.getMessage();
	}

}

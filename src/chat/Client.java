package chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	private static final String INTERFACENAME = "MessageService";
	private RemoteInterface server;
	private String clientID;
	private int timeout;
	
	public Client(String serverAddress, int timeout) throws RemoteException {
		try {
			this.clientID = System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostName();
			Registry registry = LocateRegistry.getRegistry(serverAddress);
			server = (RemoteInterface) registry.lookup(INTERFACENAME);
		} catch (UnknownHostException e) {
			System.out.println("Konnte eigene IP-Adresse nicht ermitteln");
			e.printStackTrace();		
		} catch (NotBoundException e) {
			System.out.println("Remote Objekt konnte in der Registry nicht gefunden werden");
			e.printStackTrace();			
		}
		this.timeout = timeout;
	}

	public String getMessage() throws RemoteException, InterruptedException {
		try {
			return server.nextMessage(clientID);
		} catch (RemoteException e) {
			// Try on for timeout seconds
			long timeoutStart = System.currentTimeMillis();
			while (System.currentTimeMillis() < (timeoutStart + timeout*1000)) {
				Thread.sleep(1000);
				try {
					return server.nextMessage(clientID);
				} catch (RemoteException e2) {	}
			}
			e.printStackTrace();
			return null;
		}
	}
	
	public void sendMessage(String message) throws RemoteException, InterruptedException {
		try {
			server.newMessage(clientID, message);
		} catch (RemoteException e) {
			// Try on for timeout seconds
			long timeoutStart = System.currentTimeMillis();
			while (System.currentTimeMillis() < (timeoutStart + timeout*1000)) {
				Thread.sleep(1000);
				try {
					server.newMessage(clientID, message);
				} catch (RemoteException e2) {	}
			}
			e.printStackTrace();
		}
	}
}

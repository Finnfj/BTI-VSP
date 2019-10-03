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
	private String chatlog;
	
	public Client(String serverAddress, int timeout) {
		try {
			this.clientID = System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostName();
			Registry registry = LocateRegistry.getRegistry(serverAddress);
			server = (RemoteInterface) registry.lookup(INTERFACENAME);
		} catch (UnknownHostException e) {
			System.out.println("Konnte eigene IP-Adresse nicht ermitteln");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.out.println("Fehler bei Kommunikation mit RMI-Server");
			e.printStackTrace();			
		} catch (NotBoundException e) {
			System.out.println("Remote Objekt konnte in der Registry nicht gefunden werden");
			e.printStackTrace();			
		}
		this.timeout = timeout;
		this.chatlog = "";
	}

	public void getMessage() throws RemoteException {
		System.out.println("CLIENT: " + server.nextMessage(clientID));
	}
	
	public void sendMessage(String message) throws RemoteException {
		server.newMessage(clientID, message);
	}
}

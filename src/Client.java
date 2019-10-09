

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {
	private static final String INTERFACENAME = "MessageService";
	private MessageService server;
	private String clientID;
	private int timeout;
	private String serverAddress;
	private Registry registry;
	
	public Client(String serverAddress, int timeout) {
		System.setProperty("java.security.policy", "file:server.policy");
		System.setSecurityManager(new SecurityManager());
			try {
				this.clientID = System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				System.out.println("Konnte eigenen Hostname nicht ermitteln");
				e.printStackTrace();
			}
			this.serverAddress = serverAddress;
			connect();
		this.timeout = timeout;
	}
	
	public void stop() {
		try {
			UnicastRemoteObject.unexportObject(server, true);
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getMessage() throws InterruptedException {
		try {
			return server.nextMessage(clientID);
		} catch (RemoteException e) {
//			// Try on for timeout seconds
//			long timeoutStart = System.currentTimeMillis();
//			while (System.currentTimeMillis() < (timeoutStart + timeout*1000)) {
//				Thread.sleep(1000);
//				try {
//					return server.nextMessage(clientID);
//				} catch (RemoteException e2) {	}
//			}
//			e.printStackTrace();
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
					Registry registry = LocateRegistry.getRegistry(serverAddress);
					server = (MessageService) registry.lookup(INTERFACENAME);
					server.newMessage(clientID, message);
					return;
				} catch (RemoteException | NotBoundException e2) {	}
			}
			e.printStackTrace();
		}
	}
	
	private void connect() {
		try {
			registry = LocateRegistry.getRegistry(serverAddress);
			server = (MessageService) registry.lookup(INTERFACENAME);
		} catch (RemoteException e) {
			System.out.println("Konnte nicht mit angegebener Adresse verbinden");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Konnte keine Registry unter diesem Namen ermitteln");
			e.printStackTrace();
		}
	}
}
